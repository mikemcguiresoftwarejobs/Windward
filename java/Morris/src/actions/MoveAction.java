package actions;

import model.Board;
import model.Game;
import model.Node;
import player.Player;
import player.PlayerID;
import exceptions.IllegalPlayException;

public class MoveAction extends Action {
	
	private int source, destination;
	
	public MoveAction(PlayerID agentID, int source, int destination) {
		super(agentID);
		this.source = source;
		this.destination = destination;
	}
	
	public int getSource() {
		return this.source;
	}
	
	public int getDestination() {
		return this.destination;
	}

	@Override
	public boolean execute(Game g) throws IllegalPlayException {
		super.execute(g);
		
		Board b = g.getBoard();
		Node destNode = b.getNode(this.destination);
		Player agent = g.getPlayer(aid);
		boolean opponentIsCapturable = g.playerIsCapturable(g.other(this.aid));
		
		if( agent.canSlashMustCapture() && opponentIsCapturable )
			throw new IllegalPlayException(this.aid, IllegalPlayException.Nature.MUST_CAPTURE_IGNORE);
		
		if( agent.tokensInHand() > 0 )
			throw new IllegalPlayException(this.aid, IllegalPlayException.Nature.UNEMPTY_HANDED_MOVE);
		
		if( agent.tokensOnBoard() > 3 && !destNode.isNeighbor(b.getNode(this.source)) )
			throw new IllegalPlayException(this.aid, IllegalPlayException.Nature.ILLEGAL_UBER_MOVE_ATTEMPT);
		
		b.move(agent, this.source, this.destination);
		
		if( destNode.isInMill() && opponentIsCapturable ) {
			agent.setCanSlashMustCapture(true);
			return true;
		}
		
		return false;
	}

}
