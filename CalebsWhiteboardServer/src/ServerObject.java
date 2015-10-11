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
			initializeConnection();
			whileReceiving();
		} catch (IOException e) {
			System.out.println("Disconnected with " + this.hostName);
			hosts.remove(this);
			Thread.currentThread().interrupt();
		}

	}

	private void initializeConnection() {

		Packet assignID = new AssignID(hosts.indexOf(this) + 1);

		outputData(assignID);

		for (Packet socketInputObject : drawPaths) {
			// System.out.println("sending: " + num);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			outputData(socketInputObject);
		}

		// let client know we have finished loading and user can now draw
		outputData(new ServerCommand("finishedLoading"));
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
	@SuppressWarnings("unused")
	private void closeCrap() {
		System.out.println("\n closing connections... \n");
		try {
			output.close();
			input.close();
			connection.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	// send message to client
	private void outputData(Object socketInputObject) {
		try {
			output.writeObject(socketInputObject);
			output.flush();
		} catch (IOException ioException) {
			System.out.println("\n error: can't send message");
		}
	}

	// during the chat conversation
	private void whileReceiving() throws IOException {
		System.out.println("You are now connected");
		System.out.println("Current devices connected: " + hosts.size());
		Object socketInputObject = null;
		do {
			try {
				socketInputObject = (Object) input.readObject();
				Packet packet = (Packet) socketInputObject;
				objectInputIdentify(packet);

			} catch (ClassNotFoundException classNotFoundException) {
				System.out.println("Error: input not an object");
			} catch (ClassCastException classCastException) {
				System.out.println("obj is not a Packet object");
			}

		} while (true);
	}

	// this method finds out what to do with the packet class recieved from the
	// client.
	private void objectInputIdentify(Packet packet) {

		// send out the path if instance of drawpath
		if (packet instanceof DrawPath) {
			DrawPath drawPath = (DrawPath) packet;
			drawPath.clientID = hosts.indexOf(this) + 1;
			drawPaths.add((DrawPath) packet);
			broadcastClient(packet);
		} else if (packet instanceof ServerCommand) {
			ServerCommand serverCommand = (ServerCommand) packet;
			String commandText = serverCommand.getCommand();
			initiateCommand(commandText);
		}

	}

	private void broadcastClient(Object socketInputObject) {
		for (ServerObject bse : hosts) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (this != bse)
				bse.outputData(socketInputObject);
		}
	}

	private void initiateCommand(String command) {
		System.out.println(command);
		switch (command) {
		case "clear":
			drawPaths = new ArrayList<DrawPath>();
			broadcastClient(new ServerCommand("clear"));
			System.out.println("clearing board");
			break;
		case "req":
			System.out.println("data has been requested");
			break;
		}

	}

	// setup streams to send and receive information
	private void setupStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		// output.writeObject(new int[] {-1,0,0,0,0,hosts.indexOf(this)});
		System.out.println("Streams are now setup");
	}

	// wait for connection then display connection iformation
	private void waitForConnection() throws IOException {
		System.out.println("waiting for connection...\n");
		connection = server.accept();

		if (!isDupe(connection.getInetAddress().getHostName())) {
			ServerObject bse = new ServerObject(this.connection, this.server,
					connection.getInetAddress().getHostName());
			hosts.add(bse);
			System.out.println(" Now connected to " + connection.getInetAddress().getHostName());
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
