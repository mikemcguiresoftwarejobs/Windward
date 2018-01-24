package player;

import actions.Play;
import model.Game;

public class ComatosePlayer extends Player {

	public ComatosePlayer(PlayerID id) {
		super(id.name(), id);
	}

	@Override
	public Play choosePlay(Game g) {
		return null;
	}

}
