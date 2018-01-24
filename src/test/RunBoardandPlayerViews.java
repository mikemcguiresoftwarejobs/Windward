package test;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import player.PlayerID;

import model.Game;
import view.PlayerView;

public class RunBoardandPlayerViews extends JFrame {

	public static void main(String[] args) {
		new RunBoardandPlayerViews(new Game());
	}
	
	public RunBoardandPlayerViews(Game g) {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		new RunBoardView(g);
		
		PlayerView p1View = new PlayerView(g.getPlayer(PlayerID.WHITE), PlayerView.Orientation.VERTICAL);
		PlayerView p2View = new PlayerView(g.getPlayer(PlayerID.BLACK), PlayerView.Orientation.VERTICAL);
		
		this.setLayout(new GridLayout(1, 2));
		this.setSize(2*p1View.getPreferredSize().width, p1View.getPreferredSize().height);
		
		this.add(p1View);
		this.add(p2View);
		
		this.setVisible(true);
		
		PlayerID winnerID = g.run(g.getActivePlayer().id());
		if( winnerID == null )
			JOptionPane.showMessageDialog(null, "Draw!");
		else
			JOptionPane.showMessageDialog(null, g.run(g.getActivePlayer().id()).name() + " wins!");
		System.exit(0);
	}
	
}
