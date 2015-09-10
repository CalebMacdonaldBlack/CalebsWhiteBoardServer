import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;

public class RunServer {
	
	public RunServer() throws IOException{
		
		BufferedImage in = ImageIO.read(getClass().getResourceAsStream("resources/image.jpg"));
    	BufferedImage newImage = new BufferedImage(
    	    in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);

    	Graphics2D g = newImage.createGraphics();
    	g.drawImage(in, 0, 0, null);
    	g.dispose();
    	
        ServerSocket serverSocket = new ServerSocket(9090);
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                try {
                	ImageIO.write(newImage, "jpg", socket.getOutputStream());
                	System.out.println(newImage);
                } finally {
                    socket.close();
                }
            }
        }
        finally {
        	serverSocket.close();
        }
    }

	
	}
