package evaluation;

import java.util.ArrayList;
import java.util.List;

import actions.Play;

import model.Game;
import model.GameState;
import player.Player;
import player.PlayerID;
import exceptions.IllegalPlayException;

public class GameTree {
	
	private Game game;
	private Evaluator eval;
	
	public GameTree(Game g, Evaluator e) {
		this.eval = e;
		this.game = g;
	}
	
	// the player has a move
	public Play bestPlay(int level) {
		GameState gs = new GameState(this.game);
		Player agent = this.game.getActivePlayer();
		PlayerID aid = agent.id();
		
		boolean maximize = aid == PlayerID.WHITE;
		double value;
		if( aid == PlayerID.WHITE )
			value = Double.NEGATIVE_INFINITY;
		else
			value = Double.POSITIVE_INFINITY;
		
		double result;
		List<Play> plays = new ArrayList<Play>();
		for(Play p : this.game.getPlays(agent)) {
			gs.setGame(this.game);
			try {
				if( this.game.executePlay(p) )
					System.out.println("Incomplete play in Game Tree");
			} catch(IllegalPlayException ipe) {
				System.out.println("Major error " + ipe.reason().name());
				ipe.printStackTrace();
			}
			
			result = bestPlay(new GameState(this.game), level - 1);
			
			if ((maximize && result > value) || (!maximize && result < value)) {
				plays.clear();
				value = result;
			}
			
			if( result == value )
				plays.add(p);
		}
		
		gs.setGame(this.game);
		
		return plays.get( (int)(Math.random() * plays.size()) );
	}
	
	private double bestPlay(GameState gs, int level) {
		
		gs.setGame(this.game);
		Player agent = this.game.getActivePlayer();
		PlayerID aid = agent.id();
		
		double eval = this.eval.evaluate(this.game);
		
		if (eval == Double.MIN_VALUE || eval == Double.MAX_VALUE || level == 0
				|| this.game.isDraw())
			return eval;
		
		boolean maximize = aid == PlayerID.WHITE;
		double value;
		if( maximize )
			value = Double.MIN_VALUE;
		else
			value = Double.MAX_VALUE;
		
		List<Play> plays = this.game.getPlays(agent);
		this.game.testPlays(plays);
		if( plays.size() == 0 ) {
			this.game.toggleActivePlayer();
			return bestPlay(new GameState(this.game), level - 1);
		}
		
		double result;
		for(Play p : plays) {
			gs.setGame(this.game);
			try {
				if( this.game.executePlay(p) )
					System.out.println("In GameTree: incomplete play");
				} catch(IllegalPlayException ipe) {
					System.out.println("Major error " + ipe.reason().name());
					ipe.printStackTrace();
				}
			
			result = bestPlay(new GameState(this.game), level - 1);
			if( maximize && result > value)
				value = result;
			if( !maximize && result < value )
				value = result;
		}
		
		return value;
	}

}
