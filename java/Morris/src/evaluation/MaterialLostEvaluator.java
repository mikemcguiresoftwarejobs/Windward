package evaluation;

import model.Game;
import player.Player;
import player.PlayerID;

public class MaterialLostEvaluator implements Evaluator {

	public double evaluate(Game g) {
		
		if( g.isDraw() )
			return 0.0;
		
		int t1 = g.getPlayer(PlayerID.WHITE).tokensPossessed();
		if( t1 == 2 )
			return Double.NEGATIVE_INFINITY;
		
		int t2 = g.getPlayer(PlayerID.BLACK).tokensPossessed();
		if( t2 == 2 )
			return Double.POSITIVE_INFINITY;
		
		return (Player.MAX_TOKENS - t2) - (Player.MAX_TOKENS - t1);
	}

}
