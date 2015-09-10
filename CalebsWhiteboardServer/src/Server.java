
import java.util.ArrayList;

public class Server {

	/**
	 * Runs the server.
	 */
	public static void main(String[] args) {
		ServerObject.hosts = new ArrayList<ServerObject>();
		ServerObject.intArrayArray = new ArrayList<int[]>();
		ServerObject bse = new ServerObject(null, null, null);
		bse.startRunning();

	}
}
