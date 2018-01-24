package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import player.Player;
import player.PlayerID;
import exceptions.IllegalPlayException;

public class Board extends Observable {
	
	public static final int NUM_NODES = 24;
	public static final int NUM_SEGMENTS = 20;
	
	private Node[] nodes;
	private Segment[] segments;
	
	public Board() {
		this.setupNodes();
		this.setupSegments();
		this.addSegmentsToNodes();
	}
	
	public void update() {
		this.notifyObservers();
		this.setChanged();
	}
	
	public void place(Player placer, int destination) throws IllegalPlayException {
		if( this.nodes[destination].isPossessed() )
			throw new IllegalPlayException(placer.id(), IllegalPlayException.Nature.PLACE_TO_OCCUPIED_NODE);
		
		this.clearEphemeralNodes(placer.id());
		this.nodes[destination].acceptOccupance(placer);
		
		for(Segment s : this.nodes[destination].getSegments())
			if( s.isMill() )
				s.setEphemeral(true);
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public void remove(Player remover, int source) throws IllegalPlayException {
		Node srcNode = this.nodes[source];
		if( !this.nodes[source].isPossessed() )
			throw new IllegalPlayException(remover.id(), IllegalPlayException.Nature.REMOVE_FROM_UNOCCUPIED_NODE);
		if( this.nodes[source].getPossessor().id() == remover.id() && !srcNode.isEphemeral() )
			throw new IllegalPlayException(remover.id(), IllegalPlayException.Nature.UNEPHEMERAL_NODE_REMOVE_ATTEMPT);
		if(this.getNode(source).isInMill() && remover.id() != srcNode.getPossessor().id())
			throw new IllegalPlayException(remover.id(), IllegalPlayException.Nature.MILLSTONE_CAPTURE_ATTEMPT);
		
		if( remover.canSlashMustCapture() && srcNode.getPossessor().id() == remover.id() )
			throw new IllegalPlayException(remover.id(), IllegalPlayException.Nature.MUST_CAPTURE_IGNORE);
		if( !remover.canSlashMustCapture() && srcNode.getPossessor().id() != remover.id() )
			throw new IllegalPlayException(remover.id(), IllegalPlayException.Nature.ILLEGAL_CAPTURE_ATTEMPT);
		
		if( remover.id() == srcNode.getPossessor().id() )
			this.clearEphemeralNodes(remover.id());
		srcNode.surrenderOccupance();
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public void move(Player mover, int source, int destination) throws IllegalPlayException {
		if( !this.nodes[source].isPossessed() )
			throw new IllegalPlayException(mover.id(), IllegalPlayException.Nature.MOVE_FROM_UNOCCUPIED_NODE);
		if( this.nodes[source].getPossessor().id() != mover.id() )
			throw new IllegalPlayException(mover.id(), IllegalPlayException.Nature.ILLEGAL_MOVE_ATTEMPT);
		if( this.nodes[destination].isPossessed() )
			throw new IllegalPlayException(mover.id(), IllegalPlayException.Nature.MOVE_TO_OCCUPIED_NODE);
		
		this.clearEphemeralNodes(mover.id());
		this.nodes[destination].acceptOccupance(this.nodes[source].surrenderOccupance());
		
		for(Segment s : this.nodes[destination].getSegments())
			if( s.isMill() )
				s.setEphemeral(true);
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public void clearEphemeralNodes(PlayerID id) {
		for( int i = 0; i < NUM_SEGMENTS; ++i )
			if( this.segments[i].isMill() && this.segments[i].getMiller().id() == id) {
				this.segments[i].setEphemeral(false);
			}
	}
	
	public List<Node> getPossession(PlayerID pc) {
		ArrayList<Node> ret = new ArrayList<Node>();
		for(int i = 0; i < NUM_NODES; ++i)
			if( this.nodes[i].isPossessed() && this.nodes[i].getPossessor().id() == pc)
				ret.add(this.nodes[i]);
		return ret;
	}
	
	public List<Node> getUnpossessed() {
		ArrayList<Node> ret = new ArrayList<Node>();
		for(int i = 0; i < NUM_NODES; ++i)
			if( !this.nodes[i].isPossessed() )
				ret.add(this.nodes[i]);
		return ret;
	}
	
	public Node getNode(int id) {
		return this.nodes[id];
	}
	
	public Segment getSegment(int id) {
		return this.segments[id];
	}
	
	private void setupNodes() {
		this.nodes = new Node[NUM_NODES];
		for(int i = 0; i < NUM_NODES; ++i)
			this.nodes[i] = new Node(i);
		
		//Node 0
		this.nodes[0].addNeigbor(this.nodes[1]);
		this.nodes[0].addNeigbor(this.nodes[3]);
		this.nodes[0].addNeigbor(this.nodes[9]);
		
		//Node 1
		this.nodes[1].addNeigbor(this.nodes[0]);
		this.nodes[1].addNeigbor(this.nodes[2]);
		this.nodes[1].addNeigbor(this.nodes[4]);
		
		//Node 2
		this.nodes[2].addNeigbor(this.nodes[1]);
		this.nodes[2].addNeigbor(this.nodes[5]);
		this.nodes[2].addNeigbor(this.nodes[14]);
		
		//Node 3
		this.nodes[3].addNeigbor(this.nodes[0]);
		this.nodes[3].addNeigbor(this.nodes[4]);
		this.nodes[3].addNeigbor(this.nodes[6]);
		this.nodes[3].addNeigbor(this.nodes[10]);
		
		//Node 4
		this.nodes[4].addNeigbor(this.nodes[1]);
		this.nodes[4].addNeigbor(this.nodes[3]);
		this.nodes[4].addNeigbor(this.nodes[5]);
		this.nodes[4].addNeigbor(this.nodes[7]);
		
		//Node 5
		this.nodes[5].addNeigbor(this.nodes[2]);
		this.nodes[5].addNeigbor(this.nodes[4]);
		this.nodes[5].addNeigbor(this.nodes[8]);
		this.nodes[5].addNeigbor(this.nodes[13]);
		
		//Node 6
		this.nodes[6].addNeigbor(this.nodes[3]);
		this.nodes[6].addNeigbor(this.nodes[7]);
		this.nodes[6].addNeigbor(this.nodes[11]);
		
		//Node 7
		this.nodes[7].addNeigbor(this.nodes[4]);
		this.nodes[7].addNeigbor(this.nodes[6]);
		this.nodes[7].addNeigbor(this.nodes[8]);
		
		//Node 8
		this.nodes[8].addNeigbor(this.nodes[5]);
		this.nodes[8].addNeigbor(this.nodes[7]);
		this.nodes[8].addNeigbor(this.nodes[12]);
		
		//Node 9
		this.nodes[9].addNeigbor(this.nodes[0]);
		this.nodes[9].addNeigbor(this.nodes[10]);
		this.nodes[9].addNeigbor(this.nodes[21]);
		
		//Node 10
		this.nodes[10].addNeigbor(this.nodes[3]);
		this.nodes[10].addNeigbor(this.nodes[9]);
		this.nodes[10].addNeigbor(this.nodes[11]);
		this.nodes[10].addNeigbor(this.nodes[18]);
		
		//Node 11
		this.nodes[11].addNeigbor(this.nodes[6]);
		this.nodes[11].addNeigbor(this.nodes[10]);
		this.nodes[11].addNeigbor(this.nodes[15]);
		
		//Node 12
		this.nodes[12].addNeigbor(this.nodes[8]);
		this.nodes[12].addNeigbor(this.nodes[13]);
		this.nodes[12].addNeigbor(this.nodes[17]);
		
		//Node 13
		this.nodes[13].addNeigbor(this.nodes[5]);
		this.nodes[13].addNeigbor(this.nodes[12]);
		this.nodes[13].addNeigbor(this.nodes[14]);
		this.nodes[13].addNeigbor(this.nodes[20]);
		
		//Node 14
		this.nodes[14].addNeigbor(this.nodes[2]);
		this.nodes[14].addNeigbor(this.nodes[13]);
		this.nodes[14].addNeigbor(this.nodes[23]);
		
		//Node 15
		this.nodes[15].addNeigbor(this.nodes[11]);
		this.nodes[15].addNeigbor(this.nodes[16]);
		this.nodes[15].addNeigbor(this.nodes[18]);
		
		//Node 16
		this.nodes[16].addNeigbor(this.nodes[15]);
		this.nodes[16].addNeigbor(this.nodes[17]);
		this.nodes[16].addNeigbor(this.nodes[19]);
		
		//Node 17
		this.nodes[17].addNeigbor(this.nodes[12]);
		this.nodes[17].addNeigbor(this.nodes[16]);
		this.nodes[17].addNeigbor(this.nodes[20]);
		
		//Node 18
		this.nodes[18].addNeigbor(this.nodes[10]);
		this.nodes[18].addNeigbor(this.nodes[15]);
		this.nodes[18].addNeigbor(this.nodes[19]);
		this.nodes[18].addNeigbor(this.nodes[21]);
		
		//Node 19
		this.nodes[19].addNeigbor(this.nodes[16]);
		this.nodes[19].addNeigbor(this.nodes[18]);
		this.nodes[19].addNeigbor(this.nodes[20]);
		this.nodes[19].addNeigbor(this.nodes[22]);
		
		//Node 20
		this.nodes[20].addNeigbor(this.nodes[13]);
		this.nodes[20].addNeigbor(this.nodes[17]);
		this.nodes[20].addNeigbor(this.nodes[19]);
		this.nodes[20].addNeigbor(this.nodes[23]);
		
		//Node 21
		this.nodes[21].addNeigbor(this.nodes[9]);
		this.nodes[21].addNeigbor(this.nodes[18]);
		this.nodes[21].addNeigbor(this.nodes[22]);
		
		//Node 22
		this.nodes[22].addNeigbor(this.nodes[19]);
		this.nodes[22].addNeigbor(this.nodes[21]);
		this.nodes[22].addNeigbor(this.nodes[23]);
		
		//Node 23
		this.nodes[23].addNeigbor(this.nodes[14]);
		this.nodes[23].addNeigbor(this.nodes[20]);
		this.nodes[23].addNeigbor(this.nodes[22]);
	}
	
	private void setupSegments() {
		this.segments = new Segment[NUM_SEGMENTS];
		for(int i = 0; i < NUM_SEGMENTS; ++i)
			this.segments[i] = new Segment(i);
		
		//Segment 0
		this.segments[0].addNode(this.nodes[0]);
		this.segments[0].addNode(this.nodes[1]);
		this.segments[0].addNode(this.nodes[2]);
		
		//Segment 1
		this.segments[1].addNode(this.nodes[3]);
		this.segments[1].addNode(this.nodes[4]);
		this.segments[1].addNode(this.nodes[5]);
		
		//Segment 2
		this.segments[2].addNode(this.nodes[6]);
		this.segments[2].addNode(this.nodes[7]);
		this.segments[2].addNode(this.nodes[8]);
		
		//Segment 3
		this.segments[3].addNode(this.nodes[15]);
		this.segments[3].addNode(this.nodes[16]);
		this.segments[3].addNode(this.nodes[17]);
		
		//Segment 4
		this.segments[4].addNode(this.nodes[18]);
		this.segments[4].addNode(this.nodes[19]);
		this.segments[4].addNode(this.nodes[20]);
		
		//Segment 5
		this.segments[5].addNode(this.nodes[21]);
		this.segments[5].addNode(this.nodes[22]);
		this.segments[5].addNode(this.nodes[23]);
		
		//Segment 6
		this.segments[6].addNode(this.nodes[0]);
		this.segments[6].addNode(this.nodes[9]);
		this.segments[6].addNode(this.nodes[21]);
		
		//Segment 7
		this.segments[7].addNode(this.nodes[3]);
		this.segments[7].addNode(this.nodes[10]);
		this.segments[7].addNode(this.nodes[18]);
		
		//Segment 8
		this.segments[8].addNode(this.nodes[6]);
		this.segments[8].addNode(this.nodes[11]);
		this.segments[8].addNode(this.nodes[15]);
		
		//Segment 9
		this.segments[9].addNode(this.nodes[8]);
		this.segments[9].addNode(this.nodes[12]);
		this.segments[9].addNode(this.nodes[17]);
		
		//Segment 10
		this.segments[10].addNode(this.nodes[5]);
		this.segments[10].addNode(this.nodes[13]);
		this.segments[10].addNode(this.nodes[20]);
		
		//Segment 11
		this.segments[11].addNode(this.nodes[2]);
		this.segments[11].addNode(this.nodes[14]);
		this.segments[11].addNode(this.nodes[23]);
		
		//Segment 12
		this.segments[12].addNode(this.nodes[1]);
		this.segments[12].addNode(this.nodes[4]);
		this.segments[12].addNode(this.nodes[7]);
		
		//Segment 13
		this.segments[13].addNode(this.nodes[2]);
		this.segments[13].addNode(this.nodes[5]);
		this.segments[13].addNode(this.nodes[8]);
		
		//Segment 14
		this.segments[14].addNode(this.nodes[12]);
		this.segments[14].addNode(this.nodes[13]);
		this.segments[14].addNode(this.nodes[14]);
		
		//Segment 15
		this.segments[15].addNode(this.nodes[17]);
		this.segments[15].addNode(this.nodes[20]);
		this.segments[15].addNode(this.nodes[23]);
		
		//Segment 16
		this.segments[16].addNode(this.nodes[16]);
		this.segments[16].addNode(this.nodes[19]);
		this.segments[16].addNode(this.nodes[22]);
		
		//Segment 17
		this.segments[17].addNode(this.nodes[15]);
		this.segments[17].addNode(this.nodes[18]);
		this.segments[17].addNode(this.nodes[21]);
		
		//Segment 18
		this.segments[18].addNode(this.nodes[9]);
		this.segments[18].addNode(this.nodes[10]);
		this.segments[18].addNode(this.nodes[11]);
		
		//Segment 19
		this.segments[19].addNode(this.nodes[0]);
		this.segments[19].addNode(this.nodes[3]);
		this.segments[19].addNode(this.nodes[6]);
	}
	
	private void addSegmentsToNodes() {
		// Node 0
		this.nodes[0].addSegment(this.segments[0]);
		this.nodes[0].addSegment(this.segments[6]);
		this.nodes[0].addSegment(this.segments[19]);
		
		// Node 1
		this.nodes[1].addSegment(this.segments[0]);
		this.nodes[1].addSegment(this.segments[12]);
		
		// Node 2
		this.nodes[2].addSegment(this.segments[0]);
		this.nodes[2].addSegment(this.segments[11]);
		this.nodes[2].addSegment(this.segments[13]);
		
		// Node 3
		this.nodes[3].addSegment(this.segments[1]);
		this.nodes[3].addSegment(this.segments[7]);
		this.nodes[3].addSegment(this.segments[19]);
		
		// Node 4
		this.nodes[4].addSegment(this.segments[1]);
		this.nodes[4].addSegment(this.segments[12]);
		
		// Node 5
		this.nodes[5].addSegment(this.segments[1]);
		this.nodes[5].addSegment(this.segments[10]);
		this.nodes[5].addSegment(this.segments[13]);
		
		// Node 6
		this.nodes[6].addSegment(this.segments[2]);
		this.nodes[6].addSegment(this.segments[8]);
		this.nodes[6].addSegment(this.segments[19]);
		
		// Node 7
		this.nodes[7].addSegment(this.segments[2]);
		this.nodes[7].addSegment(this.segments[12]);
		
		// Node 8
		this.nodes[8].addSegment(this.segments[2]);
		this.nodes[8].addSegment(this.segments[9]);
		this.nodes[8].addSegment(this.segments[13]);
		
		// Node 9
		this.nodes[9].addSegment(this.segments[6]);
		this.nodes[9].addSegment(this.segments[18]);
		
		// Node 10
		this.nodes[10].addSegment(this.segments[7]);
		this.nodes[10].addSegment(this.segments[18]);
		
		// Node 11
		this.nodes[11].addSegment(this.segments[8]);
		this.nodes[11].addSegment(this.segments[18]);
		
		// Node 12
		this.nodes[12].addSegment(this.segments[9]);
		this.nodes[12].addSegment(this.segments[14]);

		
		// Node 13
		this.nodes[13].addSegment(this.segments[10]);
		this.nodes[13].addSegment(this.segments[14]);
		
		// Node 14
		this.nodes[14].addSegment(this.segments[11]);
		this.nodes[14].addSegment(this.segments[14]);
		
		// Node 15
		this.nodes[15].addSegment(this.segments[3]);
		this.nodes[15].addSegment(this.segments[8]);
		this.nodes[15].addSegment(this.segments[17]);
		
		// Node 16
		this.nodes[16].addSegment(this.segments[3]);
		this.nodes[16].addSegment(this.segments[16]);
		
		// Node 17
		this.nodes[17].addSegment(this.segments[3]);
		this.nodes[17].addSegment(this.segments[9]);
		this.nodes[17].addSegment(this.segments[15]);
		
		// Node 18
		this.nodes[18].addSegment(this.segments[4]);
		this.nodes[18].addSegment(this.segments[7]);
		this.nodes[18].addSegment(this.segments[17]);
		
		// Node 19
		this.nodes[19].addSegment(this.segments[4]);
		this.nodes[19].addSegment(this.segments[16]);
		
		// Node 20
		this.nodes[20].addSegment(this.segments[4]);
		this.nodes[20].addSegment(this.segments[10]);
		this.nodes[20].addSegment(this.segments[15]);
		
		// Node 21
		this.nodes[21].addSegment(this.segments[5]);
		this.nodes[21].addSegment(this.segments[6]);
		this.nodes[21].addSegment(this.segments[17]);
		
		// Node 22
		this.nodes[22].addSegment(this.segments[5]);
		this.nodes[22].addSegment(this.segments[16]);
		
		// Node 23
		this.nodes[23].addSegment(this.segments[5]);
		this.nodes[23].addSegment(this.segments[11]);
		this.nodes[23].addSegment(this.segments[15]);
		
		
		
	}

}
