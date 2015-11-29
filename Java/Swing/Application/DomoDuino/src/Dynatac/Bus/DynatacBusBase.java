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
		} catch (Exception e) {
			System.err.println("Could not write to output buffer.");
			e.printStackTrace();
			//assert(0);
		}
	}
	
	/****************************************
	 *  INTERNAL METHODS 						
	 ****************************************/
	
	/**
	 * This method is called by specific bus and read, base dynatac bus will perform a common read line
	 */
	protected void dataAvailable ()
	{
		// Read buffered line
		//
		try {
			String inputLine = input_.readLine();

			notifySuscriptors (inputLine);			
		} catch (IOException e1) {
			e1.printStackTrace();
			System.err.println(e1.toString());
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.toString());
		}
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
}
