package player;

import evaluation.Evaluator;

public abstract class ComputerPlayer extends Player {
	
	protected Evaluator e;

	public ComputerPlayer(String name, PlayerID id, Evaluator e) {
		super(name, id);
		this.e = e;
	}
	
	public Evaluator evaluator() {
		return this.e;
	}

}
