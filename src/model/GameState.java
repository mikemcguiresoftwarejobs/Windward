package model;

import player.Player;
import player.PlayerID;

public class GameState {
	
	// [ AP | C |  TIH | TOB | N23 | N22 | ... | N1 ]
	// 58   57  56    52    48    46    44     2    0
	// each node is two bits wide: [p|e]
	
	private static final int SIZE = 58;
	
	private static final int TOKEN_MASK = 0xF;	// mask for a token field
	private static final int NODE_MASK = 0x3;	// mask for a node field
	
	private static final int TOB_SHIFT = 48; // first bit of tokens on board; 
	private static final int TIH_SHIFT = 52; // first bit of tokens in hand;
	private static final int CAPTURE_SHIFT = 56; // bit which tells if this player can/must capture
	private static final int AP_SHIFT = 57;	// bit which tells whether this player is active
	
	private long p1, p2;
	
	public GameState(Game g) {
		
		long time = System.currentTimeMillis();
		
		this.p1 = 0L;
		this.p2 = 0L;
		
		Board b = g.getBoard();
		
		// set node data for each player
		Node n;
		for(long i = 0; i < Board.NUM_NODES; ++i) {
			n = b.getNode((int) i);
			if( n.isPossessed() ) {
				 if( n.getPossessor().id() == PlayerID.WHITE ) {
					 this.p1 |= (1L << ((i << 1) + 1));
					 if( n.isEphemeral() )
						 this.p1 |= (1L << (i << 1));
				 }
				 else {
					 this.p2 |= (1L << ((i << 1) + 1));
					 if( n.isEphemeral() )
						 this.p2 |= (1L << (i << 1));
				 }
			}
		}
		
		// Set player data: tokens on board, tokens in hand, can/must capture
		Player play = g.getPlayer(PlayerID.WHITE);
		this.p1 |= ((long) play.tokensOnBoard() << TOB_SHIFT);
		this.p1 |= ((long) play.tokensInHand() << TIH_SHIFT);
		
		if( play.canSlashMustCapture() )
			this.p1 |= (1L << CAPTURE_SHIFT );
		
		play = g.getPlayer(PlayerID.BLACK);
		this.p2 |= ((long) play.tokensOnBoard() << TOB_SHIFT);
		this.p2 |= ((long) play.tokensInHand() << TIH_SHIFT);
		
		if( play.canSlashMustCapture() )
			this.p2 |= (1L << CAPTURE_SHIFT );
		
		// set active player for game
		if( g.getActivePlayer().id() == play.id() ) // Black's turn to move
			this.p2 |= (1L << AP_SHIFT);
		else
			this.p1 |= (1L << AP_SHIFT);
//		
//		System.out.println(playerInfo(1));
//		System.out.println(playerInfo(2));
//		System.out.println("Time to make: " + (System.currentTimeMillis() - time));
	}
	
	public void setGame(Game g) {
		
		long time = System.currentTimeMillis();
		
		Board b = g.getBoard();
		Player play1 = g.getPlayer(PlayerID.WHITE);
		Player play2 = g.getPlayer(PlayerID.BLACK);
		
		Node n;
		int info;
		for(int i = 0; i < Board.NUM_NODES; ++i) {
			n = b.getNode(i);
			
			info = (int) ((this.p1 >> (i << 1)) & NODE_MASK);
			if( info > 0 ) { // we are the possesor
				n.setPossessor(play1);
				n.setEphemeral(info % 2 == 1);
			}
			
			else { // check for player2
				info = (int) ((this.p2 >> (i << 1)) & NODE_MASK);
				if( info > 0 ) { // we are the possesor
					n.setPossessor(play2);
					n.setEphemeral(info % 2 == 1);
				}
				else {
					n.setPossessor(null);
					n.setEphemeral(false);
				}
			}
		}
		
		play1.setTokensOnBoard((int) ((this.p1 >> TOB_SHIFT) & TOKEN_MASK));
		play1.setTokensInHand((int) ((this.p1 >> TIH_SHIFT) & TOKEN_MASK));
		
		play2.setTokensOnBoard((int) ((this.p2 >> TOB_SHIFT) & TOKEN_MASK));
		play2.setTokensInHand((int) ((this.p2 >> TIH_SHIFT) & TOKEN_MASK));
		
		if( ((this.p1 >> AP_SHIFT) % 2) == 1L ) // White is active
			g.setActivePlayer(PlayerID.WHITE);
		else
			g.setActivePlayer(PlayerID.BLACK);
		
		play1.setCanSlashMustCapture( ((this.p1 >> CAPTURE_SHIFT) % 2) == 1L );
		play2.setCanSlashMustCapture( ((this.p2 >> CAPTURE_SHIFT) % 2) == 1L );
		
		
		if( g.countObservers() > 0 ) {
			b.update();
			play1.update();
			play2.update();
			g.update();
		}
		
//		System.out.println(playerInfo(1));
//		System.out.println(playerInfo(2));
//		System.out.println("Time to set: " + (System.currentTimeMillis() - time));
	}
	
	public String playerInfo(int pid) {
		long src = 0L;
		if( pid == 1 )
			src = this.p1;
		if( pid == 2 )
			src = this.p2;
		
		String ret = "";
		for(int i = 0; i < SIZE; ++i ) {
			if( i < 48 ) {
				if( i % 2 == 0 )
					ret = " " + ret;
				if( i % 8 == 0 )
					ret = "  " + ret;
			}
			if( i == 48 || i == 52 || i >= 56 )
				ret = "  " + ret;
			ret = ((src >> i) % 2) + ret;
		}
		return ret;
	}
	
	public int compareTo(GameState gs) {
		return (int) ((this.p1 - gs.p1) + (this.p2 - gs.p2));
	}

}
