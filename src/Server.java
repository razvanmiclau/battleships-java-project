import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/*
 * SERVER.JAVA - SERVER CLASS / INTERFACE
 * --------------------------------------------------------------
 * Must be executed before GAME.java
 */

public class Server extends JFrame {
	
	// PROPERTIES
	private int port;
	private ServerSocket server;
	private JPanel statusPanel = new JPanel(new BorderLayout());
	private JPanel buttonPanel = new JPanel();
	public static JTextArea textarea = new JTextArea();
	public JScrollPane scroll = new JScrollPane(textarea);
	
	// SERVER CONSTRUCTOR
	public Server(int port) throws IOException{
		super("Server - PORT "+port);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(200,200);
		this.port = port;
		
		initInterface();
		run();
	}
	
	// INITIALISE INTERFACE FOR THE SERVER
	public void initInterface(){
		Container contentPane = getContentPane();
		contentPane.add(statusPanel, BorderLayout.CENTER);
		statusPanel.add(scroll);
	}
	
	// RUN GAME HANDLING THREAD
	public void run() throws IOException{
		try {
			server = new ServerSocket(port);
			Server.textarea.append("Server started on port " + server.getLocalPort() + "\n");
			while (true) {
				int count = 0;
				Player[] players =  new Player[2];
				while (count < 2) {
					players[count] = new Player(server.accept());
					count++;
				}
				new GameThread(players[0], players[1]).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			server.close();
		}
	}

	public static void main(String[] args) throws IOException {
		new Server(8080).setVisible(true);
	}

}
