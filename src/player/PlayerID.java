package player;

import java.awt.Color;

public enum PlayerID {
	
	WHITE(new Color(204, 230, 255)),
	BLACK(new Color(0, 51, 51));
	
	private Color color;
	
	private PlayerID(Color c) {
		this.color = c;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public boolean equals(PlayerID other) {
		return this.name().equals(other.name());
	}

}
