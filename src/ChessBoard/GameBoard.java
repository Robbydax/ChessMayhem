package ChessBoard;

import java.lang.reflect.Array;
import java.util.ArrayList;

/*a lot of the design choices were taken from this model found on github as a means of guidance/starting direction
  https://github.com/ChessCorp/chess-rules-java/tree/master/src/main/java/org/alcibiade/chess/model
*/
public class GameBoard {

	public ChessPiece[][] grid  = new ChessPiece[8][8];
	public GameState gamestate;
	public int[] inventory; //inventory of number of pieces both players possess, 
	//the first 6 entries belonging to WHITE and the next 6 belong to BLACK, with explicit structure below
	//Inventory Structure = [Pawns, Rooks, Knight, Bishop, Queen, King, Pawns, Rooks, Knight, Bishop, Queen, King]  
	//naively check for general reachability
	public boolean pawnReachable(ChessCoordinates newCoord, ChessPiece piece) {
		ChessCoordinates oldCoord = piece.getCoord();
		if(oldCoord.getRow()==1 || oldCoord.getRow()==6) { 
			if(oldCoord.getRow()==6) 
				if((newCoord.getRow()>=6 || newCoord.getRow()<=3) || newCoord.getColumn()>oldCoord.getColumn()+1 || newCoord.getColumn()<oldCoord.getColumn()-1) 
					return false;
			else if(oldCoord.getRow()==1)
				if(newCoord.getRow()<=1 || newCoord.getRow()>=4 || newCoord.getColumn()>oldCoord.getColumn()+1 || newCoord.getColumn()<oldCoord.getColumn()-1) 
					return false;
		}
		else {
			if(piece.getColor()==ChessPieceColor.WHITE) 
				if(newCoord.getRow()<oldCoord.getRow()-1 || newCoord.getColumn()>oldCoord.getColumn()+1 || newCoord.getColumn()<oldCoord.getColumn()-1) 
					return false;
			else if(piece.getColor()==ChessPieceColor.BLACK)
				if(newCoord.getRow()>oldCoord.getRow()+1 || newCoord.getColumn()>oldCoord.getColumn()+1 || newCoord.getColumn()<oldCoord.getColumn()-1) 
					return false;
		}
		return true;
	}
	
	public boolean rookReachable(ChessCoordinates newCoord, ChessPiece piece) {
		ChessCoordinates oldCoord = piece.getCoord();
		return (newCoord.getRow()==oldCoord.getRow() || newCoord.getColumn()==oldCoord.getColumn());
	}
	
	//naively check for bishop reachability by iterating through all possible diagonal positions possible
	public boolean bishopReachable(ChessCoordinates newCoord, ChessPiece piece) {
		ChessCoordinates oldCoord = piece.getCoord();
		int offset = 1;
		//traverse diagonally downward 
		for(int row = oldCoord.getRow()+1; row < 8; row++) { 
			if(newCoord.getRow()==row && (newCoord.getColumn()==(oldCoord.getColumn()+offset) || newCoord.getColumn()==(oldCoord.getColumn()-offset)))
				return true;
			else
				offset+=1;
		}
		//traverse diagonally upward
		offset = 1;
		for(int row = oldCoord.getRow()-1; row >= 0; row--) { 
			if(newCoord.getRow()==row && (newCoord.getColumn()==(oldCoord.getColumn()+offset) || newCoord.getColumn()==(oldCoord.getColumn()-offset)))
				return true;
			else
				offset+=1;
		}
		return false;
	}
	
	public boolean knightReachable(ChessCoordinates newCoord, ChessPiece piece) {
		ChessCoordinates oldCoord = piece.getCoord();
		int x = oldCoord.getRow();
		int y = oldCoord.getColumn();
		int nx = newCoord.getRow();
		int ny = newCoord.getColumn();
		return ((nx==x+1 && (ny==y+2 || ny==y-2)) || (nx==x-1 && (ny==y+2 || ny==y-2)) || (ny==y+1 && (nx==x+2 || nx==x-2)) || (ny==y-1 && (nx==x+2 || nx==x-2)));
	}
	
	public boolean queenReachable(ChessCoordinates newCoord, ChessPiece piece) {
		return(rookReachable(newCoord, piece) || bishopReachable(newCoord, piece));
	}
	
	//check that king moves only one square away
	public boolean kingReachable(ChessCoordinates newCoord, ChessPiece piece) {
		ChessCoordinates oldCoord = piece.getCoord();
		return(oldCoord.getColumn()<=newCoord.getColumn()+1 && oldCoord.getRow()>=newCoord.getRow()-1 && oldCoord.getRow()<=newCoord.getRow()+1);
	}
	
	//custom dog piece. The dog can either move one space to the left, one space to the right, or two spaces forward
	public boolean dogReachable(ChessCoordinates newCoord, ChessPiece piece) {
		ChessCoordinates oldCoord = piece.getCoord();
		int x = oldCoord.getRow();
		int y = oldCoord.getColumn();
		int nx = newCoord.getRow();
		int ny = newCoord.getColumn();
		return((nx==x&&(ny==y+1||ny==y-1) || (ny==y&&(nx==x+1||nx==x-1))));
	}
	
	//custom trivial cat piece. able to move only diagonally 2 spaces, but it is unobstructed like a knight
	public boolean catReachable(ChessCoordinates newCoord, ChessPiece piece) {
		ChessCoordinates oldCoord = piece.getCoord();
		int x = oldCoord.getRow();
		int y = oldCoord.getColumn();
		int nx = newCoord.getRow();
		int ny = newCoord.getColumn();
		return((nx==x+2&&(ny==y+2 || ny==y-2)) || (nx==x-2&&(ny==y+2 || ny==y-2)));
	}
	
	//assign reachable function to corresponding piece
	public boolean reachable(ChessCoordinates newCoord, ChessPiece piece) {
		ChessPieceType type = piece.getType();
        if(type==ChessPieceType.PAWN) {
			return pawnReachable(newCoord, piece);   }
		else if(type==ChessPieceType.ROOK) {
			return rookReachable(newCoord, piece);   }
		else if(type==ChessPieceType.BISHOP) {
			return bishopReachable(newCoord, piece); }
		else if(type==ChessPieceType.KNIGHT) {
			return knightReachable(newCoord, piece); }
		else if(type==ChessPieceType.QUEEN) {
			return queenReachable(newCoord, piece);  }
		else {
			return kingReachable(newCoord, piece);   }
	}
	//check if a pawn moving two spaces forward will pass another piece, or if you try to move straight forward with an enemy piece there
	public boolean unobstructedPawn(ChessCoordinates newCoord, ChessPiece piece) {	
		if(piece.getCoord().getRow()==1 && newCoord.getRow()==3) //move two spaces forward from BLACK side
			return (grid[2][piece.getCoord().getColumn()] == null && grid[3][piece.getCoord().getColumn()] == null);
		else if(piece.getCoord().getRow()==6 && newCoord.getRow()==4) // move two spaces forward from WHITE side
			return (grid[5][piece.getCoord().getColumn()] == null && grid[4][piece.getCoord().getColumn()] == null);
		//check if moving one space forward to an empty space
		else if(newCoord.getColumn()==piece.getCoord().getColumn() && newCoord.getRow()==piece.getCoord().getRow()+1 && grid[newCoord.getRow()][newCoord.getColumn()]==null)
			return true;
		else if(newCoord.getColumn()==piece.getCoord().getColumn() && newCoord.getRow()==piece.getCoord().getRow()-1 && grid[newCoord.getRow()][newCoord.getColumn()]==null)
			return true;
		//check validity of pawns trying to capture diagonally
		else if(newCoord.getColumn()!=piece.getCoord().getColumn() && grid[newCoord.getRow()][newCoord.getColumn()]!=null && grid[newCoord.getRow()][newCoord.getColumn()].getColor()!=piece.getColor()) 
			return true;
		else return false;
	}
	public boolean unobstructedRook(ChessCoordinates newCoord, ChessPiece piece) {
		int x = piece.getCoord().getRow();
		int y = piece.getCoord().getColumn();
		int nx = newCoord.getRow();
		int ny = newCoord.getColumn();
		if(x==nx) { //rook moves along a row
			if(y>ny) { //rook moves left
				for(int start = y-1; start > ny; start--) 				
					if(grid[x][start] != null) return false;
				return true;
			}
			else { //rook moves right
				for(int start = y+1; start < ny; start++) 				
					if(grid[x][start] != null) return false;
				return true;
			}
		}
		else { //rook moves along a column
			if(x>nx) { //rook moves upward
				for(int start = x-1; start > nx; start--) 				
					if(grid[start][y] != null) return false;
				return true;
			}
			else { //rook moves downward
				for(int start = x+1; start < nx; start++) 				
					if(grid[start][y] != null) return false;
				return true;
			}
		}
	}
	
	public boolean unobstructedBishop(ChessCoordinates newCoord, ChessPiece piece) {
		int x = piece.getCoord().getRow();
		int y = piece.getCoord().getColumn();
		int nx = newCoord.getRow();
		int ny = newCoord.getColumn();
		if(x<nx) { //moves diagonally downward
			if(y<ny) { //moves diagonally downward to the right
				for(int start = 1; start < (nx-x); start++) 
					if((grid[x+start][y+start]) != null) return false;
				return true;
			}
			else { //moves diagonally downward to the left
				for(int start = 1; start < (nx-x); start++) 
					if((grid[x+start][y-start]) != null) return false;
				return true;
			}
		}
		else { //moves diagonally upward
			if(y<ny) { //moves diagonally upward to the right
				for(int start = 1; start < (x-nx); start++) 
					if((grid[x-start][y+start]) != null) return false;
				return true;
			}
			else { //moves diagonally upward to the left
				for(int start = 1; start < (x-nx); start++) 
					if((grid[x-start][y-start]) != null) return false;
				return true;
			}
		}
	}
	
	public boolean unobstructedQueen(ChessCoordinates newCoord, ChessPiece piece) {
		if(piece.getCoord().getRow()==newCoord.getRow() ^ piece.getCoord().getColumn()==newCoord.getColumn()) 
			return unobstructedRook(newCoord, piece);
		else
			return unobstructedBishop(newCoord, piece);
	}
	
	public boolean unobstructed(ChessCoordinates newCoord, ChessPiece piece) {
		if(piece.getType()==ChessPieceType.KNIGHT || piece.getType()==ChessPieceType.KING) return true; //Knights and Kings are never obstructed
		if(piece.getType()==ChessPieceType.PAWN) {
			return unobstructedPawn(newCoord, piece);
		}
		else if(piece.getType()==ChessPieceType.ROOK) {
			return unobstructedRook(newCoord, piece);
		}
        else if(piece.getType()==ChessPieceType.BISHOP) {
        	return unobstructedBishop(newCoord, piece);
		}
        else
        	return unobstructedQueen(newCoord, piece);
	}
	
	public boolean isValidMove(ChessCoordinates newCoord, ChessPiece piece) throws IllegalMoveException{
		if(newCoord.getColumn()>7 || newCoord.getColumn()<0) {
			throw new IllegalMoveException("Illegal Column"); }
		else if(newCoord.getRow()>7 || newCoord.getRow()<0) {
			throw new IllegalMoveException("Illegal Row"); }
		else if(grid[newCoord.getRow()][newCoord.getColumn()]!=null && grid[newCoord.getRow()][newCoord.getColumn()].getColor()==piece.getColor())  //you cannot capture your own piece
			return false;
		if(reachable(newCoord, piece) && unobstructed(newCoord, piece)) 
			return true;
		else 
			return false;
	}
	
	public void capturePiece(ChessPiece piece) {
		ChessPieceColor color = piece.getColor();
		ChessPieceType chessType = piece.getType();
		int offset;
		if(color==ChessPieceColor.WHITE) offset = 0;
		else offset = 6;
		inventory[chessType.getInventoryIndex()+offset] -= 1;
	}
	
	public void movePiece(ChessCoordinates newCoord, ChessPiece piece) throws IllegalMoveException {
		assert grid[piece.getCoord().getRow()][piece.getCoord().getColumn()] != null; //make sure you moving a piece that exists on the grid
		assert((newCoord.getRow()==piece.getCoord().getRow() && newCoord.getColumn()==piece.getCoord().getColumn()) == false); //make sure piece makes "progress," ie. you are not allowed to do nothing
		ChessCoordinates oldCoord = piece.getCoord();
		if(isValidMove(newCoord, piece) == false) {
			throw new IllegalMoveException("Illegal Move"); 
		}
		else {
			if(grid[newCoord.getRow()][newCoord.getColumn()]!=null) {
				//internal bookkeeping
				capturePiece(grid[newCoord.getRow()][newCoord.getColumn()]);
			}
			grid[newCoord.getRow()][newCoord.getColumn()] = new ChessPiece(piece.getType(), piece.getColor(), new ChessCoordinates(newCoord.getRow(), newCoord.getColumn()));
			grid[oldCoord.getRow()][oldCoord.getColumn()] = null;
			gamestate.numberMoves += 1;
			ChessPieceColor opposite;
			if(gamestate.playerTurn==ChessPieceColor.BLACK) opposite = ChessPieceColor.WHITE;
			else opposite = ChessPieceColor.BLACK;
			
			if(isCheck(opposite)) {
				if(noValidMoves(opposite)) {
					gamestate.status = GameStatus.CHECKMATE;
				}
				else gamestate.status = GameStatus.CHECK;
			}
			else if(noValidMoves(opposite)) {
				gamestate.status = GameStatus.STALEMATE;
				//END THE GAME OR SOMETHING
			}
			else gamestate.status = GameStatus.ONGOING;
			
			if(gamestate.playerTurn==ChessPieceColor.WHITE) gamestate.playerTurn = ChessPieceColor.BLACK;
			else gamestate.playerTurn = ChessPieceColor.WHITE;

		}
	}
	//once a player ends turn, check whether the king can be captured in one move
	//ie go through entire gameboard and see whether each piece of a given player can make a legal move to capture the king 
	
	public ChessCoordinates findKingCoord(ChessPieceColor color) {
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(grid[i][j]!=null)
					if(grid[i][j].getType()==ChessPieceType.KING && grid[i][j].getColor()==color)
						return new ChessCoordinates(grid[i][j].getCoord().getRow(), grid[i][j].getCoord().getColumn());	
			}
		}
		return null; 
	}
	//check whether a player is in check
	public boolean isCheck(ChessPieceColor color) throws IllegalMoveException {
		ChessCoordinates KingCoordinates = findKingCoord(color);
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(grid[i][j]!=null) 
					if(isValidMove(KingCoordinates, grid[i][j]))
						return true;
			}
		}
		return false;
	}
	public ArrayList<ChessMove> generateLegalMoves(ChessPieceColor color) throws IllegalMoveException{
		ArrayList<ChessMove> movesArray = new ArrayList<ChessMove>();
		movesArray.clear();
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(grid[i][j]!=null) {
					for(int k = 0; k < 8; k++) {
						for(int l = 0; l < 8; l++) {
							if(isValidMove(new ChessCoordinates(k, l), grid[i][j]))
								movesArray.add(new ChessMove(new ChessCoordinates(k, l), grid[i][j]));
						}
					}
				}
			}
		}
		return movesArray;
	}
	public boolean noValidMoves(ChessPieceColor color) throws IllegalMoveException {
		return generateLegalMoves(color).isEmpty();
	}
	
	public void clearGrid() {
		for(int row = 0; row < 8; row++) {
			for(int column = 0; column < 8; column++) {
				grid[row][column] = null;
			}
		}
	}
	//used to help setup the back rows of the board for the rooks, bishops, and knights
	private void initializeBackrowPiecePosition(ChessPieceType type) {
		int column = type.getStartingColumn();
		grid[0][column] = new ChessPiece(type, ChessPieceColor.BLACK, new ChessCoordinates(0, column));
		grid[0][7-column] = new ChessPiece(type, ChessPieceColor.BLACK, new ChessCoordinates(0, 7-column));
		grid[7][column] = new ChessPiece(type, ChessPieceColor.WHITE, new ChessCoordinates(7, column));
		grid[7][7-column] = new ChessPiece(type, ChessPieceColor.WHITE, new ChessCoordinates(7, 7-column));
	}
	
	public GameBoard() {
		clearGrid();
		for(int column = 0; column < 8; column++) {
			grid[6][column] = new ChessPiece(ChessPieceType.PAWN, ChessPieceColor.WHITE, new ChessCoordinates(6, column));
			grid[1][column] = new ChessPiece(ChessPieceType.PAWN, ChessPieceColor.BLACK, new ChessCoordinates(1, column));
		}
		initializeBackrowPiecePosition(ChessPieceType.ROOK);
		initializeBackrowPiecePosition(ChessPieceType.KNIGHT);
		initializeBackrowPiecePosition(ChessPieceType.BISHOP);
		
		grid[0][3] = new ChessPiece(ChessPieceType.QUEEN, ChessPieceColor.BLACK, new ChessCoordinates(0, 3));
		grid[7][3] = new ChessPiece(ChessPieceType.QUEEN, ChessPieceColor.WHITE, new ChessCoordinates(7, 3));
		grid[0][4] = new ChessPiece(ChessPieceType.KING, ChessPieceColor.BLACK, new ChessCoordinates(0, 4));
		grid[7][4] = new ChessPiece(ChessPieceType.KING, ChessPieceColor.WHITE, new ChessCoordinates(7, 4));
		
		inventory =  new int[] {8,2,2,2,1,1,8,2,2,2,1,1};
		
		gamestate = new GameState(ChessPieceColor.WHITE, 0, GameStatus.ONGOING);
	}
	
}
