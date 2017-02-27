import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class Player {
	
	// PROPERTIES
	private PrintWriter out;
	private BufferedReader in;
	
	// PLAYER'S SHIP POSITIONS
	private String[] positions;

	public Player(Socket socket) throws IOException {
		try {
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
			positions = (String[]) objectInputStream.readObject();
            System.out.println(Arrays.toString(positions));
            out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (Exception e) {
			out.close();
			in.close();
		}
	}

	public PrintWriter getOut() {
		return out;
	}

	public BufferedReader getIn() {
		return in;
	}
	
	public String[] getPositions() {
		return positions;
	}

}
