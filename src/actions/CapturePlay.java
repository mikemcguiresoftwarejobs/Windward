package actions;

import model.Game;

public class CapturePlay extends Play {
	
	private Action capture;
	
	public CapturePlay(Action move, Action capture) {
		super(move);
		this.capture = capture;
	}
	
	@Override
	public boolean execute(Game g) {
		boolean again = this.action.execute(g);
		if( again )
			again = this.capture.execute(g);
		return again;
	}

}
