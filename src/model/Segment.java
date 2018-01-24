package model;

import player.Player;
import player.PlayerID;

public class Segment {
	
	public static final int NODES_PER_SEGMENT = 3;
	
	private Node[] nodes;
	private int id;

	public Segment(int id) {
		this.id = id;
		this.nodes = new Node[NODES_PER_SEGMENT];
	}
	
	public int getId() {
		return this.id;
	}
	
	public void addNode(Node n) {
		for(int i = 0; i < NODES_PER_SEGMENT; ++i)
			if( this.nodes[i] == null ) {
				this.nodes[i] = n;
				return;
			}
	}
	
	public void setEphemeral(boolean state) {
		for(int i = 0; i < NODES_PER_SEGMENT; ++i)
			this.nodes[i].setEphemeral(state);
	}
	
	public boolean isEphemeral() {
		for(int i = 0; i < NODES_PER_SEGMENT; ++i)
			if( !this.nodes[i].isEphemeral() )
				return false;
		return true;
	}
	
	public boolean isMill() {
		for(int i = 0; i < NODES_PER_SEGMENT; ++i)
			if( !this.nodes[i].isPossessed() )
				return false;
		for(int i = 0; i < NODES_PER_SEGMENT - 1; ++i)
			if( this.nodes[i].getPossessor().id() != this.nodes[i+1].getPossessor().id() )
				return false;
		return true;
	}
	
	public boolean isPreMillForPlayer(PlayerID pid) {
		int count = 0;
		for(int i = 0; i < NODES_PER_SEGMENT; ++i)
			if( this.nodes[i].isPossessed() && this.nodes[i].getPossessor().id() == pid )
				++count;
		return count == NODES_PER_SEGMENT - 1;
	}

	public Player getMiller() {
		if( !this.isMill() )
			return null;
		return this.nodes[0].getPossessor();
	}

	public Node[] getNodes() {
		return nodes;
	}

}
