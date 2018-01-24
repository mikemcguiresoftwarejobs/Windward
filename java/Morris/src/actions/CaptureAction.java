package actions;

import model.Game;
import player.Player;
import player.PlayerID;
import exceptions.IllegalPlayException;

public class CaptureAction extends Action {

	private int source;

	public CaptureAction(PlayerID aid, int nodeID) {
		super(aid);
		this.source = nodeID;
	}

	public int getSource() {
		return this.source;
	}
	
	@Override
	public boolean execute(Game g) throws IllegalPlayException {
		super.execute(g);
		
		Player agent = g.getPlayer(this.aid);
		
		if( !agent.canSlashMustCapture() )
			throw new IllegalPlayException(this.aid, IllegalPlayException.Nature.ILLEGAL_CAPTURE_ATTEMPT);
		
		g.getBoard().remove(agent, this.source);
		g.getPlayer(g.other(this.aid).id()).loseToken();
		agent.setCanSlashMustCapture(false);
		
		return false;
	}

}
