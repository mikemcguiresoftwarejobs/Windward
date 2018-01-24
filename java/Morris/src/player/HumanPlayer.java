package player;

import model.Game;
import actions.Action;
import actions.Play;

public class HumanPlayer extends Player {
	
	private Action myAction;
	
	public HumanPlayer(String name, PlayerID id) {
		super(name, id);
		this.myAction = null;
	}

	@Override
	public Play choosePlay(Game g) {
		this.myAction = null;
		while(this.myAction == null)
		{
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}		
		return new Play(this.myAction);
	}
	
	public void setAction(Action a) {
		this.myAction = a;
	}

}
