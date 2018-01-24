package view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import player.Player;

import model.Board;

public class PlayerView extends JPanel implements Observer {
	
	private Player player;
	private Orientation orientation;
	
	public PlayerView(Player p) {
		this.player = p;
		this.player.addObserver(this);
		
		this.setBackground(BoardView.BACKGROUND_COLOR);
	}
	
	public PlayerView(Player p, Orientation o) {
		this.player = p;
		this.orientation = o;
		
		switch(o) {
		case HORIZONTAL:
			this.setPreferredSize(new Dimension(50*Player.MAX_TOKENS, 50));
			break;
		case VERTICAL:
			this.setPreferredSize(new Dimension(50, 50*Player.MAX_TOKENS));
			break;
		}
		
		this.player.addObserver(this);
		this.setBackground(BoardView.BACKGROUND_COLOR);
	}
	
	public void setOrientation(Orientation o) {
		this.orientation = o;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		this.paintTokens(g2);
	}
	
	private void paintTokens(Graphics2D g2) {

		int numTokens = this.player.tokensInHand()
				+ this.player.tokensOnBoard();

		switch (this.orientation) {
		case HORIZONTAL: {
			int widthRegion = this.getWidth() / Player.MAX_TOKENS;
			int widthGutter = widthRegion / 12;

			int heightGutter = this.getHeight() / 12;

			int circleWidth = 5 * widthRegion / 6;
			int circleHeight = 5 * this.getHeight() / 6;

			Composite old = g2.getComposite();
			g2.setColor(player.id().getColor());
			for (int i = 0; i < numTokens; ++i) {
				if (i == this.player.tokensInHand())
					g2.setComposite(AlphaComposite.getInstance(
							AlphaComposite.SRC_OUT, 10.0f));
				g2.fillOval(i * widthRegion + widthGutter, heightGutter,
						circleWidth, circleHeight);
			}
			
			g2.setComposite(old);
			g2.setColor(Color.BLACK);
			for (int i = 0; i < Player.MAX_TOKENS; ++i)
				g2.drawOval(i * widthRegion + widthGutter, heightGutter,
						circleWidth, circleHeight);
			break;
		}
		case VERTICAL:
			int widthGutter = this.getWidth() / 12;

			int heightRegion = this.getHeight() / Player.MAX_TOKENS;
			int heightGutter = heightRegion / 12;

			int circleWidth = 5 * this.getWidth() / 6;
			int circleHeight = 5 * heightRegion / 6;

			Composite old = g2.getComposite();
			g2.setColor(player.id().getColor());
			for (int i = 0; i < numTokens; ++i) {
				if (i == this.player.tokensInHand())
					g2.setComposite(AlphaComposite.getInstance(
							AlphaComposite.SRC_OVER, 0.5f));
				g2.fillOval(widthGutter, i * heightRegion + heightGutter,
						circleWidth, circleHeight);
			}
			
			g2.setComposite(old);
			g2.setColor(Color.BLACK);
			for (int i = 0; i < Player.MAX_TOKENS; ++i)
				g2.drawOval(widthGutter, i * heightRegion + heightGutter,
						circleWidth, circleHeight);
			break;
		}
	}

	public void update(Observable arg0, Object arg1) {
		this.repaint();
	}
	
	public enum Orientation {
		HORIZONTAL, VERTICAL;
	}

}
