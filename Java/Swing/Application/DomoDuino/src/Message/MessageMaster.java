package Message;

import java.util.List;

import Frame.IFrame;
import Frame.IFrame.IFrameSuscriptor;

import java.util.ArrayList;

public class MessageMaster implements IMessageMaster, IFrameSuscriptor {
	private List<IMessageMasterSuscriptor> suscriptors_ = new ArrayList<IMessageMasterSuscriptor>();
	
	private IFrame myFrame;
	
	public MessageMaster (IFrame aFrame) {
		myFrame = aFrame;
		
		myFrame.setOnDataAvailable(this);
	}
	
	/* 
	 * IMessageMaster methods 
	 * */
	public void setOnEvent(IMessageMasterSuscriptor subscriptor) {
		if (!suscriptors_.contains(subscriptor))
		{
			suscriptors_.add(subscriptor);
		}
	}	
	
	public void sendCommand(int command, int parameter) {
		String data = "c;"+command+";"+parameter;
		myFrame.write(data);
	}
	
	protected void eventReceived (int myEvent, int myData)
	{
		System.out.println ("New event recieved: "+myEvent+" data: "+myData);
		for (int z = 0; z<suscriptors_.size(); z++)
		{
			IMessageMasterSuscriptor s = suscriptors_.get(z);
			
			s.remoteEvent(myEvent, myData);
		}
	}
	
	/* 
	 * IFrameSuscriptor methods 
	 * */
	public void dataAvailable(String data) {
		int myEvent, dataEvent;
		
		String[] tokens = data.split(";");
		if (tokens.length == 3)
		{
			if (tokens[0].equals("e"))
			{
				myEvent   = Integer.parseInt(tokens[1]);
				dataEvent = Integer.parseInt(tokens[2]);
				
				eventReceived (myEvent, dataEvent);
			}
		
		}
	}
}


