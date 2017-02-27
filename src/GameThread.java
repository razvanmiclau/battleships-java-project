import java.util.Random;

/*
 * GAMETHREAD.JAVA - A SERVER THREAD THAT HANDLES GAMES
 */

public class GameThread extends Thread {
	
	// PROPERTIES
    private Player first;
    private Player second;
    private Player currentPlayer;
    private int turn;
    
    
    // THREAD CONSTRUCTOR
    public GameThread(Player first, Player second) {
        this.first = first;
        this.second = second;
    }

    // CHECKS WHETHER A SHIP HAS BEEN HIT OR NOT
    public boolean checkHit(String coordinates, String[] positions) {
        boolean hit = false;
        for (int i = 0; i < positions.length; i++) {
            if (positions[i].equals(coordinates)) {
                hit = true;
                break;
            }
        }
        return hit;
    }

    // SWITCH PLAYERS AT THE END OF THE TURN
    private void switchPlayers() {
        if (currentPlayer == first) {
            turn++;
            Server.textarea.append("<-------------> \n END TURN "+turn + "\n <-------------> \n");
            currentPlayer = second;
        } else {
            currentPlayer = first;
        }
    }
    
    // RETURNS CURRENT PLAYER, AT PLAYER'S TURN
    private Player otherPlayer() {
        return currentPlayer == first ? second : first;
    }

    @Override
    public void run() {
        Player[] players = {first, second};

        currentPlayer = players[new Random().nextInt(2)];
        currentPlayer.getOut().println("make a move");
        otherPlayer().getOut().println("enemy moves");
        try {
            while (true) {
                String request = currentPlayer.getIn().readLine();
                System.out.println(request);
                Server.textarea.append(request + "\n");
                if (request.equals("enemy lost")) {
                    otherPlayer().getOut().println("you lost");
                    break;
                }
                boolean hit = checkHit(request, otherPlayer().getPositions());
                if (hit) {
                    currentPlayer.getOut().println("hit");
                    otherPlayer().getOut().println("hit," + request);
                } else {
                    switchPlayers();
                    currentPlayer.getOut().println("make a move," + request);
                    otherPlayer().getOut().println("enemy moves");
                }
            }
            System.out.println("Game finished");
            Server.textarea.append("Game Finished \n");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
