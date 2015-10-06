
public class DrawPath {
	int oldX, oldY, currentX, currentY, brushWidth, clientID;
	String color;

	public DrawPath(int oldX, int oldY, int currentX, int currentY, int brushWidth, int clientID, String color) {
		super();
		this.oldX = oldX;
		this.oldY = oldY;
		this.currentX = currentX;
		this.currentY = currentY;
		this.brushWidth = brushWidth;
		this.clientID = clientID;
		this.color = color;
	}

}
