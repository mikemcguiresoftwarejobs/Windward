package model;

import java.util.ArrayList;
import java.util.List;

import player.Player;
import player.PlayerID;
import exceptions.NodePossessedException;
import exceptions.NodeUnpossessedException;

public class Node {
	
	private ArrayList<Node> neighbors;
	private ArrayList<Segment> segments;
	private Player possessor;
	private int id;
	private boolean isEphemeral;
	
	public Node(int id) {
		this.id = id;
		this.neighbors = new ArrayList<Node>();
		this.segments = new ArrayList<Segment>();
		this.possessor = null;
		this.isEphemeral = false;
	}
	
	public int getId() {
		return this.id;
	}
	
	public boolean isPossessed() {
		return this.possessor != null;
	}
	
	public Player getPossessor() throws NodeUnpossessedException {
		if( !this.isPossessed() )
			throw new NodeUnpossessedException();
		return this.possessor;
	}
	
	public List<Node> getNeighbors() {
		return this.neighbors;
	}
	
	public List<Segment> getSegments() {
		return this.segments;
	}
	
	public boolean isNeighbor(Node n) {
		return this.neighbors.contains(n);
	}
	
	public boolean isInSegment(Segment s) {
		return this.segments.contains(s);
	}
	
	public boolean isInMill() {
		for(Segment s : this.segments)
			if( s.isMill() )
				return true;
		return false;
	}
	
	public boolean isInPreMillForPlayer(PlayerID pid) {
		for(Segment s : this.segments)
			if( s.isPreMillForPlayer(pid) )
				return true;
		return false;
	}
	
	public void addNeigbor(Node n) {
		this.neighbors.add(n);
	}
	
	public void addSegment(Segment s) {
		this.segments.add(s);
	}
	
	public Player surrenderOccupance() throws NodeUnpossessedException {
		if( !this.isPossessed() )
			throw new NodeUnpossessedException();
		
		Player ret = this.possessor;
		this.possessor = null;
		this.isEphemeral = false;
		return ret;
	}
	
	public void acceptOccupance(Player p) throws NodePossessedException {
		if( this.isPossessed() )
			throw new NodePossessedException();
		
		this.possessor = p;
	}
	
	public void clearOccupance() {
		this.possessor = null;
	}
	
	public void setPossessor(Player p) {
		this.possessor = p;
	}
	
	public void setEphemeral(boolean state) {
		this.isEphemeral = state;
	}
	
	public boolean isEphemeral() {
		return this.isEphemeral;
	}

}
