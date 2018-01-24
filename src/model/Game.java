package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import javax.swing.Timer;

import player.GameTreePlayer;
import player.HumanPlayer;
import player.Player;
import player.PlayerID;
import actions.Action;
import actions.CaptureAction;
import actions.CapturePlay;
import actions.MoveAction;
import actions.PlaceAction;
import actions.Play;
import actions.RetrieveAction;
import evaluation.MaterialEvaluator;
import exceptions.IllegalPlayException;

public class Game extends Observable {
	
	private Board board;
	
	private Player p1, p2, activePlayer, inactivePlayer;
	
	public Game() {
		this.board = new Board();
		this.p1 = new HumanPlayer("P1", PlayerID.WHITE);
		this.p2 = new HumanPlayer("P2", PlayerID.BLACK);
		
		this.activePlayer = p1;
	}
	
	public Game(Player p1, Player p2) {
		this.board = new Board();
		this.p1 = p1;
		this.p2 = p2;
		
		this.activePlayer = p1;
	}
	
	public void update() {
		this.setChanged();
		this.notifyObservers();
	}
	
	public PlayerID run(PlayerID startPlayerID) {
		this.activePlayer = this.getPlayer(startPlayerID);
		this.inactivePlayer = this.other(startPlayerID);
		
		while(this.isRunning()) {
			try {
				//this.cycleActivePlays(100);
				
				while (true) {
					if( this.getPlays(this.activePlayer).size() == 0 )
						break;
					PlayerID agentID = this.activePlayer.id();
					Play toMake = this.activePlayer.choosePlay(this);
					if( this.activePlayer.id() != toMake.getAgentID() )
						System.out.println(agentID + " != " + this.activePlayer.id());
					if( !toMake.execute(this) )
						break;
				}
				this.toggleActivePlayer();
			} catch (IllegalPlayException ipe) {
				System.out.println(ipe.agentID() + " " + ipe.reason());
				ipe.printStackTrace();
			} catch(NullPointerException npe) {
				this.toggleActivePlayer();
			}
		}
		
		if( this.p1.tokensPossessed() == 2 )
			return this.p2.id();
		else if( this.p2.tokensPossessed() == 2 )
			return this.p1.id();
		else
			return null;
	}
	
	// returns true if another action is needed
	public boolean executePlay(Play p) {
		try {
			boolean again = p.execute(this);
			if( !again )
				this.toggleActivePlayer();
			else
				System.out.println("In Game: incomplete play");
			return again;
		} catch( IllegalPlayException ipe ) {
			System.out.println("In Game: " + ipe.reason().name());
			ipe.printStackTrace();
			return true;
		}
	}
	
	// this may need to have the additional condition that
	// both players have no moves because we might be
	// asking in the middle of a turn and in the case
	// when we have filled the board and are about to capture
	// we have not drawn.
	public boolean isDraw() {
		return this.p1.tokensOnBoard() == Player.MAX_TOKENS &&
			this.p2.tokensOnBoard() == Player.MAX_TOKENS;
	}
	
	public Player other(PlayerID pid) {
		if( this.p1.id() == pid )
			return this.p2;
		else return this.p1;
	}
	
	public boolean isRunning() {
		return !this.isDraw() && this.p1.tokensPossessed() > 2 && this.p2.tokensPossessed() > 2;
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	public Player getPlayer(PlayerID pid) {
		if( this.p1.id().equals(pid) )
			return this.p1;
		return this.p2;
	}
	
	public Player getActivePlayer() {
		return this.activePlayer;
	}
	
	public Player getInactivePlayer() {
		return this.inactivePlayer;
	}
	
	public void setActivePlayer(PlayerID pid) {
		this.activePlayer = this.getPlayer(pid);
		if( pid.equals(PlayerID.WHITE) )
			this.inactivePlayer = this.getPlayer(PlayerID.BLACK);
		else
			this.inactivePlayer = this.getPlayer(PlayerID.WHITE);
	}
	
	public void toggleActivePlayer() {
		this.activePlayer.setCanSlashMustCapture(false);
		
		Player temp = this.activePlayer;
		this.activePlayer = this.inactivePlayer;
		this.inactivePlayer = temp;
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public boolean playerIsCapturable(Player p) {
		for(int i = 0; i < Board.NUM_NODES; ++i) {
			Node temp = board.getNode(i);
			if( temp.isPossessed() && temp.getPossessor().id() == p.id() && !temp.isInMill() )
				return true;
		}
		return false;
	}
	
	private List<Integer> capturableNodeIDsForPlayer(PlayerID pid) {
		List<Integer> ret = new ArrayList<Integer>();
		
		PlayerID oid = this.other(pid).id();
		Node n;
		for(int i = 0; i < Board.NUM_NODES; ++i) {
			n = this.board.getNode(i);
			if( n.isPossessed() && n.getPossessor().id() == oid && !n.isInMill() )
				ret.add(i);
		}
		
		return ret;
	}
	
	// This giant method returns a list of all the plays that a
	// player has given the current configuration, regardless of turn
	public List<Play> getPlays(Player p) {
		ArrayList<Play> ret = new ArrayList<Play>();
		PlayerID pid = p.id();
		
		// first check if player must capture right now, if so make it so
		if( p.canSlashMustCapture() && playerIsCapturable(other(pid)) ) {
			System.out.println("Half capture");
			for( Integer i : this.capturableNodeIDsForPlayer(pid) )
				ret.add( new Play( new CaptureAction(pid, i) ) );
			return ret;
		}
		
		// PHASE I : Places allowed, no moves or ubermoves allowed
		if( p.tokensInHand() > 0 ) {
			Node n;
			// move over all the board
			for(int i = 0; i < Board.NUM_NODES; ++i) {
				n = this.board.getNode(i);
				// if a node is empty we can place
				Action place = new PlaceAction(pid, i);
				if (!n.isPossessed()) {
					// this will result in a capture
					if( n.isInPreMillForPlayer(pid) && playerIsCapturable(other(pid)) )
						for (Integer j : this.capturableNodeIDsForPlayer(pid))
							ret.add(new CapturePlay(
									place, new CaptureAction(pid, j)));
					// this won't
					else
						ret.add(new Play(place));
				}
			}
		}
		
		// PHASE II: Moves allowed, no ubermoves allowed, no places possible
		else if( p.tokensPossessed() > 3 ) {
			// move over all the nodes this player has
			for (Node n : this.board.getPossession(pid))
				// move over all the neighbors to each node
				for (Node k : n.getNeighbors())
					// if neighbor is empty we may move there
					if (!k.isPossessed()) {
						Action move = new MoveAction(pid, n.getId(), k.getId());
						// this will result in a capture
						if (k.isInPreMillForPlayer(pid) && playerIsCapturable(other(pid)) )
							for (Integer j : this.capturableNodeIDsForPlayer(pid))
								ret.add( new CapturePlay(
									move, new CaptureAction(pid, j) ) );
						// this won't
						else
								ret.add( new Play(move) );
					}
		}
		
		// PHASE III: Ubermoves allowed, no places possible
		else {
			List<Node> unpossessed = this.board.getUnpossessed();
			
			// move over all the nodes this player has
			for(Node n : this.board.getPossession(pid))
				//we may move to each unpossessed node
				for(Node k : unpossessed) {
					Action move = new MoveAction(pid, n.getId(), k.getId());
					// this will result in a capture
					if (k.isInPreMillForPlayer(pid) && playerIsCapturable(other(pid)) )
						for (Integer j : this.capturableNodeIDsForPlayer(pid))
							ret.add( new CapturePlay(
								move, new CaptureAction(pid, j) ) );
					// this won't
					else
							ret.add( new Play(move) );
				}
		}
		
		// IN ALL CASES, we can remove our own ephemeral nodes if any
		Node n;
		// move over all the nodes
		for(int i = 0; i < Board.NUM_NODES; ++i) {
			n = this.board.getNode(i);
			// if a node is ephemeral, it is possessed
			if( n.isEphemeral() && n.getPossessor().id() == pid )
				ret.add( new Play( new RetrieveAction(pid, n.getId()) ) );
		}
		
		return ret;
	}
	
	public boolean testPlays(List<Play> plays) {
		GameState current = new GameState(this);
		for(Play p : plays) {
			try {
				if( this.executePlay(p) ) {
					System.out.println("Incomplete play in testPlays");
					System.exit(1);
				}
			} catch(IllegalPlayException ipe) {
				System.out.println("Illegal Play in test Plays");
				System.exit(1);
			}
			current.setGame(this);
		}
		return true;
	}
	
	
	private Timer t;
	public void cycleActivePlays(int delayMS) {
		t = new Timer(delayMS, new CycleMove(this.getPlays(this.activePlayer).iterator()));
		t.start();
	}
	
	private class CycleMove implements ActionListener {
		
		private GameState gs;
		private Iterator<Play> itr;
		
		public CycleMove(Iterator<Play> itr) {
			this.gs = new GameState(Game.this);
			this.itr = itr;
		}

		public void actionPerformed(ActionEvent arg0) {
			this.gs.setGame(Game.this);
			if( itr.hasNext() )
				itr.next().execute(Game.this);
			else
				t.stop();
		}
		
	}
}
