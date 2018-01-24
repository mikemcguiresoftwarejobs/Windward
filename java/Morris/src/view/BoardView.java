package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Board;
import model.Game;
import model.Node;
import player.HumanPlayer;
import player.PlayerID;
import actions.Action;
import actions.CaptureAction;
import actions.MoveAction;
import actions.PlaceAction;
import actions.RetrieveAction;

public class BoardView extends JPanel implements Observer, MouseListener {
	
	public static final int CIRCLE_RADIUS_SCALE_FACTOR = 36;
	
	public static final Color SELECTED_COLOR = new Color(255,255,0); //new Color(122, 189, 255);
	public static final Color EPHEMERAL_COLOR = new Color(122, 189, 255);
	public static final Color BACKGROUND_COLOR = new Color(214,71,0);
	public static final Color LINE_COLOR = Color.BLACK;
	
	private static final int EPHEMERAL_THICKNESS = 3;
	
	private Game game;
	
	private Ellipse2D.Double[] positions;
	private Line2D.Double[] lines;
	
	private Node selectedNode;
	
	public BoardView(Game g) {
		this.game = g;
		g.addObserver(this);
		g.getBoard().addObserver(this);
		
		this.setSize(800, 800);
		this.setupLines();
		this.setupPositions();
		
		this.setBackground(new Color(214,71,0));
		
		this.selectedNode = null;
		this.addMouseListener(this);
	}
	
	private void setupLines() {
		this.lines = new Line2D.Double[Board.NUM_SEGMENTS];
		for(int i = 0; i < Board.NUM_SEGMENTS; ++i)
			this.lines[i] = new Line2D.Double();
	}
	
	private void setupPositions() {
		this.positions = new Ellipse2D.Double[Board.NUM_NODES];
		for(int i = 0; i < Board.NUM_NODES; ++i)
			positions[i] = new Ellipse2D.Double();
	}
	
	private void drawGrid(Graphics2D g2) {
		
		int gutterW = this.getWidth() / (CIRCLE_RADIUS_SCALE_FACTOR);
		int gutterH = this.getHeight() / (CIRCLE_RADIUS_SCALE_FACTOR);
		
		int width = this.getWidth()- 2*gutterW;
		int height = this.getHeight() - 2*gutterH;
		
		this.lines[0].setLine(gutterW, gutterH, gutterW + width, gutterH);
		this.lines[1].setLine(gutterW + width / 6, gutterH + height / 6,
				gutterW + 5 * width / 6, gutterH + height / 6);
		this.lines[2].setLine(gutterW + width / 3, gutterH + height / 3,
				gutterW + 2 * width / 3, gutterH + height / 3);
		
		this.lines[3].setLine(gutterW + width / 3, gutterH + 2 * height / 3,
				gutterW + 2 * width / 3, gutterH + 2 * height / 3);
		this.lines[4].setLine(gutterW + width / 6, gutterH + 5 * height / 6,
				gutterW + 5 * width / 6, gutterH + 5 * height / 6);
		this.lines[5].setLine(gutterW, gutterH + height, gutterW + width, gutterH + height);
		
		this.lines[6].setLine(gutterW, gutterH, gutterW, gutterH + height);
		this.lines[7].setLine(gutterW + width / 6, gutterH + height / 6,
				gutterW + width / 6, gutterH + 5 * height / 6);
		this.lines[8].setLine(gutterW + width / 3, gutterH + height / 3,
				gutterW + width / 3, gutterH + 2 * height / 3);
		
		this.lines[9].setLine(gutterW + 2 * width / 3, gutterH + height / 3,
				gutterW + 2 * width / 3, gutterH + 2 * height / 3);
		this.lines[10].setLine(gutterW + 5 * width / 6, gutterH + height / 6,
				gutterW + 5 * width / 6, gutterH + 5 * height / 6);
		this.lines[11].setLine(gutterW + width, gutterH,
				gutterW + width, gutterH + height);
		
		this.lines[12].setLine(gutterW+width / 2, gutterH, gutterW+width / 2, gutterH+height / 3);		
		this.lines[13].setLine(gutterW+width, gutterH, gutterW+2*width / 3, gutterH+height / 3);		
		this.lines[14].setLine(gutterW+width, gutterH+height / 2, gutterW+2*width / 3, gutterH+height / 2);
		this.lines[15].setLine(gutterW+width, gutterH+height, gutterW+2*width / 3, gutterH+2* height / 3);
		
		this.lines[16].setLine(gutterW+width / 2, gutterH+height, gutterW+width / 2, gutterH+2*height / 3);		
		this.lines[17].setLine(gutterW, gutterH+height, gutterW+width / 3, gutterH+2*height / 3);		
		this.lines[18].setLine(gutterW, gutterH+height / 2, gutterW+width / 3, gutterH+height / 2);		
		this.lines[19].setLine(gutterW, gutterH, gutterW+width / 3, gutterH+height / 3);					
		
		g2.setColor(LINE_COLOR);
		for (int i = 0; i < Board.NUM_SEGMENTS; ++i) {
			if (game.getBoard().getSegment(i).isEphemeral())
				drawEphemeralLine(g2, this.lines[i]);
			g2.draw(this.lines[i]);
		}
		
		g2.setColor(game.getActivePlayer().id().getColor());
		g2.fill(new Ellipse2D.Double(
				7*this.getWidth() / 16, 7*this.getHeight() / 16,
				this.getWidth() / 8, this.getHeight() / 8));
	}
	
	private static void drawEphemeralLine(Graphics2D g2, Line2D.Double line) {
		g2.setColor(EPHEMERAL_COLOR);
		Line2D.Double l = new Line2D.Double(line.x1, line.y1, line.x2, line.y2);
		if( l.x1 == l.x2 ) {// line is vertical
			for(int j = 0; j < EPHEMERAL_THICKNESS; ++j)
				g2.draw(translate(l, -1, 0));
			translate(l, EPHEMERAL_THICKNESS, 0);
			for(int j = 0; j < EPHEMERAL_THICKNESS; ++j )
			g2.draw(translate(l, 1, 0));
		}
		else {
			for(int j = 0; j < EPHEMERAL_THICKNESS; ++j)
				g2.draw(translate(l, 0, -1));
			translate(l, 0, EPHEMERAL_THICKNESS);
			for(int j = 0; j < EPHEMERAL_THICKNESS; ++j )
			g2.draw(translate(l, 0, 1));
		}
		g2.setColor(LINE_COLOR);
	}
	
	private static Line2D.Double translate(Line2D.Double l, int x, int y) {
		l.x1 += x;
		l.x2 += x;
		
		l.y1 += y;
		l.y2 += y;
		
		return l;
	}
	
	private void drawPositions(Graphics2D g2) {
		
		int gutterW = this.getWidth() / (CIRCLE_RADIUS_SCALE_FACTOR);
		int gutterH = this.getHeight() / (CIRCLE_RADIUS_SCALE_FACTOR);
		
		int width = this.getWidth()- 2*gutterW;
		int height = this.getHeight() - 2*gutterH;
		
		int circleWidth = 2*gutterW;
		int circleHeight = 2*gutterH;
		
		positions[0].setFrame(0, 0, circleWidth, circleHeight);
		positions[1].setFrame(width / 2, 0, circleWidth, circleHeight);
		positions[2].setFrame(width, 0, circleWidth, circleHeight);
		positions[3].setFrame(width / 6, height / 6, circleWidth, circleHeight);
		positions[4].setFrame(width / 2, height / 6, circleWidth, circleHeight);
		positions[5].setFrame(5* width / 6, height / 6, circleWidth, circleHeight);
		positions[6].setFrame(width / 3, height / 3, circleWidth, circleHeight);
		positions[7].setFrame(width / 2, height / 3, circleWidth, circleHeight);
		positions[8].setFrame(2*width / 3, height / 3, circleWidth, circleHeight);
		positions[9].setFrame(0, height / 2, circleWidth, circleHeight);
		positions[10].setFrame(width / 6, height / 2, circleWidth, circleHeight);
		positions[11].setFrame(width / 3, height / 2, circleWidth, circleHeight);
		positions[12].setFrame(2*width / 3, height / 2, circleWidth, circleHeight);
		positions[13].setFrame(5* width / 6, height / 2, circleWidth, circleHeight);
		positions[14].setFrame(width, height / 2, circleWidth, circleHeight);
		positions[15].setFrame(width / 3, 2* height / 3, circleWidth, circleHeight);
		positions[16].setFrame(width / 2, 2*height / 3, circleWidth, circleHeight);
		positions[17].setFrame(2*width / 3, 2*height / 3, circleWidth, circleHeight);
		positions[18].setFrame(width / 6, 5*height / 6, circleWidth, circleHeight);
		positions[19].setFrame(width / 2, 5*height / 6, circleWidth, circleHeight);
		positions[20].setFrame(5 * width / 6, 5 * height / 6, circleWidth, circleHeight);
		positions[21].setFrame(0, height, circleWidth, circleHeight);
		positions[22].setFrame(width / 2, height, circleWidth, circleHeight);
		positions[23].setFrame(width, height, circleWidth, circleHeight);
		
		for(int i = 0; i < Board.NUM_NODES; ++i) {
			Node curr = this.game.getBoard().getNode(i);
			
			if( curr == this.selectedNode ) {
				g2.setColor(SELECTED_COLOR);
				g2.fill(new Ellipse2D.Double(
						this.positions[i].x - 0.125*circleWidth,
						this.positions[i].y - 0.125*circleHeight,
						1.25*circleWidth, 1.25*circleHeight));
			}
			
			if( curr.isPossessed() )
				g2.setColor(curr.getPossessor().id().getColor());
			else
				g2.setColor(this.getBackground());
			g2.fill(positions[i]);
		}
		g2.setColor(Color.BLACK);
		for(int i = 0; i < Board.NUM_NODES; ++i)
			g2.draw(positions[i]);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		this.drawGrid(g2);
		this.drawPositions(g2);
	}

	public void update(Observable arg0, Object arg1) {
		this.repaint();
	}

	public void mouseClicked(MouseEvent arg0) {
		
		int x = arg0.getX();
		int y = arg0.getY();
		
		Node clicked = null;
		int clickCount = arg0.getClickCount();
		
		for(int i = 0; i < Board.NUM_NODES; ++i)
			if( this.positions[i].contains(x, y) ) {
				clicked = this.game.getBoard().getNode(i);
				break;
			}
		
		// we made no selection, or we mean to deselect by clicking the selected
		// so set selected to null
		if(clicked == null)
			this.selectedNode = null;
		
		
		// new convention:
		// left click:
		//	single: select
		//	double: place
		// right click:
		//  single: deselect, possibly remove
		else {

			Action a = null;
			PlayerID aid = game.getActivePlayer().id();

			if (arg0.getButton() == MouseEvent.BUTTON1) { // left click
					if( this.selectedNode == null ) {
						this.selectedNode = clicked;
					}
					else if (this.selectedNode.getId() == clicked.getId())
					{
						a = new PlaceAction(aid, clicked.getId());
						this.selectedNode = null;
					}
					else
					{
						a = new MoveAction(aid, this.selectedNode.getId(), clicked.getId());
						this.selectedNode = null;
					}
			}
			
			if (arg0.getButton() == MouseEvent.BUTTON3) { // right click
				if (clicked.isPossessed()) {
					if (clicked.getPossessor().id() == aid)
						a = new RetrieveAction(aid, clicked.getId());
					if (clicked.getPossessor().id() != aid)
						a = new CaptureAction(aid, clicked.getId());
				}
				this.selectedNode = null;
			}
			
			try {
				((HumanPlayer) this.game.getActivePlayer()).setAction(a);
			} catch (ClassCastException cce) {
				JOptionPane.showMessageDialog(null, "It's not your turn");
				this.selectedNode = null;
			}
		}
		
		this.repaint();
		
// else {
//			Action a = null;
//			
//			//if we are clicking an empty node to start the move, this is a place action
//			if( !clicked.isPossessed() && this.selectedNode == null )
//				a = new PlaceAction(game.getActivePlayer(), clicked.getId());
//			//if we are clicking the enemy's node to start the move, this is a capture action
//			else if( clicked.isPossessed() && clicked.getPossessor().id() != this.game.getActivePlayer().id() )
//				a = new CaptureAction(game.getActivePlayer(), clicked.getId());//I changed this to clicked because when the player is capturing, there is no selected Node.
//			
//			//if we are clicking our own node, what happens depends on which mouse button we use
//			switch( arg0.getButton() ) {
//			case MouseEvent.BUTTON1: // left click
//				//if we have not selected a node, we should be left clicking our own node to move it somewhere
//				if( this.selectedNode == null )
//					this.selectedNode = clicked;
//				//if we've already selected a node, we are left clicking an empty node to complete a move action
//				else
//					a = new MoveAction(game.getActivePlayer(), this.selectedNode.getId(), clicked.getId());
//				break;
//			case MouseEvent.BUTTON3: // right click
//				//if we are right clicking our own node, we are attempting to remove one of our nodes from one of our mills.
//				a = new RemoveAction(game.getActivePlayer(), clicked.getId());
//				break;
//			}
			

//			
//			this.selectedNode = null;
//		}
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
