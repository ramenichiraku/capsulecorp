package Dynatac.Bus;

public interface IDynatacBus {

	final static int DYNATAC_BUS_STATUS_NOT_INITIALIZED	 	= 0x00000001;
	
	final static int DYNATAC_BUS_STATUS_WRITE_ERROR			= 0x00000002;

	final static int DYNATAC_BUS_STATUS_READ_ERROR			= 0x00000004;

	final static int DYNATAC_BUS_STATUS_FINISHED			= 0x00000008;

	final static int DYNATAC_BUS_STATUS_STARTING_FAILED		= 0x00000010;

	final static int DYNATAC_BUS_STATUS_ENDING_FAILED		= 0x00000020;

	final static int DYNATAC_BUS_STATUS_CONNECTION_PROBLEM	= 0x00000040;

	final static int DYNATAC_BUS_STATUS_UNAVAILABLE	    	= 
			(
				DYNATAC_BUS_STATUS_NOT_INITIALIZED
				| DYNATAC_BUS_STATUS_WRITE_ERROR
				| DYNATAC_BUS_STATUS_READ_ERROR
				| DYNATAC_BUS_STATUS_FINISHED
				| DYNATAC_BUS_STATUS_STARTING_FAILED
				| DYNATAC_BUS_STATUS_ENDING_FAILED
				| DYNATAC_BUS_STATUS_CONNECTION_PROBLEM
			);
	
	public interface IDynatacBusListener
	{
		abstract public void dataAvailable (String data, IDynatacBus bus);
		
		abstract public void onStatusChange (int busStatus);
	}
	
	public abstract void write(String data);
	
	public abstract void installListener (IDynatacBusListener s);
	
	public abstract boolean removeListener (IDynatacBusListener s);
	
	//public abstract IDynatacBusStatus getStatus();
	
	//public abstract String getErrorDescription();
}
