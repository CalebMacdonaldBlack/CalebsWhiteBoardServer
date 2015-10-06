import java.io.Serializable;

public class ServerCommand extends Packet implements Serializable{

	private String commandText;

	public ServerCommand(String commandText) {
		this.commandText = commandText;
	}
	
	public String getCommand(){
		return commandText;
		
	}

}
