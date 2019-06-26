package ChessBoard;

public class ChessPiece {
	
	private ChessPieceType type;
	private ChessPieceColor color;
	private ChessCoordinates coord;
	
	public ChessPiece(ChessPieceType type, ChessPieceColor color, ChessCoordinates coord) {
		this.type = type;
		this.color = color;
		this.coord = coord;
	}
	public ChessPieceType getType() {
        return type;
    }
    public ChessPieceColor getColor() {
        return color;
    }
    public ChessCoordinates getCoord() {
        return coord;
    }
    
}
