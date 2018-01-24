package test;

import javax.swing.JFrame;

import player.PlayerID;

import view.BoardView;

import model.Game;

public class RunBoardView extends JFrame {
	
	public static void main(String[] args) {
		new RunBoardView(new Game()).setVisible(true);
	}
	
	public RunBoardView(Game g) {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(new BoardView(g));
		this.setSize(800, 800);
		this.setVisible(true);
	}

}
