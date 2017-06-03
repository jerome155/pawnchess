package students.oeschjerome;

public class Movement {
	public Position from;
	public Position to;
	public boolean transposed;
	public float resultingScore;
	public Movement previousMove;
	
	public Movement(Position _from, Position _to, boolean _transposed) {
		from = _from;
		to = _to;
		transposed = _transposed;
		resultingScore = 0.f;
	}
	
	public Movement(Movement move){
		from = move.from;
		to = move.to;
		transposed = move.transposed;
		resultingScore = move.resultingScore;
	}
}
