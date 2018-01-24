package player;

import java.util.List;

import model.Game;
import actions.Action;
import actions.Play;

public class RandomPlayer extends ComputerPlayer {

	public RandomPlayer(String name, PlayerID id) {
		super(name, id, null);
	}

	@Override
	public Play choosePlay(Game g) {
		List<Play> plays = g.getPlays(this);
		//System.out.println(plays.size());
		if( plays.size() == 0 )
			return null;
		return plays.get((int)(Math.random() * plays.size()));
	}

}
