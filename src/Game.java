import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/*
 * GAME.JAVA - GUI CONTAINER / CLIENT EXECUTABLE
 */

public class Game extends JFrame {
	
	// TOP-MENU
	JMenuBar mainMenu;
	JMenu fileMenu, helpMenu;
	JMenuItem userGuide, gameRules, about, exit;
	
	// PANELS
	JPanel gamePanel = new JPanel(new GridLayout(1, 1, 0, 0));
	JPanel logPanel = new JPanel(new BorderLayout());
	
	// BATTLEFIELDS
	BattleField playerField = new BattleField(10, 10);

	// STATUS CONSOLE
	public static JTextArea textarea = new JTextArea(5, 5);
	JScrollPane scroll = new JScrollPane(textarea);
	
	// GAME CONSTRUCTOR
	public Game() {
		super("Battleship Game - Group 1_B");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1015, 600);
		setLocation(180, 50);
		init();
	}
	
	// INITILISE INTERFACE
	private void init() {
		Container contentPane = getContentPane();
		initMenu();
		contentPane.add(gamePanel, BorderLayout.CENTER);
		contentPane.add(logPanel, BorderLayout.SOUTH);

		gamePanel.setBorder(BorderFactory.createEmptyBorder());
		

		logPanel.setBorder(BorderFactory.createCompoundBorder(			
				BorderFactory.createTitledBorder("Status"),
				BorderFactory.createEmptyBorder(0, 0, 0, 0)));
		

		gamePanel.add(playerField);

		logPanel.add(new JPanel(), BorderLayout.NORTH);
		logPanel.add(new JPanel(), BorderLayout.SOUTH);
		logPanel.add(new JPanel(), BorderLayout.EAST);
		logPanel.add(new JPanel(), BorderLayout.WEST);
		textarea.setEditable(false);
		logPanel.add(scroll, BorderLayout.CENTER);
		setVisible(true);
	}
	
	// ADD MAIN-MENU TO JFRAME
	private void initMenu(){
		mainMenu = new JMenuBar();
		
		fileMenu = new JMenu("File");
			about = new JMenuItem("About");
			exit = new JMenuItem("Exit");
		helpMenu = new JMenu("Help");
			userGuide = new JMenuItem("User Guide");
			gameRules = new JMenuItem("Game Rules");
		
		mainMenu.add(fileMenu);
			fileMenu.add(about);
			fileMenu.addSeparator();
			fileMenu.add(exit);
		mainMenu.add(helpMenu);
			helpMenu.add(userGuide);
			helpMenu.add(gameRules);
			
		// SET MENU	
		setJMenuBar(mainMenu);
		
		// IMPLEMENT FUNCTIONALITY TO JMENU ITEMS
		
		// USER GUIDE
		userGuide.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String rulesTitle = "User Guide";
				String rulesContent = "<html><h2><center><u>Battleship - User Guide</u></center></h2><br><ol>"
						+ "<li><i>Start the server</i> from the Server.JAR executable - <i>ONLY IF YOU ARE THE HOST</i></li>"
						+ "<li><i>Select a ship</i> from the Ships Panel(located in-between the battlefields).</li>"
						+ "<li><i>Place a ship</i> on your battlefield by clicking on any button from the grid.</li>"
						+ "<li><i>Wait for your opponent</i>, after you've placed all of your ships on the battlefield.</li>"
						+ "<li><i>Attack enemy's ship</i> by selecting any position from the enemy grid.</li>"
						+ "<li>If you manage to hit an enemy's ship, you will continue your turn until you miss.</li>"
						+ "<li>For each hit, you/your enemy will lose 1 life point.</li>"
						+ "<li>In order to win the game, reduce your enemy's life points to 0.</li>"
						+ "</ol></html>";
				
				JOptionPane.showMessageDialog(null,
						rulesContent,rulesTitle,JOptionPane.PLAIN_MESSAGE);
				
			}
			
		});
		
		// GAME RULES
		gameRules.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String rulesTitle = "Game Rules";
				String rulesContent = "<html><h2><center><u>Battleship - Game Rules</u></center></h2><br><ul>"
						+ "<li> Battleship is a board game that takes place on a 10 by 10 grid.</li>"
						+ "<li> Each game involves exactly two players.</li>"
						+ "<li> Players begin by positioning a fleet of ships of various sizes in that grid.</li>"
						+ "<li> Players do not know where their opponent’s ships are positioned.</li>"
						+ "<li> When both players are ready, they take turns firing shots to particular cells in the grid of their opponent.</li>"
						+ "<li> In this phase of the game, the ships may not move anymore.</li>"
						+ "<li> After each shot, the player is informed whether the shot landed in water or on a ship.</li>"
						+ "<li> The game is finished when one of the players destroys all of their opponent's ships.</li>"
						+ "</ul></html>";
				
				JOptionPane.showMessageDialog(null,
						rulesContent,rulesTitle,JOptionPane.PLAIN_MESSAGE);
				
			}
			
		});
		
		// ABOUT US
		about.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String title = "About";
				String content = "Battleship Java Game - Team 1B\n\n" +
						"GUI & Game Logic:        Razvan Miclau, Felix Storm-Jensen\n" +
						"Server:                             Ralf Bumanglag, Cheokho Mai, Kevin Mistry \n\n" +
						"Released on: November 10th, 2014";
				
				JOptionPane.showMessageDialog(null,
						content,title,JOptionPane.INFORMATION_MESSAGE);
				
			}
			
		});
		
		// EXIT APPLICATION
		exit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(-1);
				
			}
			
		});
	}

	public static void main(String args[]) {
		new Game().setVisible(true);
	}

}