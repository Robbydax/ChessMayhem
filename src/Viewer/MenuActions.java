package Viewer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import ChessBoard.*;

public class MenuActions implements ActionListener {
	GameBoard board;
	ChessGUI gui;
	
	public MenuActions(GameBoard board, ChessGUI chessGui) {
        this.board = board;
        this.gui = chessGui;
    }
	public void actionPerformed(ActionEvent e) {
        String menuItemText = ((JMenuItem)e.getSource()).getText();

        //determines which menu item based on the first letter of it's text
        switch (menuItemText)
        {
            case "forfeit": 
                forfeitGame();
                break;

            case "restart": 
                restartGame();
                break;

            case "undo":
                undoMove();
                break;
        }
    }
	
	private void forfeitGame( ) {
		
	}
	
	private void restartGame() {
		
		
	}
	
	private void undoMove() {
		
	}
	
}
