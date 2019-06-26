package ChessBoard;

public class ChessMove {
	
	private ChessPiece piece;
	private ChessCoordinates newCoord;
	
	public ChessMove(ChessCoordinates newCoord, ChessPiece piece) {
		this.piece = piece;
		this.newCoord = newCoord;
	}
	public ChessPiece getPiece() {
        return piece;
    }
  
    public ChessCoordinates getNewCoord() {
        return newCoord;
    }
}
