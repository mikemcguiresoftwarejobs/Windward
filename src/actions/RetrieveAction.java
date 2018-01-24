package actions;

import player.Player;
import player.PlayerID;
import model.Board;
import model.Game;
import exceptions.IllegalPlayException;

public class RetrieveAction extends Action {
	
	private int source;
	
	public RetrieveAction(PlayerID agentID, int nodeID) {
		super(agentID);
		this.source = nodeID;
	}
	
	public int getSource() {
		return this.source;
	}
	
	@Override
	public boolean execute(Game g) throws IllegalPlayException {
		super.execute(g);
		
		Player agent = g.getPlayer(this.aid);
		
		if( agent.canSlashMustCapture() && g.playerIsCapturable(g.other(this.aid)) )
			throw new IllegalPlayException(this.aid, IllegalPlayException.Nature.MUST_CAPTURE_IGNORE);
		
		g.getBoard().remove(agent, this.source);
		agent.retrieveToken();
		
		return false;
	}

}
