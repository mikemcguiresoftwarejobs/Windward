package evaluation;

import model.Board;
import model.Game;
import model.Segment;
import player.Player;
import player.PlayerID;

public class Material_Mill_Evaluator implements Evaluator {
	
	private double mat_wight, mill_weight;
	
	public Material_Mill_Evaluator(double materialWeight, double millWeight) {
		this.mat_wight = materialWeight;
		this.mill_weight = millWeight;
	}

	public double evaluate(Game g) {
	
		if( g.isDraw() )
			return 0.0;
		
		Player p1 = g.getPlayer(PlayerID.WHITE);
		int t1 = p1.tokensPossessed();
		if( t1 <= 2 )
			return Double.NEGATIVE_INFINITY;
		Player p2 = g.getPlayer(PlayerID.BLACK);
		int t2 = p2.tokensPossessed();
		if( t2 <= 2 )
			return Double.NEGATIVE_INFINITY;
		
		double eval = 0.0;
		
		eval += this.mat_wight*(t1 - t2);
		
		Segment s;
		for(int i = 0; i < Board.NUM_SEGMENTS; ++i) {
			s = g.getBoard().getSegment(i);
			if( s.isMill() ) {
				if( s.getMiller() == p1 )
					eval += this.mill_weight;
				else
					eval -= this.mill_weight;
			}
		}
		
		return eval;
	}

}
