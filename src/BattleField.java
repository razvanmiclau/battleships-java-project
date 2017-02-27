import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


 /* BATTLEFIELD.java - WRAPPING UP BATTLEFIELD PANELS AND THE GAME LOGIC
  * ------------------------------------------------------------------------------------
  */

public class BattleField extends JPanel {
	
	// SERVER_STUFF
	private Socket socket;
	private PrintWriter out;
    private BufferedReader in;
    private String ipAddress = "localhost";
	
	// PLAYERS_GRIDS
    private JButton[][] playerGrid, enemyGrid;

    // SHIPS_POSITIONS
    private String[] positions;
    
    // GUI_PANELS
    private JPanel playerGridPanel, enemyGridPanel, shipsContainer, shipsPanel,radioPanel;
    
    /* BUTTONS / RADIOS */
    private JRadioButton rHorizontal = new JRadioButton("Horizontal");
    private JRadioButton rVertical = new JRadioButton("Vertical");
    private ButtonGroup radioButtons = new ButtonGroup();
    
	// SHIPS_ICONS
    /*
     * Icons extracted from http://www.clipartbest.com/battleship-vector-free
     * All rights reserved to their respective owners. We DO NOT INTEND to use these for commercial purposes.
     */
	Icon[] iconArray = {
			new ImageIcon(getClass().getResource("/battleship_project/images/aircraftcarrier.png")), //0
			new ImageIcon(getClass().getResource("/battleship_project/images/battleship.png")), //1
			new ImageIcon(getClass().getResource("/battleship_project/images/submarine.png")), //2
			new ImageIcon(getClass().getResource("/battleship_project/images/destroyer.png")), //3
			new ImageIcon(getClass().getResource("/battleship_project/images/patrolboat.png")), //4
			new ImageIcon(getClass().getResource("/battleship_project/images/hit.png")), //5
			new ImageIcon(getClass().getResource("/battleship_project/images/hit2.gif")), //6
			new ImageIcon(getClass().getResource("/battleship_project/images/miss.png")), //7
	};
	
    // SHIPS
    JButton[] ships = {
    		new Ship("Aircraft", 5), 
    		new Ship("Battleship", 4),
            new Ship("Submarine", 3), 
            new Ship("Destroyer", 3),
            new Ship("Patrol Boat", 2)
    };

    // DEFAULT_SETTINGS
    private boolean vertical = false;
    private boolean isFull = false;
    public int life;

    // BATTLEFIELD_CONSTRUCTOR
    public BattleField(int row, int col) {
        setSize(500, 500);
        setLayout(new GridLayout(1, 1, 3, 3));

		/* INITIALIZE CONTAINERS & BORDERS */
        /*--------------------------------------------------------*/
	        playerGridPanel = new JPanel(new GridLayout(row, col, 3, 3));
	        enemyGridPanel = new JPanel(new GridLayout(row, col, 3, 3));
	        shipsContainer = new JPanel(new BorderLayout());
	        shipsPanel = new JPanel(new GridLayout(0, 1, 3, 3));
	        radioPanel = new JPanel(new FlowLayout());
	        initBorders();
        /*--------------------------------------------------------*/

		/* INSERT OBJECTS INSIDE PANELS */
        /*--------------------------------------------------------*/
	        add(playerGridPanel);add(shipsContainer);add(enemyGridPanel);
	        shipsContainer.add(new JPanel(), BorderLayout.WEST);
	        shipsContainer.add(new JPanel(), BorderLayout.NORTH);
	        shipsContainer.add(shipsPanel, BorderLayout.CENTER);
	        shipsContainer.add(radioPanel, BorderLayout.SOUTH);
	        radioPanel.add(rHorizontal);
	        radioPanel.add(rVertical);
        /*--------------------------------------------------------*/
        
		/* INSERT Icon on Ship buttons */
        /*--------------------------------------------------------*/
	        for(int i=0; i<ships.length; i++) {
	        	ships[i].setIcon(iconArray[i]);
	        }
        /*--------------------------------------------------------*/

        /* INITIALIZE LIFE
		/*--------------------------------------------------------*/
        	life = getLife();
        /*--------------------------------------------------------*/

        /* INITIALIZE GRIDS
    	/*-----------------------------------------------------*/
	        playerGrid = new JButton[row][col];
	        for (int x = 0; x < row; x++) {
	            for (int y = 0; y < col; y++) {
	                playerGrid[x][y] = new JButton();
	                // add action listener here...
	                playerGrid[x][y].addActionListener(placeShips);
	                // playerGrid[x][y].addMouseListener(addShips);
	                playerGridPanel.add(playerGrid[x][y]);
	            }
	        }
	
	        enemyGrid = new JButton[row][col];
	        for (int x = 0; x < row; x++) {
	            for (int y = 0; y < col; y++) {
	                enemyGrid[x][y] = new JButton();
	                enemyGrid[x][y].setEnabled(false);
	                enemyGridPanel.add(enemyGrid[x][y]);
	            }
	        }
		/*--------------------------------------------------------*/

	    /* INSERT SHIPS AND RADIOS BUTTONS TO THEIR PANELS
		/*-----------------------------------------------------*/
	        for (int i = 0; i < ships.length; i++) {
	            shipsPanel.add(ships[i]);
	        }
	        radioButtons.add(rHorizontal);
	        radioButtons.add(rVertical);
	        rHorizontal.setSelected(true);
        /*--------------------------------------------------------*/

	    /* ADDS FUNCTIONALITY TO RADIO BUTTONS - HORIZONTAL / VERTICAL
		/*------------------------------------------------------------*/
	        rHorizontal.addActionListener(new ActionListener() {
	
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                vertical = false;
	                Game.textarea.append("Current State : HORIZONTAL" + "\n");
	
	            }
	
	        });
	
	        rVertical.addActionListener(new ActionListener() {
	
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                vertical = true;
	                Game.textarea.append("Current State : VERTICAL" + "\n");
	            }
	
	        });
		/*-------------------------------------------------------------*/

    }
    
    
    
    

    /* METHODS / ACTIONSLISTENERS / FUNCTIONALITY OF OBJECTS
	 * -------------------------------------------------------
	 * 1. initBorders() 
	 * 2. getPositions(JButton[] PlayerGrid)
	 * 3. checkPositions()
	 * 4. waitForTurn()
	 * 5. setEnemyGridEnabled(boolean b)
	 * 6. checkEndPos()
	 * 7. checkEndGame()
	 * 8. getLife()
	 * 9. showDialog()
	 * --------------------------------
	 * 10. placeShips - ActionListener
	 * 11. fire - ActionListener
	/*=================================================================*/


    /* INITBORDERS() - ADD BORDERS TO PANELS
	/*-----------------------------------------------------------------*/
	    public void initBorders() {
	        playerGridPanel.setBorder(BorderFactory.createCompoundBorder(
	                BorderFactory.createTitledBorder("Your Field"),
	                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
	
	        enemyGridPanel.setBorder(BorderFactory.createCompoundBorder(
	                BorderFactory.createTitledBorder("Your Enemy"),
	                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
	
	        shipsPanel.setBorder(BorderFactory.createCompoundBorder(
	                BorderFactory.createTitledBorder("Available Ships"),
	                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
	
	        radioPanel.setBorder(BorderFactory.createCompoundBorder(
	                BorderFactory.createTitledBorder("Selected Position"),
	                BorderFactory.createEmptyBorder(3, 3, 3, 3)));
	    }
	/*------------------------------------------------------------------*/
    
    /* GETPOSITIONS - RETURNS AN ARRAY OF POSITIONS WHERE THE SHIPS HAVE
     * BEEN PLACED ON THE GRID
     * 
     * To be used with the String[] positions array!
	/*-----------------------------------------------------------------*/
	    public String[] getPositions(JButton jbplayerGrid[][]) {
	        String[] toReturn;
	        int counter = 0;
	        toReturn = new String[17];
	        
	        for (int i = 0; i < 10; i++) {
	            for (int j = 0; j < 10; j++) {
	                if (jbplayerGrid[i][j].getBackground().equals(Color.GRAY)) {
	                    toReturn[counter] = i + ":" + j;
	                    counter++;
	                }
	            }
	        }
	        return toReturn;
	    }
    /*------------------------------------------------------------------*/
    
    
    /* CHECKPOSITION() - ACTIVATES THE BATTLE_PHASE AFTER ALL POSITIONS
     * HAVE BEEN FILLED BY SENDING THROUGH THE SERVER BOTH PLAYERS COORDINATES
     * VIA THE POSITIONS ARRAY
	/*-----------------------------------------------------------------*/
	    public void checkPositions() throws IOException {
	        for (int i = 0; i < positions.length;) {
	            if (positions[positions.length - 1] != null) {
	                socket = new Socket(ipAddress, 8080);
	
	                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
	                objectOutputStream.writeObject(positions);
	
	                out = new PrintWriter(socket.getOutputStream(), true);
	                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	                int row = 10;
	                int col = 10;
	                JOptionPane.showMessageDialog(this,
	                        "Well Done! Waiting for opponent...");
	                for (int x = 0; x < row; x++) {
	                    for (int y = 0; y < col; y++) {
	                        playerGrid[x][y].setEnabled(false);
	                        enemyGrid[x][y].addActionListener(fire);
	                    }
	                }
	                String message = in.readLine();
	                if (message.equals("make a move")) {
	                    setEnemyGridEnabled(true);
	                    JOptionPane.showMessageDialog(this, "Your turn");
	                } else {
	                    JOptionPane.showMessageDialog(this, "Opponents turn");
	                    waitForTurn();
	                }
	            }
	            break;
	        }
	    }
    /*------------------------------------------------------------------*/
    
    
    /* WAITFORTURN() - HANDLING THREAD FOR EACH PLAYER'S TURN
	/*-----------------------------------------------------------------*/
	    private void waitForTurn() {
	        new Thread() {
	            @Override
	            public void run() {
	                try {
	                    String message = in.readLine();
	                    String[] messageSplit = message.split(",");
	                    String[] position = messageSplit[1].split(":");
	                    while (!messageSplit[0].equals("make a move")) {
	                        System.out.println(message);
	                        if (messageSplit[0].equals("hit")) {
	                            int row = Integer.parseInt(position[0]);
	                            int col = Integer.parseInt(position[1]);
	                            
	                            playerGrid[row][col].setIcon(iconArray[5]);
	                            playerGrid[row][col].setDisabledIcon(iconArray[5]);
	                        } else if (message.equals("you lost")) {
	                            showDialog("You lost.");
	                            break;
	                        }
	                        message = in.readLine();
	                        messageSplit = message.split(",");
	                        position = messageSplit[1].split(":");
	                    }
	                    if (messageSplit[0].equals("make a move")) {
	                        int row = Integer.parseInt(position[0]);
	                        int col = Integer.parseInt(position[1]);
	                        
	                        playerGrid[row][col].setIcon(iconArray[7]);
	                        playerGrid[row][col].setDisabledIcon(iconArray[7]);
	                        setEnemyGridEnabled(true);
	                    }
	                } catch (Exception e) {
	                }
	            }
	        }.start();
	
	    }
    /*------------------------------------------------------------------*/

    /* SET_ENEMY_GRID_ENABLED(BOOLEAN B) - ACTIVATE/DEACTIVE ENEMY'S GRID
	/*--------------------------------------------------------------------*/
    private void setEnemyGridEnabled(boolean enabled) {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
            	
            	if(enemyGrid[x][y].getIcon() == iconArray[6] || enemyGrid[x][y].getIcon() == iconArray[7]) {
            		enemyGrid[x][y].setEnabled(false);
            	}else
                enemyGrid[x][y].setEnabled(enabled);
            }
        }
    }
    /*------------------------------------------------------------------*/
    
    
    /* CHECK_END_POS() - CHECKS WHETER THE POSITIONS ARRAY HAS BEEN FILLED OR NOT
	/*----------------------------------------------------------------------------*/
	    public boolean checkEndPos() {
	        if (positions[positions.length - 1] != null) {
	            isFull = true;
	        }
	        return isFull;
	    }
    /*------------------------------------------------------------------------*/

	    
	/* GETLIFE() - RETURN PLAYER'S LIFE, BASED ON THE CUMMULATIVE SHIPS LENGTH
	/*------------------------------------------------------------------------*/
	    public int getLife() {
	        for (int i = 0; i < ships.length; i++) {
	            life += ((Ship) ships[i]).getLength();
	        }
	        return life;
	    }
    /*------------------------------------------------------------------------*/
    
	/* CHECK_END_GAME
	/*------------------------------------------------------------------------*/
	    public boolean checkEndGame(boolean b) {
	        if (life == 0) {
	        }
	        return b;
	    }
    /*------------------------------------------------------------------------*/
    
	/* SHOWDIALOG(MESSAGE) - DISPLAY A POP-UP MESSAGE
	/*------------------------------------------------------------------------*/
	    private void showDialog(String message) {
	        JOptionPane.showMessageDialog(this, message);
	    }
    /*------------------------------------------------------------------------*/
	    
	    
	    
	    
	    
	    
	    
    
	/* FIRE / ACTIONLISTENER - FIRE ENEMY AT POSITION
	/* ------------------------------------------------------------------------------------------------*/
	    ActionListener fire = new ActionListener() {
	        @Override
			public void actionPerformed(ActionEvent e) {
	            try {
	                for (int row = 0; row < 10; row++) {
	                    for (int col = 0; col < 10; col++) {
	                        if (enemyGrid[row][col] == e.getSource()) {
	                            String coordinates = row + ":" + col;
	                            System.out.println(coordinates);
	                            out.println(coordinates);
	                            String response = in.readLine();
	                            boolean hit = response.equals("hit");
	                            
	                            if (!hit) {
	                                //enemyGrid[row][col].setBackground(Color.RED);
	                            	enemyGrid[row][col].setIcon(iconArray[7]);
	                            	enemyGrid[row][col].setDisabledIcon(iconArray[7]);
	                                enemyGrid[row][col].setEnabled(false);
	                                Game.textarea.append("MISS "
	                                        + " =====> Enemy's Life: " + life + "\n");
	                                setEnemyGridEnabled(false);
	                                waitForTurn();
	                            } else {
	                                //enemyGrid[row][col].setBackground(Color.GREEN);
	                            	enemyGrid[row][col].setIcon(iconArray[6]);
	                            	enemyGrid[row][col].setDisabledIcon(iconArray[6]);
	                                enemyGrid[row][col].setEnabled(false);
	                                life--;
	                                if (life == 0) {
	                                    showDialog("Congratulations! You have won the battle!");
	                                    setEnemyGridEnabled(false);
	                                    Game.textarea.append("Well Done! You have won the game!");
	                                    out.println("enemy lost");
	                                    // System.exit(-1);
	                                } else {
	                                    Game.textarea.append("HIT "
	                                            + " =====> Enemy's Life: " + life
	                                            + "\n");
	                                }
	
	                            }
	                        }
	                    }
	                }
	            } catch (Exception e1) {
	                e1.printStackTrace();
	            }
	        }
	    };
    /*------------------------------------------------------------------------------------------------*/
    
	    
	/* PLACESHIPS / ACTIONLISTENER - GAME LOGIC FOR PLACING SHIPS TO THE GRID
	/* ------------------------------------------------------------------------------------------------*/
	    ActionListener placeShips = new ActionListener() {
	
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            int length;
	
	            for (int row = 0; row < 10; row++) {
	                for (int col = 0; col < 10; col++) {
	                    for (int ship = 0; ship < ships.length; ship++) {
	                        if (playerGrid[row][col] == e.getSource()) {
	                            if (((Ship) ships[ship]).isPressed() == true) {
	                                length = ((Ship) ships[ship]).getLength();
	
	                                if (vertical == false) {
	                                    for (int i = 0; i < length; i++) {
	                                        if (col + length > 10) {
	                                            showDialog("Can't Place that there My Lord!");
	                                            break;
	                                        } 
	                                        
	                                        if (playerGrid[row][i + col].getBackground() == Color.GRAY || playerGrid[row][col+(length-1)].getBackground() == Color.GRAY) {
	                                             JOptionPane.showMessageDialog(null, "You can't place a ship on top of another ship!");
	                                             ((Ship) ships[ship]).setPressed(true);
	                                             break;
	                                        } 
	                                        
	                                        else {
	                                             playerGrid[row][i + col].setBackground(Color.GRAY);
	                                             playerGrid[row][i + col].setEnabled(false);
	                                             positions = getPositions(playerGrid);
	                                             ((Ship) ships[ship]).setPressed(false);
	                                        }
	                                    }
	                                } 
	                                
	                                else {
	                                    for (int i = 0; i < length; i++) {
	                                        if (row + length > 10) {
	                                            JOptionPane.showMessageDialog(null,"Can't Place that there My Lord!");
	                                            break;
	                                        } 
	                                            
	                                        if (playerGrid[i + row][col].getBackground() == Color.GRAY || playerGrid[row+(length-1)][col].getBackground() == Color.GRAY) {
	                                            JOptionPane.showMessageDialog(null, "You can't place a ship on top of another ship!");
	                                            break;
	                                        } 
	                                        
	                                        else {
	                                            playerGrid[i + row][col].setBackground(Color.GRAY);
	                                            playerGrid[i + row][col].setEnabled(false);
	                                            positions = getPositions(playerGrid);
	                                            ((Ship) ships[ship]).setPressed(false);
	                                        }  
	                                    }
	                                }
	
	                            }
	                        }
	                    }
	                }
	            }
	            
	            
	             /* SEND POSITIONS
	             /*--------------------------------------------------------*/
			            try {
			                if (checkEndPos() == true) {
			                    String pos = Arrays.toString(positions);
			                    System.out.println(pos);
			                    Game.textarea.append("You've placed your ships on the following positions: "+pos+"\n");
			                }
			                checkPositions();
			            } catch (UnknownHostException e1) {
			                e1.printStackTrace();
			            } catch (IOException e1) {
			                e1.printStackTrace();
			            }
		          /*--------------------------------------------------------*/
	        }
	
	    };
	/*----------------------------------------------------------------------------------------------------------------*/

}