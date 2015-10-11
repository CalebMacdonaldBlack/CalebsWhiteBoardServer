
public class AssignID extends Packet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int clientID;

	public int getClientID() {
		return clientID;
	}

	public AssignID(int clientID) {
		super();
		this.clientID = clientID;
	}
}
