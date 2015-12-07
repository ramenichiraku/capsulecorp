package Dynatac.Protocol;

import java.util.List;

import Dynatac.Bus.IDynatacBus;
import Dynatac.Bus.IDynatacBus.IDynatacBusListener;
import java.util.ArrayList;

/**
 * Dynatac protocol implementation for both master and slave
 */
public class DynatacProtocol implements IDynatacProtocolMaster, IDynatacProtocolSlave, IDynatacBusListener {
	
	/**
	 * Object construction initializations
	 */
	public DynatacProtocol (IDynatacBus aDynatacBus) {
		myDynatacBus_ = aDynatacBus;
		
		myDynatacBus_.installListener(this);
	}

	/********************************** 
	 *				  *
	 * IDynatacProtocolMaster methods *
	 *				  *
	 *********************************/
	public void setOnEvent(IDynatacProtocolMasterSuscriptor subscriptor) {
		if (!masterSuscriptors_.contains(subscriptor))
		{
			masterSuscriptors_.add(subscriptor);
		}
	}	
	
	public void sendCommand(int command, int parameter) {
		String data = "c;"+command+";"+parameter;
		myDynatacBus_.write(data);
	}

	
	/******************************** 
	 *				*
	 * DynatacProtocolSlave methods *
	 *				*
	 ********************************/
	public void setOnCommand(IDynatacProtocolSlaveSuscriptor subscriptor) {
		if (!slaveSuscriptors_.contains(subscriptor))
		{
			slaveSuscriptors_.add(subscriptor);
		}
	}	
	
	public void sendEvent(int event, int parameter) {
		String data = "e;"+event+";"+parameter;
		myDynatacBus_.write(data);
	}

	/***************************************** 
	 *                                       *
	 * IDynatacBusListener inherited methods * 
	 *                                       *
	 *****************************************/

	/** 
	 * Implemetation for IDynatacBusListener methods 
	 *
	 * @param1 receibed data
	 * @param2 bus where data comes from
	 *
	 * */
	public void dataAvailable(String data, IDynatacBus bus) {
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
	
	@Override
	public void onStatusChange(int busStatus) {
		
		
	}
	
	/********************************
	 *				*
	 * Private methods 		*
	 *				*
	 ********************************/
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


	/**
	 * Private vars
	 */
	private List<IDynatacProtocolMasterSuscriptor> masterSuscriptors_ = new ArrayList<IDynatacProtocolMasterSuscriptor>();
	private List<IDynatacProtocolSlaveSuscriptor>   slaveSuscriptors_ = new ArrayList<IDynatacProtocolSlaveSuscriptor>();
	
	private IDynatacBus myDynatacBus_;
}


