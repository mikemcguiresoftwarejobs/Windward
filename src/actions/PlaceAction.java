package actions;

import model.Board;
import model.Game;
import player.Player;
import player.PlayerID;
import exceptions.IllegalPlayException;

public class PlaceAction extends Action {
	
	private int destination;
	
	public PlaceAction(PlayerID agentID, int nodeID) {
		super(agentID);
		this.destination = nodeID;
	}
	
	public int getDestination() {
		return this.destination;
	}
	
	@Override
	public boolean execute(Game g) throws IllegalPlayException {
		super.execute(g);
		
		Player agent = g.getPlayer(this.aid);
		boolean opponentIsCapturable = g.playerIsCapturable(g.other(this.aid));
		
		if( agent.canSlashMustCapture() && opponentIsCapturable)
			throw new IllegalPlayException(this.aid, IllegalPlayException.Nature.MUST_CAPTURE_IGNORE);
		
		if( agent.tokensInHand() == 0 )
			throw new IllegalPlayException(this.aid, IllegalPlayException.Nature.EMPTY_HANDED_PLACE);
		
		Board b = g.getBoard();
		b.place(agent, this.destination);
		agent.placeToken();
		
		if( b.getNode(this.destination).isInMill() && opponentIsCapturable ) {
			agent.setCanSlashMustCapture(true);
			return true;
		}
		
		return false;
	}

}
