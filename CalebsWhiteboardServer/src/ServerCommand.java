
public class ServerCommand extends Packet{

	private String commandText;

	public ServerCommand(String commandText) {
		this.commandText = commandText;
	}
	
	public ServerCommand getCommand(){
		return this;
		
	}
	
	public String toString(){
		return commandText;
	}

}
