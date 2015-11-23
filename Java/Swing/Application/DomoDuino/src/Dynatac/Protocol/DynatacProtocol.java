package Dynatac.Protocol;

import java.util.List;

import Dynatac.Bus.IDynatacBus;
import Dynatac.Bus.IDynatacBus.IDynatacBusSuscriptor;
import java.util.ArrayList;

public class DynatacProtocol implements IDynatacProtocolMaster, IDynatacProtocolSlave, IDynatacBusSuscriptor {
	private List<IDynatacProtocolMasterSuscriptor> masterSuscriptors_ = new ArrayList<IDynatacProtocolMasterSuscriptor>();
	private List<IDynatacProtocolSlaveSuscriptor>   slaveSuscriptors_ = new ArrayList<IDynatacProtocolSlaveSuscriptor>();
	
	private IDynatacBus myDynatacBus;
	
	public DynatacProtocol (IDynatacBus aDynatacBus) {
		myDynatacBus = aDynatacBus;
		
		myDynatacBus.setOnDataAvailable(this);
	}
	
	/* 
	 * DynatacProtocolMaster methods 
	 * */
	public void setOnEvent(IDynatacProtocolMasterSuscriptor subscriptor) {
		if (!masterSuscriptors_.contains(subscriptor))
		{
			masterSuscriptors_.add(subscriptor);
		}
	}	
	
	public void sendCommand(int command, int parameter) {
		String data = "c;"+command+";"+parameter;
		myDynatacBus.write(data);
	}

	
	/* 
	 * DynatacProtocolSlave methods 
	 * */
	public void setOnCommand(IDynatacProtocolSlaveSuscriptor subscriptor) {
		if (!slaveSuscriptors_.contains(subscriptor))
		{
			slaveSuscriptors_.add(subscriptor);
		}
	}	
	
	public void sendEvent(int event, int parameter) {
		String data = "e;"+event+";"+parameter;
		myDynatacBus.write(data);
	}

	
	protected void eventReceived (int myEvent, int myData)
	{
		System.out.println ("New event recieved: "+myEvent+" data: "+myData);
		for (int z = 0; z<masterSuscriptors_.size(); z++)
		{
			IDynatacProtocolMasterSuscriptor s = masterSuscriptors_.get(z);
			
			s.remoteEvent(myEvent, myData);
		}
	}

	protected void commandReceived (int myCommand, int myData)
	{
		System.out.println ("New event command: "+myCommand+" data: "+myData);
		for (int z = 0; z<slaveSuscriptors_.size(); z++)
		{
			IDynatacProtocolSlaveSuscriptor s = slaveSuscriptors_.get(z);
			
			s.commandAvailable(myCommand, myData);
		}
	}

	
	/* 
	 * IDynatacBusSuscriptor methods 
	 * */
	public void dataAvailable(String data) {
		int myCommand, myEvent, dataEvent;
		
		String[] tokens = data.split(";");
		if (tokens.length == 3)
		{
			if (tokens[0].equals("e"))
			{
				myEvent   = Integer.parseInt(tokens[1]);
				dataEvent = Integer.parseInt(tokens[2]);
				
				eventReceived (myEvent, dataEvent);
			}
			else if (tokens[0].equals("c"))
			{
				myCommand = Integer.parseInt(tokens[1]);
				dataEvent = Integer.parseInt(tokens[2]);
				
				commandReceived (myCommand, dataEvent);
			}
		}
	}
}

