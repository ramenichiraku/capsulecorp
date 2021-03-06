package Dynatac.Bus;

/* java serial access*/
import java.util.List;


import java.util.ArrayList;

public abstract class DynatacBusCommon implements IDynatacBus {
	
	/****************************************
	 *  INHERITATION INTERFACE 						
	 ****************************************/
	//abstract protected void dataReady ();
	
	/****************************************
	 *  OBJECT CONSTRUCTION	
	 ****************************************/
	/**
	* Constructor and finalize method
	*/
	protected DynatacBusCommon ()
	{
	}

	
	/****************************************
	 *  PUBLIC METHODS 						
	 ****************************************/
	public void installListener(IDynatacBusListener s) {
		if (!listeners_.contains(s))
		{
			listeners_.add(s);
			s.onStatusChange(myStatus_);
		}
	}
	
	public boolean removeListener(IDynatacBusListener s) {
		if (!listeners_.contains(s))
		{
			listeners_.remove(s);
			
			return true;
		}
		
		return false;
	}
	
	/****************************************
	 *  INTERNAL METHODS 						
	 ****************************************/
	
	/**
	* Common implementations (suscriptors management)
	*/
	protected void notifyListeners (String line)
	{
		for (int z = 0; z<listeners_.size(); z++)
		{
			IDynatacBusListener s = listeners_.get(z);
					
			s.dataAvailable(line, this);
		}				
	}

	protected void notifyStatus ()
	{
		for (int z = 0; z<listeners_.size(); z++)
		{
			IDynatacBusListener s = listeners_.get(z);
					
			s.onStatusChange(myStatus_);
		}				
	}

	protected void setStatus (int aStatus)
	{
			setStatus (aStatus, false);
	}
	
	protected void setStatus (int aStatus, boolean clearPreviously)
	{
		if (clearPreviously)
		{
			clearStatus(false);
		}

		myStatus_ |= aStatus;
		notifyStatus();
	}
	
	protected void clearStatus ()
	{
		clearStatus (true);
	}

	protected void clearStatus(boolean notify)
	{
		myStatus_ = 0;
		
		if (notify)
		{
			notifyStatus();
		}
	}
	
	int getStatus ()
	{
		return myStatus_;
	}
	
	/*
	protected void clearStatus(int aStatus)
	{
		myStatus_ &= ~aStatus;
	}*/

	protected boolean isSet (int aStatus)
	{
		return ((myStatus_ & aStatus) != 0);
	}


	/****************************************
	 *  INTERNAL VARS 						
	 ****************************************/
	private List<IDynatacBusListener> listeners_ = new ArrayList<IDynatacBusListener>();			
	private int myStatus_ = DYNATAC_BUS_STATUS_NOT_INITIALIZED;


	/**
	* Protected variables inherited by other implementations.
	*/
}
