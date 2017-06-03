package students.oeschjerome;

import java.util.ArrayList;
//import java.math;
import ch.uzh.ifi.ddis.pai.chessim.game.Board;
import ch.uzh.ifi.ddis.pai.chessim.game.Color;
import ch.uzh.ifi.ddis.pai.chessim.game.Coordinates;
import ch.uzh.ifi.ddis.pai.chessim.game.Figure;

public class Map {
	final float chance = 0.9f;
	private int height;
	private int width;
	private Color myColor;
	private Position[][] map;
	private ArrayList<Pawn> opposingTeam;
	private ArrayList<Pawn> myTeam;
	private ArrayList<Movement> gatheredTeamMoves;
	private boolean transposed;
	public float score;
	public Movement bestMove;
	//public Map parentMap;
	//public Map resultingMap;
	//public ArrayList<Map> childrenMap;
	public boolean myTurn;
	
	//First round, create everything
	public Map(int _height, int _width, Board board, Color _myColor) {
		height = _height;
		width = _width;
		map = new Position[height][width];
		myColor = _myColor;
		opposingTeam = new ArrayList<Pawn>();
		myTeam = new ArrayList<Pawn>();
		gatheredTeamMoves = new ArrayList<Movement>();
		transposed= false;
		score = 0;
		bestMove = null;
		//parentMap = null;
		//resultingMap = null;
		//childrenMap = new ArrayList<Map>();
		
		
		if (myColor == Color.WHITE) {
			transposed = false;
		} else {
			transposed = true;
		}
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Position pos;
				if (transposed) {
					pos = new Position(Math.abs(i-(height-1)),Math.abs(j-(width-1)),transposed);
				} else {
					pos = new Position(i,j,transposed);
				}
				
				if (board.figureAt(new Coordinates(i,j)) != null) {
					Figure tempFig = board.figures().get(new Coordinates(i,j));
					Pawn newPawn;
					if (transposed) {
						newPawn = new Pawn(tempFig.color,Math.abs(i-(height-1)),Math.abs(j-(width-1)),transposed, width, height, tempFig.color != myColor ? true: false);
					} else {
						newPawn = new Pawn(tempFig.color,i, j,transposed, width, height, tempFig.color != myColor ? true : false);
					}
					pos.setPawn(newPawn);
					if (tempFig.color == myColor) {
						myTeam.add(newPawn);
					} else {
						opposingTeam.add(newPawn);
					}
				}
				if (transposed) {
					map[Math.abs(i-(height-1))][Math.abs(j-(width-1))] = pos;
				} else {
					map[i][j] = pos;
				}
			}
		}
		for (int i = 0; i < myTeam.size(); i++) {
			myTeam.get(i).calculateMoves(map);
		}
		for (int i = 0; i < opposingTeam.size(); i++) {
			opposingTeam.get(i).calculateMoves(map);
		}	
	}
	
	public Map(Map _map) {
		height = _map.height;
		width = _map.width;
		map = new Position[height][width];
		myColor = _map.myColor;
		opposingTeam = new ArrayList<Pawn>();
		myTeam = new ArrayList<Pawn>();
		gatheredTeamMoves = new ArrayList<Movement>();
		transposed = _map.transposed;
		score = 0.f;
		bestMove = null;
		//parentMap = null;
		//resultingMap = null;
		//childrenMap = new ArrayList<Map>();
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				map[i][j] = new Position(_map.map[i][j]);
				if (map[i][j].getPawn()!= null) {
					if (map[i][j].getPawn().color != myColor) {
						opposingTeam.add(map[i][j].getPawn());
					} else {
						myTeam.add(map[i][j].getPawn());
					}
				}
			}
		}
//		for (int i = 0; i < myTeam.size(); i++) {
//			myTeam.get(i).calculateMoves(map);
//		}
//		for (int i = 0; i < opposingTeam.size(); i++) {
//			opposingTeam.get(i).calculateMoves(map);
//		}	
	}
	
	public ArrayList<Movement> gatherTeamMoves(boolean _myTurn) {
		gatheredTeamMoves = new ArrayList<Movement>();
		if (_myTurn) {
			for (int i = 0; i < myTeam.size(); i++) {
				for (int j = 0; j < myTeam.get(i).moves.size(); j++) {
					gatheredTeamMoves.add(myTeam.get(i).moves.get(j));
				}
			}
			return gatheredTeamMoves;
		} else {
			for (int i = 0; i < opposingTeam.size(); i++) {
				for (int j = 0; j < opposingTeam.get(i).moves.size(); j++) {
					gatheredTeamMoves.add(opposingTeam.get(i).moves.get(j));
				}
			}
			return gatheredTeamMoves;
		}
		
	}
	
	public Movement transposeMove(Movement move) {
		if(transposed) {
			move.from.x = Math.abs(move.from.x - (height-1));
			move.from.y = Math.abs(move.from.y - (width-1));
			move.to.x = Math.abs(move.to.x - (height-1));
			move.to.y = Math.abs(move.to.y - (width-1));
		}
		return move;	
	}
	
	public boolean makeMove(Movement _move) {	
		if (map[_move.to.x][_move.to.y] != null) {
			Pawn toMovePawn = map[_move.from.x][_move.from.y].getPawn();
			if (map[_move.to.x][_move.to.y].getPawn() != null) {
				Pawn toDeletePawn = map[_move.to.x][_move.to.y].getPawn();
				if (toDeletePawn.isEnemy) {
					opposingTeam.remove(toDeletePawn);
				} else {
					myTeam.remove(toDeletePawn);
				}
			}
			map[_move.from.x][_move.from.y].setPawn(null);
			map[_move.to.x][_move.to.y].setPawn(toMovePawn);
			toMovePawn.x = _move.to.x;
			toMovePawn.y = _move.to.y;
			toMovePawn.calculateMoves(map);
			toMovePawn.calculateScore(map);
			return true;
		} else {
			return false;
		}
	}
	
	public float scoreMap(boolean myTurn, boolean scoresKnown) {
		score = 0;
		if (myTurn) {
			score = chance * scoreMyMap(scoresKnown) - (1-chance) * scoreEnemyMap(scoresKnown);
		} else {
			score = chance * scoreEnemyMap(scoresKnown) - (1-chance) * scoreMyMap(scoresKnown);
		}
		return score;
	}
	
	private float scoreEnemyMap(boolean scoresKnown) {
		float enemyScore = 0.f;
		for (int i = 0; i < opposingTeam.size(); i++) {
			if (!scoresKnown) {
				opposingTeam.get(i).calculateMoves(map);
				opposingTeam.get(i).calculateScore(map);
			}
			enemyScore += opposingTeam.get(i).score;
		}
		return enemyScore;
	}
	
	private float scoreMyMap(boolean scoresKnown) {
		float myScore = 0.f;
		for (int i = 0; i < myTeam.size(); i++) {
			if (!scoresKnown) {
				myTeam.get(i).calculateMoves(map);
				myTeam.get(i).calculateScore(map);
			}
			myScore += myTeam.get(i).score;
		}
		return myScore;
	}
	
	public ArrayList<Pawn> getMyTeam() {
		return myTeam;
	}
	
	public ArrayList<Pawn> getOpposingTeam() {
		return opposingTeam;
	}
	
	public void printMap() {
		for (int i = height-1; i >= 0; i--) {
			for (int j = 0; j < width; j++) {
				System.out.print("|");
				if (map[i][j].getPawn() != null) {
					if (map[i][j].getPawn().color == Color.BLACK) {
						System.out.print("O");
					} else {
						System.out.print("X");
					}
				} else {
					System.out.print(" ");
				}
			}
			System.out.println("|");
		}
	}
	
}
