/**
 * This file is part of the chess assignment of the 
 * "Practical Artificial Intelligence" class at University of Zurich.
 *
 * @copyright:
 *	 Dynamic and Distributed Information Systems Group
 * 	 Departement of Informatics, University of Zurich
 * @author:
 *   Michael Weiss, mail@mweiss.ch
 * @year: 
 *   2016
 */
package ch.uzh.ifi.ddis.pai.chessim;

import java.util.Date;

import students.OeschJerome;
import ch.uzh.ifi.ddis.pai.chessim.display.ConsoleDisplay;
import ch.uzh.ifi.ddis.pai.chessim.dummyagents.RandomAgent;
import ch.uzh.ifi.ddis.pai.chessim.game.Game;
import ch.uzh.ifi.ddis.pai.chessim.game.Game.GameResult;
import ch.uzh.ifi.ddis.pai.chessim.game.randomMover.RandomMoverPawnGame;

public class GameStarter {

	/**
	 * You may use this method to test your agent
	 * Feel free to modify the paramenters.
	 * @param args
	 */
	public static void main(String[] args){
		//To have a look at a game you can run the RandomAgent against itself by uncommenting the following line
		//Game game = new RandomMoverPawnGame(20000,1000, new RandomAgent(), new RandomAgent(), 0.1, (new Date()).getTime());
		Game game = new RandomMoverPawnGame(20000,1000, new OeschJerome(),new OeschJerome(), 0.1, (new Date()).getTime());
		game.registerDisplay(new ConsoleDisplay());
		GameResult result = game.playGame();
	}
}
