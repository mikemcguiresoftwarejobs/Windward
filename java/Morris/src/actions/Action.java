package actions;

import player.PlayerID;
import model.Game;
import exceptions.IllegalPlayException;

public abstract class Action {
	
	protected PlayerID aid;
	
	public Action(PlayerID aid) {
		this.aid = aid;
	}
	
	public PlayerID agentID() {
		return this.aid;
	}
	
	public boolean execute(Game g) throws IllegalPlayException {
		if( !g.getActivePlayer().id().equals(this.aid) )
			throw new IllegalPlayException(this.aid, IllegalPlayException.Nature.PLAY_OUT_OF_TURN);
		return false;
	}
	
}
