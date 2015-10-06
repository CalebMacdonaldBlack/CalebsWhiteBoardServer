import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerObject implements Runnable {

	Socket connection;
	ServerSocket server;
	ObjectOutputStream output;
	ObjectInputStream input;
	String hostName;
	public static ArrayList<ServerObject> hosts;
	public static ArrayList<DrawPath> drawPaths;

	@Override
	public void run() {

		try {
			setupStreams();
			loadAllEvents();
			whileReceiving();
		} catch (IOException e) {
			System.out.println("Disconnected with " + this.hostName);
			hosts.remove(this);
			Thread.currentThread().interrupt();
		}

	}

	private void loadAllEvents() {
		for (DrawPath socketInputObject : drawPaths) {
			// System.out.println("sending: " + num);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sendMessage(socketInputObject);
		}

		sendMessage("eventsLoaded");
	}

	public void startRunning() {
		try {
			server = new ServerSocket(9090, 100);

			while (true) {
				try {
					waitForConnection();
				} catch (EOFException eofException) {

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// close streams and sockets after you are done chatting
	private void closeCrap() {
		showMessage("\n closing connections... \n");
		try {
			output.close();
			input.close();
			connection.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	// display message
	private void showMessage(String string) {
		System.out.println(string);
	}

	// send message to client
	private void sendMessage(Object socketInputObject) {
		try {
			output.writeObject(socketInputObject);
			output.flush();
		} catch (IOException ioException) {
			System.out.println("\n error: can't send message");
		}
	}

	// during the chat conversation
	private void whileReceiving() throws IOException {
		String message = "You are now connected";
		System.out.println("Current devices connected: " + hosts.size());
		showMessage(message);
		Object socketInputObject = null;
		do {
			try {
				socketInputObject = (Object) input.readObject();
				DrawPath changedObj = (DrawPath) socketInputObject;
				drawPaths.add(changedObj);
				broadcastClient(changedObj);

			} catch (ClassNotFoundException classNotFoundException) {
				System.out.println("Error: input not an object");
			} catch (ClassCastException classCastException) {
				System.out.println("obj is not an int array");
			}

			try {
				String command = (String) socketInputObject;
				initiateCommand(command);
			} catch (ClassCastException classCastException) {
			}

		} while (!message.equals("CLIENT - END"));
	}

	private void broadcastClient(Object socketInputObject) {
		int[] intArray;
		for (ServerObject bse : hosts) {
			try {
				intArray = (int[]) socketInputObject;
				intArray[5] = hosts.indexOf(bse) + 1;
				socketInputObject = intArray;
			} catch (ClassCastException e) {
				System.out.println("class exception");
			}

			bse.sendMessage(socketInputObject);
		}
	}

	private void initiateCommand(String command) {
		System.out.println(command);
		switch (command) {
		case "clearTheScreen":
			drawPaths = new ArrayList<DrawPath>();
			broadcastClient("clearScreen");
			System.out.println("clearing board");
			break;
		case "reqData":
			System.out.println("data has been requested");
			System.out.println("intArray size: " + drawPaths.size());
			loadAllEvents();
			break;
		}

	}

	// setup streams to send and receive information
	private void setupStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		// output.writeObject(new int[] {-1,0,0,0,0,hosts.indexOf(this)});
		showMessage("Streams are now setup");
	}

	// wait for connection then display connection iformation
	private void waitForConnection() throws IOException {
		showMessage("waiting for connection...\n");
		connection = server.accept();

		if (!isDupe(connection.getInetAddress().getHostName())) {
			ServerObject bse = new ServerObject(this.connection, this.server,
					connection.getInetAddress().getHostName());
			hosts.add(bse);
			showMessage(" Now connected to " + connection.getInetAddress().getHostName());
			Thread thread = new Thread(bse);
			thread.start();
		}

	}

	private boolean isDupe(String hostName) {
		for (ServerObject bse : hosts) {
			if (bse.hostName.equals(hostName))
				return true;
		}
		return false;
	}

	// , ObjectInputStream input, ObjectOutputStream output
	public ServerObject(Socket connection, ServerSocket server, String hostName) {
		super();
		this.connection = connection;
		this.server = server;
		this.hostName = hostName;
	}

}
