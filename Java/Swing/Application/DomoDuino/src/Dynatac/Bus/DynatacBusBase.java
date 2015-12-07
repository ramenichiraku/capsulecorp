package Dynatac.Bus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * 
 * @author elotro
 *
 */
public abstract class DynatacBusBase extends DynatacBusCommon implements IDynatacBus {
	
	/****************************************
	 *  INHERITATION INTERFACE 						
	 ****************************************/
	/**
	* Methods implemented by the different implementations
	*/
	//protected abstract void initialize();

	protected abstract void close();
	
	/****************************************
	 *  OBJECT CONSTRUCTION	
	 ****************************************/
	/**
	* Constructor and finalize method
	*/
	protected DynatacBusBase ()
	{
	}

	protected void finalize() //throws Throwable   
	{
	 	close();
	}
	
	
	/****************************************
	 *  PUBLIC METHODS 						
	 ****************************************/
	public void write(String data) {
		System.out.println("Sent: " + data);
		try {
			//output_.write(data.getBytes());
			output_.println (data);
		} catch (RuntimeException e) {

			System.err.println("Could not write to output buffer.");
			//e.printStackTrace();
			System.err.println(e.getMessage());

			setStatus(DYNATAC_BUS_STATUS_WRITE_ERROR);
		}
	}
	
	/****************************************
	 *  INTERNAL METHODS 						
	 ****************************************/
	
	/**
	 * This method is called by specific bus and read, base dynatac bus will perform a common read line
	 *
	 * @return false if received null from input line: it means connection has been closed
	 */
	protected boolean dataReady ()
	{
		if (isSet (DYNATAC_BUS_STATUS_NOT_INITIALIZED))
		{
			return true;
		}

		// Read buffered line
		//
		String inputLine;
		try {
			inputLine = input_.readLine();
			
			if (inputLine == null)
			{
//				System.err.println ("");
				setStatus(DYNATAC_BUS_STATUS_FINISHED);
				return false;
			}
			else
			{
				notifyListeners (inputLine);
			}
		} catch (IOException e) {
			System.err.println ("Could not read from buffer");
			System.err.println (e.toString());
			//e.printStackTrace();

			setStatus(DYNATAC_BUS_STATUS_READ_ERROR);
		}
		
		return true;
	}
	
	/**
	 * Once the specific bus has a input/output bus must call this method to initialize the base bus
	 * 
	 * @param input stream
	 * @param output stream
	 */
	protected void streamInitializations (InputStream input, OutputStream output)
	{
		output_ = new PrintStream (output);
		input_  = new BufferedReader(new InputStreamReader(input));
		
		clearStatus(DYNATAC_BUS_STATUS_NOT_INITIALIZED);
	}



	/****************************************
	 *  INTERNAL VARS 						
	 ****************************************/

	/**
	* Protected variables inherited by other implementations.
	*/

	/**
	* A BufferedReader which will be fed by a InputStreamReader 
	* converting the bytes into characters 
	* making the displayed results codepage independent
	*/
	private BufferedReader   input_  = null;
	/** The output stream to the port */
	//private OutputStream 	 output_ = null;
	private PrintStream 	 output_ = null;

//	private String errorDescription_    = ;
}
