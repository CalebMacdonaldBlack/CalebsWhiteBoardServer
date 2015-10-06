
public class DrawPath extends Packet{
	
	int oldX, oldY, currentX, currentY, brushWidth, clientID;
	String color;
	
	public DrawPath(String packetID, int oldX, int oldY, int currentX, int currentY, int brushWidth, int clientID,
			String color, String packetID2) {
		this.oldX = oldX;
		this.oldY = oldY;
		this.currentX = currentX;
		this.currentY = currentY;
		this.brushWidth = brushWidth;
		this.clientID = clientID;
		this.color = color;
		packetID = packetID2;
	}

	@Override
	public DrawPath getDrawPath(){
		return this;
	}
	

}
