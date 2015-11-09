package Message;

public interface IMessageSlave {
	
	public interface IMessageSlaveSuscriptor {
		abstract public void commandAvailable (String data);
	}
	
	abstract public void setOnCommand (IMessageSlaveSuscriptor subscriptor);
	
	abstract public void sendEvent (int aEvent, int aData);
}
