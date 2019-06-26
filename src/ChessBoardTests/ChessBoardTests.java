package ChessBoardTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import ChessBoard.ChessCoordinates;
import ChessBoard.ChessPieceColor;
import ChessBoard.ChessPieceType;
import ChessBoard.GameBoard;
import ChessBoard.GameStatus;
import ChessBoard.IllegalMoveException;

import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

public class ChessBoardTests {
	
	/**
	 * This test checks that a GameBoard is properly initialized: checking that 
	 * its grid(grid pieces and empty spaces), inventory, and gamestate are correctly set.
	 */
	@Test
	public void instantiateGameBoard() { 
		GameBoard gb = new GameBoard();
		assertNull(gb.grid[3][3]);
		
		assertEquals(gb.grid[1][0].getType(), ChessPieceType.PAWN);
		assertEquals(gb.grid[1][0].getColor(), ChessPieceColor.BLACK);
		assertEquals(gb.grid[6][7].getType(), ChessPieceType.PAWN);
		assertEquals(gb.grid[6][7].getColor(), ChessPieceColor.WHITE);
		
		assertEquals(gb.grid[0][0].getType(), ChessPieceType.ROOK);
		assertEquals(gb.grid[0][0].getCoord().getRow(), 0);
		assertEquals(gb.grid[0][0].getCoord().getColumn(), 0);
		assertEquals(gb.grid[0][0].getColor(), ChessPieceColor.BLACK);
		
		assertEquals(gb.grid[7][2].getType(), ChessPieceType.BISHOP);
		assertEquals(gb.grid[7][2].getCoord().getRow(), 7);
		assertEquals(gb.grid[7][2].getCoord().getColumn(), 2);
		assertEquals(gb.grid[7][2].getColor(), ChessPieceColor.WHITE);
		
		assertEquals(gb.grid[0][3].getType(), ChessPieceType.QUEEN);
		assertEquals(gb.grid[0][3].getCoord().getRow(), 0);
		assertEquals(gb.grid[0][3].getCoord().getColumn(), 3);
		assertEquals(gb.grid[0][3].getColor(), ChessPieceColor.BLACK);
		
		assertEquals(gb.grid[7][4].getType(), ChessPieceType.KING);
		assertEquals(gb.grid[7][4].getCoord().getRow(), 7);
		assertEquals(gb.grid[7][4].getCoord().getColumn(), 4);
		assertEquals(gb.grid[7][4].getColor(), ChessPieceColor.WHITE);
		
		assertTrue(gb.inventory!=null);
		int array[] = {8, 2, 2, 2, 1, 1, 8, 2, 2, 2, 1, 1}; 
		assertTrue(Arrays.equals(gb.inventory, array));
		
		assertEquals(gb.gamestate.numberMoves, 0);
		assertEquals(gb.gamestate.playerTurn, ChessPieceColor.WHITE);
		assertEquals(gb.gamestate.status, GameStatus.ONGOING);
	}
	/**
	 * This test checks that a piece can move to an empty space
	 * 
	 * @throws IllegalMoveException 
	 */
	@Test
	public void movingPieceToEmptySpace() throws IllegalMoveException {
		GameBoard gb = new GameBoard();
		assertNull(gb.grid[5][3]);
		gb.movePiece(new ChessCoordinates(5,3), gb.grid[6][3]);
		assertTrue(gb.grid[5][3] != null);
		assertEquals(gb.grid[5][3].getColor(), ChessPieceColor.WHITE);
		assertTrue(gb.grid[6][3]==null);
		
		assertNull(gb.grid[2][6]);
		gb.movePiece(new ChessCoordinates(2,6), gb.grid[1][6]);
		assertTrue(gb.grid[2][6] != null);
		assertEquals(gb.grid[2][6].getColor(), ChessPieceColor.BLACK);
		assertNull(gb.grid[1][6]);
		
	}
	/**
	 * This test checks that an Exception is caught when the player tries to move
	 * out of the board boundaries
	 * @throws IllegalMoveException 
	 */
	@Test
	public void movingPieceOutOfBounds() throws IllegalMoveException {
		GameBoard gb = new GameBoard();
		try {
			gb.movePiece(new ChessCoordinates(7,8), gb.grid[7][7]);
			fail("Expected IllegalMoveException");
		} catch(Exception IllegalMoveException) {
			assertTrue(gb.grid[7][7] != null);
			assertEquals(gb.grid[7][7].getType(), ChessPieceType.ROOK);
		}
		try {
			gb.movePiece(new ChessCoordinates(-1,4), gb.grid[0][4]);
			fail("Expected IllegalMoveException");
		} catch(Exception IllegalMoveException) {
			assertTrue(gb.grid[0][4] != null);
			assertEquals(gb.grid[0][4].getType(), ChessPieceType.KING);
		}
	}
	/**
	 * This test checks that when a piece is captured, the old piece is properly removed and recorded
	 * in the inventory and the new piece has replaced its position
	 * @throws IllegalMoveException 
	 */
	@Test
	public void capturePiece() throws IllegalMoveException {
		GameBoard gb = new GameBoard();
		gb.movePiece(new ChessCoordinates(3,6), gb.grid[1][6]);
		gb.movePiece(new ChessCoordinates(5,3), gb.grid[6][3]);
		gb.movePiece(new ChessCoordinates(3,6), gb.grid[7][2]);
		assertNull(gb.grid[2][7]);
		assertEquals(gb.grid[3][6].getColor(), ChessPieceColor.WHITE);
		assertEquals(gb.grid[3][6].getType(), ChessPieceType.BISHOP);
		assertEquals(gb.inventory[6], 7);
	}
	/**
	 * This test checks that when one of your pieces tries to validly move into a space that is empty but a piece 
	 * is obstructing its path, an exception is thrown 
	 * @throws IllegalMoveException 
	 */
	@Test
	public void detectObstruction() throws IllegalMoveException {
		GameBoard gb = new GameBoard();
		try {
			gb.movePiece(new ChessCoordinates(4, 2), gb.grid[7][5]);
			fail("Expected IllegalMoveException");
		} catch(Exception IllegalMoveException) {
			assertTrue(gb.grid[7][5] != null);
			assertEquals(gb.grid[7][5].getType(), ChessPieceType.BISHOP);
			assertNull(gb.grid[4][2]);
		}
	}
	/**
	 * This test checks that when one of your pieces tries to move into a space that is already occupied by 
	 * one of your pieces, an exception is thrown
	 * @throws IllegalMoveException 
	 */
	@Test
	public void captureFriendly() throws IllegalMoveException {
		GameBoard gb = new GameBoard();
		try {
			gb.movePiece(new ChessCoordinates(1, 3), gb.grid[0][1]);
			fail("Expected IllegalMoveException");
		} catch(Exception IllegalMoveException) {
			assertTrue(gb.grid[0][1] != null);
			assertEquals(gb.grid[0][1].getType(), ChessPieceType.KNIGHT);
			assertTrue(gb.grid[1][3] != null);
			assertEquals(gb.grid[1][3].getType(), ChessPieceType.PAWN);
		}
		try {
			gb.movePiece(new ChessCoordinates(1, 7), gb.grid[0][7]);
			fail("Expected IllegalMoveException");
		} catch(Exception IllegalMoveException) {
			assertTrue(gb.grid[0][7] != null);
			assertEquals(gb.grid[0][7].getType(), ChessPieceType.ROOK);
			assertTrue(gb.grid[1][7] != null);
			assertEquals(gb.grid[1][7].getType(), ChessPieceType.PAWN);
		}
	}
	/**
	 * This test checks that a king put in "check" can be detected after a move
	 * @throws IllegalMoveException 
	 */
	@Test
	public void detectCheck() throws IllegalMoveException {
		GameBoard gb = new GameBoard();
		gb.movePiece(new ChessCoordinates(5,3), gb.grid[6][3]);
		gb.movePiece(new ChessCoordinates(2,6), gb.grid[1][6]);
		gb.movePiece(new ChessCoordinates(2,2), gb.grid[1][2]);
		gb.movePiece(new ChessCoordinates(3,0), gb.grid[0][3]);
		assertEquals(gb.grid[3][0].getType(), ChessPieceType.QUEEN);
		assertTrue(gb.grid[6][3]==null);
		assertTrue(gb.unobstructedQueen(new ChessCoordinates(7, 4), gb.grid[3][0]));
		assertEquals(gb.gamestate.status, GameStatus.CHECK);
	}
	public void testDog() throws IllegalMoveException {
		GameBoard gb = new GameBoard();
	
	}

}
