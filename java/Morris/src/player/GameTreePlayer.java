package player;

import model.Game;
import model.GameState;
import actions.Play;
import evaluation.Evaluator;
import evaluation.GameTree;

public class GameTreePlayer extends ComputerPlayer {
	
	private int depth;

	public GameTreePlayer(String name, PlayerID id, Evaluator e, int depth) {
		super(name, id, e);
		this.depth = depth;
	}

	@Override
	public Play choosePlay(Game g) {
		Game gPrime = new Game(new ComatosePlayer(PlayerID.WHITE), new ComatosePlayer(PlayerID.BLACK));
		new GameState(g).setGame(gPrime);
		
		return new GameTree(gPrime, this.e).bestPlay(depth);
	}

}
