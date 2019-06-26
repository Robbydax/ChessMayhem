package Viewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import ChessBoard.GameBoard;
 
 
public class ChessGUI implements ActionListener{
	
	public JPanel[][] squares;
    public JFrame boardFrame;
    public Container container;
    public GameBoard theBoard;
    public JMenuBar menubar;
    public JMenu menu;	
    public JMenuItem forfeit, restart, undo;
	
	public ChessGUI(GameBoard board){
		theBoard = board;
		setupGUI();
	}
	
	private void setupGUI() {

		boardFrame = new JFrame("Chess");
		boardFrame.setSize(800,600);
		//JPanel myPanel = initializePanel();
        setUpMenu(boardFrame);
        boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        container = boardFrame.getContentPane();

        container.setLayout(new GridLayout(8,8));
        create_squares();
        
        boardFrame.setVisible(true);
    }
	private JPanel initializePanel() {
        JPanel myPanel = new JPanel();
        myPanel.setPreferredSize(new Dimension(500,500));
        myPanel.setLayout(new BorderLayout());
        return myPanel;
    }
	
	private void setUpMenu(JFrame window) {
        menubar = new JMenuBar();
        MenuActions actionListener = new MenuActions(theBoard, this);
        menu = new JMenu("Menu");
        
        forfeit = new JMenuItem("forfeit");
        forfeit.addActionListener(this);
        menu.add(forfeit);
        restart = new JMenuItem("restart");
        restart.addActionListener(this);
        menu.add(restart);
        undo = new JMenuItem("undo");
        undo.addActionListener(this);
        menu.add(undo);
        
        menubar.add(menu);
        window.setJMenuBar(menubar);
    }
	
	
	private String getPng(int i, int j) {
		 if(i==1) return "Chess_bdt60.png";
         else if(i==6) return "Chess_blt60.png";
         else if(i==0) {
         	if(j==0 || j==7) return "Chess_rdt60.png";
         	else if(j==1 || j ==6) return "Chess_ndt60.png";
         	else if(j==2 || j ==5) return "Chess_pdt60.png";
         	else if(j==3) return "Chess_qdt60.png";
         	else return "Chess_kdt60.png";
         }
         else if(i==7) {
        	if(j==0 || j==7) return "Chess_rlt60.png";
          	else if(j==1 || j ==6) return "Chess_nlt60.png";
          	else if(j==2 || j ==5) return "Chess_plt60.png";
          	else if(j==3) return "Chess_qlt60.png";
          	else return "Chess_klt60.png";
         }
         else return null;
	}
	
	private void create_squares()
    {
        squares = new JPanel[8][8];
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                JPanel p = new JPanel();
                if((i+j)%2 == 0)
                    p.setBackground(Color.WHITE);
                else
                	p.setBackground(Color.gray);
                JLabel il = new JLabel();
                if(getPng(i,j)!=null) il.setIcon(new ImageIcon(getClass().getResource("/images/" + getPng(i, j))));
                p.add(il);
                squares[i][j]=p;
                container.add(p);
            }
        }
    }
   

	@Override
    public void actionPerformed(ActionEvent e) {
        
    }
	
	 public static void main(String[] args) {
		 	GameBoard theBoard = new GameBoard();
	        ChessGUI gui = new ChessGUI(theBoard);
	 }
}