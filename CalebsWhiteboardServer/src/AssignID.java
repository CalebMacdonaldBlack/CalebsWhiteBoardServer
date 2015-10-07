
public class AssignID extends Packet{
	int clientID;

	public int getClientID() {
		return clientID;
	}

	public AssignID(int clientID) {
		super();
		this.clientID = clientID;
	}
}
