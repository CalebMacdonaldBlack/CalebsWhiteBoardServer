import java.io.Serializable;

public class DrawPath extends Packet implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int oldX, oldY, currentX, currentY, brushWidth, clientID;
	String mouseAction;
	
	public DrawPath(int oldX, int oldY, int currentX, int currentY, int brushWidth, int clientID,
			String mouseAction) {
		this.oldX = oldX;
		this.oldY = oldY;
		this.currentX = currentX;
		this.currentY = currentY;
		this.brushWidth = brushWidth;
		this.clientID = clientID;
		this.mouseAction = mouseAction;
	}

	@Override
	public DrawPath getDrawPath(){
		return this;
	}
	

}
