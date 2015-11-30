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


	/****************************************
	 *  INTERNAL VARS 						
	 ****************************************/
	private List<IDynatacBusListener> listeners_ = new ArrayList<IDynatacBusListener>();			

	/**
	* Protected variables inherited by other implementations.
	*/
}
