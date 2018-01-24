package actions;

import model.Game;
import player.PlayerID;

public class Play {
	
	protected Action action;
	
	public Play(Action action) {
		this.action = action;
	}
	
	public boolean execute(Game g) {
		return this.action.execute(g);
	}
	
	public PlayerID getAgentID() {
		return this.action.agentID();
	}

}
