package Serial;

public interface IFrame {
	public interface IFrameSuscriptor
	{
		abstract public void remoteEvent (int eventId, int data);
		
	   //Any number of final, static fields
	   //Any number of abstract method declarations\
	}
	
	abstract public void setOnEvent (IFrameSuscriptor subscriptor);
	
	abstract public boolean sendCommand (int command,int parameter);
}