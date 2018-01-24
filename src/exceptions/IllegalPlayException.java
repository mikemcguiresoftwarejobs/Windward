package exceptions;

import player.PlayerID;

public class IllegalPlayException extends RuntimeException {
	
	public enum Nature {
		PLAY_OUT_OF_TURN,
		EMPTY_HANDED_PLACE,
		UNEMPTY_HANDED_MOVE,
		ILLEGAL_MOVE_ATTEMPT,
		ILLEGAL_UBER_MOVE_ATTEMPT,
		ILLEGAL_CAPTURE_ATTEMPT,
		MILLSTONE_CAPTURE_ATTEMPT,
		UNEPHEMERAL_NODE_REMOVE_ATTEMPT,
		MUST_CAPTURE_IGNORE,
		PLACE_TO_OCCUPIED_NODE,
		REMOVE_FROM_UNOCCUPIED_NODE,
		MOVE_FROM_UNOCCUPIED_NODE,
		MOVE_TO_OCCUPIED_NODE;
	}
	
	private PlayerID agentID;
	private Nature reason;
	
	public IllegalPlayException() {}
	
	public IllegalPlayException(PlayerID aid, Nature reason) {
		this.agentID = aid;
		this.reason = reason;
	}
	
	public Nature reason() {
		return this.reason;
	}
	
	public PlayerID agentID() {
		return this.agentID;
	}

}
