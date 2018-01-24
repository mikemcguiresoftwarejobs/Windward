package test;

import model.Game;
import player.GameTreePlayer;
import player.Player;
import player.PlayerID;
import evaluation.MaterialEvaluator;
import evaluation.Material_Mill_Evaluator;

public class RunManyGames {
	
	public static void main(String[] args) {
		
		int games = 100;
		

		Player p1 = new GameTreePlayer("P1", PlayerID.WHITE, new MaterialEvaluator(), 4);
		Player p2 = new GameTreePlayer("P2", PlayerID.BLACK, new MaterialEvaluator(), 4);
		
		int p1Wins = 0;
		int draws = 0;
		
		long time, timesofar = 0;
		
		for(int i = 0; i < games; ++i) {
			
			p1.reset();
			p2.reset();

			PlayerID winnerID;
			time = System.currentTimeMillis();
			if( i % 2 == 0 )
				winnerID = new Game(p1, p2).run(PlayerID.WHITE);
			else
				winnerID = new Game(p1, p2).run(PlayerID.BLACK);
			timesofar += (int) (System.currentTimeMillis() - time);
			
			System.out.println(i + " " + winnerID);
			System.out.flush();
			timesofar += (int) (System.currentTimeMillis() - time);
			if( winnerID == null )
				++draws;
			else
				if( winnerID == PlayerID.WHITE )
					++p1Wins;
		}
		System.out.println(p1Wins + " to " + (games - draws - p1Wins) + ", " + draws + " draws");
		System.out.println("Average game time: " + (double) timesofar / games);
	}

}
