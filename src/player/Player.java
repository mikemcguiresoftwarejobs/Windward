package player;

import java.util.Observable;

import actions.Play;

import model.Board;
import model.Game;

public abstract class Player extends Observable {
	
	public static final int MAX_TOKENS = Board.NUM_NODES / 2;
	
	private String name;
	protected PlayerID id;
	protected int tokensInHand, tokensOnBoard;
	protected boolean canSlashMustCapture;
	
	public Player(String name, PlayerID id) {
		this.name = name;
		this.id = id;
		
		this.tokensInHand = MAX_TOKENS;
		this.tokensOnBoard = 0;
		
		this.canSlashMustCapture = false;
	}
	
	public void update() {
		this.setChanged();
		this.notifyObservers();
	}
	
	public String name() {
		return this.name;
	}
	
	public PlayerID id() {
		return this.id;
	}
	
	public int tokensInHand() {
		return this.tokensInHand;
	}
	
	// Only call this from GameState or else behaviour is undefined
	public void setTokensInHand(int numTokens) {
		this.tokensInHand = numTokens;
	}
	
	public int tokensOnBoard() {
		return this.tokensOnBoard;
	}
	
	// Only call this from GameState or else behaviour is undefined
	public void setTokensOnBoard(int numTokens) {
		this.tokensOnBoard = numTokens;
	}
	
	public int tokensPossessed() {
		return (this.tokensInHand + this.tokensOnBoard);
	}
	
	public void placeToken() {
		--this.tokensInHand;
		++this.tokensOnBoard;
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public void retrieveToken() {
		++this.tokensInHand;
		--this.tokensOnBoard;
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public void loseToken() {
		--this.tokensOnBoard;
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public void setCanSlashMustCapture(boolean state) {
		this.canSlashMustCapture = state;
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public boolean canSlashMustCapture() {
		return this.canSlashMustCapture;
	}
	
	public void reset() {
		this.tokensInHand = MAX_TOKENS;
		this.tokensOnBoard = 0;
		this.canSlashMustCapture = false;
	}
	
	public abstract Play choosePlay(Game g);
}
