package Dynatac.Bus;

/* java serial access*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;


import java.util.ArrayList;

public abstract class DynatacBusCommon implements IDynatacBus {
	
	/****************************************
	 *  INHERITATION INTERFACE 						
	 ****************************************/
	abstract protected void dataAvailable ();
	
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
	public void setOnDataAvailable(IDynatacBusSuscriptor s) {
		if (!suscriptors_.contains(s))
		{
			suscriptors_.add(s);
		}
	}
	
	/****************************************
	 *  INTERNAL METHODS 						
	 ****************************************/
	

	/**
	* Common implementations (suscriptors management)
	*/
	private void notifySuscriptors (String line)
	{
		for (int z = 0; z<suscriptors_.size(); z++)
		{
			IDynatacBusSuscriptor s = suscriptors_.get(z);
					
			s.dataAvailable(line);
		}				
	}


	/****************************************
	 *  INTERNAL VARS 						
	 ****************************************/
	private List<IDynatacBusSuscriptor> suscriptors_ = new ArrayList<IDynatacBusSuscriptor>();			

	/**
	* Protected variables inherited by other implementations.
	*/

}