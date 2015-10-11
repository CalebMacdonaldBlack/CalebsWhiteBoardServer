import java.io.Serializable;

public class Packet implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String packetID;

	public Packet() {
	}
	
	public DrawPath getDrawPath(){
		return null;
	}
	
	public String getCommand(){
		return null;
	}
}
