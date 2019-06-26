package ChessBoard;

public class GameState {
	public ChessPieceColor playerTurn;
	//implement this later
	public int numberMoves;
	public GameStatus status;
	
	public GameState(ChessPieceColor playerTurn, int numberMoves, GameStatus status) {
		this.playerTurn = playerTurn;
		this.numberMoves = numberMoves;
		this.status = status;
	}
}
