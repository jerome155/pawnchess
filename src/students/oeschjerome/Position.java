package students.oeschjerome;

import ch.uzh.ifi.ddis.pai.chessim.game.Coordinates;

public class Position {
	public int x;
	public int y;
	public boolean transposed;
	private Pawn pawn;
	
	public Position(int _x, int _y, boolean _transposed) {
		x = _x;
		y = _y;
		transposed = _transposed;
		pawn = null;
	}
	
	public Position(int _x, int _y) {
		x = _x;
		y = _y;
		pawn = null;
	}
	
	public Position(Position pos) {
		x = pos.x;
		y = pos.y;
		transposed = pos.transposed;
		if (pos.pawn != null) {
			pawn = new Pawn(pos.pawn);
		} else {
			pawn = null;
		}

	}
	
	public void setPawn(Pawn _pawn) {
		pawn = _pawn;
	}
	
	public Pawn getPawn() {
		if (pawn != null) {
			return pawn;
		} else {
			return null;
		}
	}
	
	public Coordinates getCoordinates() {
		Coordinates cords = new Coordinates(x, y);
		return cords;
	}
}
