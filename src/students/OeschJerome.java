package students;

import java.util.ArrayList;
import java.util.Iterator;

import ch.uzh.ifi.ddis.pai.chessim.game.Agent;
import ch.uzh.ifi.ddis.pai.chessim.game.Board;
import ch.uzh.ifi.ddis.pai.chessim.game.Color;
import ch.uzh.ifi.ddis.pai.chessim.game.Figure;
import ch.uzh.ifi.ddis.pai.chessim.game.History;
import ch.uzh.ifi.ddis.pai.chessim.game.Move;
import students.oeschjerome.Map;
import students.oeschjerome.Movement;

public class OeschJerome implements Agent{

	private Color myColor;
	private Map map;
	private int height;
	final int DEPTH = 2;
	final int WINNER = 10000;
	
	@Override
	public String developerAlias() {
		return "YawningPawning";
	}

	@Override
	public Move nextMove(Color yourColor, Board board, History history,
			long timeLimit) {
		
		myColor = yourColor;
		height = board.height;
		//System.out.println("My color: " + myColor);
		
		map = new Map(board.height, board.width, board, myColor);
		
		Movement winningMove = null;
		TreeSearch(map, 0, true);
				
		winningMove = map.bestMove;
		winningMove = map.transposeMove(winningMove);
		
		return idioticMoveConverter(board, myColor, winningMove);
		
	}
	
	public float TreeSearch(Map inputMap, int curDepth, boolean myTurn) {
		float highScore = 0.f;

		if (myTurn) {
			ArrayList<Movement> moves = inputMap.gatherTeamMoves(myTurn);
			if (curDepth > DEPTH) {
				//case last leaf reached --> stop expansion, return the best possible move for this leaf
				highScore = 0.f;
				for (int i = 0; i < moves.size(); i++) {
					Map tempMap = new Map(inputMap);
					tempMap.makeMove(moves.get(i));
					
					tempMap.scoreMap(myTurn, false);
					float tempScore = tempMap.score;
					if (tempScore >= highScore) {
						inputMap.bestMove = moves.get(i);
						highScore = tempScore;
					} 
				}
				return highScore;	
			} else {
				highScore = 0.f;
				//TODO: Continue expanding tree, unless gameover has been reached.
				for (int i = 0; i < moves.size(); i++) {
					Map tempMap = new Map(inputMap);
					tempMap.makeMove(moves.get(i));
					
					tempMap.scoreMap(myTurn, false);
					if (gameOver(tempMap, myTurn)) {
						inputMap.bestMove = moves.get(i);
						inputMap.myTurn = true;
						return WINNER;
					}
					float tempScore = TreeSearch(tempMap, curDepth+1, !myTurn);
					if (tempScore >= highScore) {
						inputMap.bestMove = moves.get(i);
						inputMap.myTurn = true;
						highScore = tempScore;
					}
				}
				return highScore;
			}
		} else {
			ArrayList<Movement> moves = inputMap.gatherTeamMoves(myTurn);
			highScore = WINNER;
			//TODO: Continue expanding tree. NOT MY TURN
			for (int i = 0; i < moves.size(); i++) {
				Map tempMap = new Map(inputMap);
				tempMap.makeMove(moves.get(i));
				tempMap.scoreMap(myTurn, false);
				
				if (gameOver(tempMap, myTurn)) {
					inputMap.bestMove = moves.get(i);
					inputMap.myTurn = false;
					return 0;
				}

				//maximizes to get lowest possible score of me
				float tempScore = TreeSearch(tempMap, curDepth+1, !myTurn);
				if (tempScore <= highScore) {
					inputMap.bestMove = moves.get(i);
					inputMap.myTurn = false;
					highScore = tempScore;
				}	
			}
			return highScore;
		}
	}
		
	private boolean gameOver(Map inputMap, boolean myTurn) {
		if (myTurn) {
			for (int i = 0; i < inputMap.getMyTeam().size(); i++) {
				if(inputMap.getMyTeam().get(i).x == height-1) {
					return true;
				}
			}
			if (inputMap.getOpposingTeam().size() == 0) {
				return true;
			}
			return false;
		} else {
			for (int i = 0; i < inputMap.getOpposingTeam().size(); i++) {
				if(inputMap.getOpposingTeam().get(i).x == 0) {
					return true;
				}
			}
			if(inputMap.getOpposingTeam().size() == 0) {
				return true;
			}
			return false;
		}
		
	}

	private Move idioticMoveConverter(Board _board, Color _myColor, Movement _move) {
		
		Iterator<Figure> figures = _board.figures(_myColor).values().iterator();
		ArrayList<Move> possibleMoves = new ArrayList<>();
		Move finalMove= null;
		while(figures.hasNext()){
			possibleMoves.addAll(figures.next().possibleMoves(_board).keySet());
		}
		boolean moveFound = false;
		for (int i = 0; i < possibleMoves.size(); i++) {
			if (possibleMoves.get(i).from.equals(_move.from.getCoordinates())) {
				if (possibleMoves.get(i).to.equals(_move.to.getCoordinates())) {
					finalMove = possibleMoves.get(i);
					moveFound =true;
				}
			}
		}
		if (!moveFound) {
			System.out.println("Comparing moves: Move not found.");
		}
		
		return finalMove;
	}

}
