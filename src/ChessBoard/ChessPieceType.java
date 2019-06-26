package ChessBoard;

public enum ChessPieceType {
	PAWN(0, 0), ROOK(0, 1), KNIGHT(1, 2), BISHOP(2, 3),  QUEEN(3, 4), KING(4, 5), DOG(0, 6), CAT(7,7);
	
	private final int startingColumn;
	private final int inventoryIndex;
	
	ChessPieceType(int startingColumn, int inventoryIndex) {
		this.startingColumn = startingColumn;
		this.inventoryIndex = inventoryIndex;
	}
	
	public int getStartingColumn() {
		return startingColumn;
	}
	public int getInventoryIndex() {
		return inventoryIndex;
	}
}
