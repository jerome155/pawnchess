package students.oeschjerome;

import java.util.ArrayList;

import ch.uzh.ifi.ddis.pai.chessim.game.Color;

public class Pawn {
	public Color color;
	public boolean isEnemy;
	public float score;
	public int x;
	public int y;
	public ArrayList<Movement> moves;
	public boolean transposed;
	private int diagonals;
	private int width;
	private int height;

	public Pawn(Color _color, int _x, int _y, boolean _transposed, int _width, int _height, boolean _isEnemy) {
		color = _color;
		x = _x;
		y = _y;
		score = 0;
		transposed = _transposed;
		moves = new ArrayList<Movement>();
		diagonals = 0;
		width = _width;
		height = _height;
		isEnemy = _isEnemy;
	}
	
	public Pawn(Pawn _oldPawn) {
		color = _oldPawn.color;
		x = _oldPawn.x;
		y = _oldPawn.y;
		score = _oldPawn.score;
		transposed = _oldPawn.transposed;
		diagonals = _oldPawn.diagonals;
		width = _oldPawn.width;
		height = _oldPawn.height;
		isEnemy = _oldPawn.isEnemy;
		moves = new ArrayList<Movement>();
	}
	
	public float calculateScore(Position[][] map) {
		//TODO: Finish: Split between enemy and own figure.
		score = 0;
		if (!isEnemy) {
			//Pawn exists
			score += 20;
			//Distance to other side
			score += (x^2);
			//Diagonals > 1
			score += diagonals * 20;
			//one step before victory
			if (x == height-2) {
				score += 100;
			}
			//can be eaten by others
			if (y > 0 && x < height-1) {
				if (map[x+1][y-1].getPawn()!= null) {
					if (map[x+1][y-1].getPawn().color != color) {
						score -= 100;
					}
				}
			}
			if (y + 1 <= width - 1 && x < height-1) {
				if (map[x+1][y+1].getPawn() != null) {
					if (map[x+1][y+1].getPawn().color != color) {
						score -= 100;
					}
				}
			}
		} else {
			//TODO: Change to enemy
			//Pawn exists
			score += 20;
			//Distance to other side
			score += ((height-x)^2);
			//Diagonals > 1
			score += diagonals * 20;
			//one step before victory
			if (x == 1) {
				score += 200;
			}
			//can be eaten by others
			if (y > 0 && x > 0) {
				if (map[x-1][y-1].getPawn()!= null) {
					if (map[x-1][y-1].getPawn().color != color) {
						score -= 100;
					}
				}
			}
			if (y + 1 <= width - 1 && x > 0) {
				if (map[x-1][y+1].getPawn() != null) {
					if (map[x-1][y+1].getPawn().color != color) {
						score -= 100;
					}
				}
			}
		}
		return score;
	}
	
	public void calculateMoves(Position[][] map) {
		//TODO: isEnemy: Evaluate from other side.
		if (isEnemy) {
			if (x > 0) {
				if (map[x-1][y].getPawn() == null) {
					moves.add(new Movement(new Position(x, y), new Position(x-1, y), transposed));
				}
			}
			
			if (y > 0 && x > 0) {
				if (map[x-1][y-1].getPawn() != null) {
					if (map[x-1][y-1].getPawn().color == color.getOtherColor()) {
						moves.add(new Movement(new Position(x, y), new Position(x-1, y-1), transposed));
						diagonals++;
					}
				}	
			}
			if (y < width-1 && x > 0) {
				if (map[x-1][y+1].getPawn() != null) {
					if (map[x-1][y+1].getPawn().color == color.getOtherColor()) {
						moves.add(new Movement(new Position(x, y), new Position(x-1, y+1), transposed));
						diagonals++;
					}
				}
			}
		} else {
			if (x < height-1) {
				if (map[x+1][y].getPawn() == null) {
					moves.add(new Movement(new Position(x, y), new Position(x+1, y), transposed));
				}
			}
			if (y > 0 && x < height-1) {
				if (map[x+1][y-1].getPawn() != null) {
					if (map[x+1][y-1].getPawn().color == color.getOtherColor()) {
						moves.add(new Movement(new Position(x, y), new Position(x+1, y-1), transposed));
						diagonals++;
					}
				}	
			}
			if (y < width-1 && x < height-1) {
				if (map[x+1][y+1].getPawn() != null) {
					if (map[x+1][y+1].getPawn().color == color.getOtherColor()) {
						moves.add(new Movement(new Position(x, y), new Position(x+1, y+1), transposed));
						diagonals++;
					}
				}
			}
		}
		
	}
}
