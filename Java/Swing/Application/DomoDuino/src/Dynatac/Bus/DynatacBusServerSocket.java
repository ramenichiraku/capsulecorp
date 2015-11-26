package Dynatac.Bus;

import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;


public class DynatacBusServerSocket extends DynatacBusBase implements Runnable {
	
	/**
	* Constructor
	*/
	public DynatacBusServerSocket (int aPort) {
		myPort_ = aPort;
		
		new Thread (this).start();
	}

	/**
	 * Starts listening a socket
	 * 
	 * @throws IOException
	 */
	private void startServer () throws IOException
	{
		listener_ = new ServerSocket(myPort_);
		socket_   = listener_.accept();		
		
		// Initialize base class buffers
		//
		streamInitializations (socket_.getInputStream(), socket_.getOutputStream());
		
		serverIsStarted_ = true;
	}
	
	/**
	 * Stops socket management
	 * 
	 * @throws IOException
	 */
	private void stopServer () throws IOException
	{
		serverIsStarted_ = false;
		socket_.close();
		listener_.close();
	}

	/**
	 * Listener thread
	 */
	public void run() {
		initialize ();
	
		while (serverIsStarted_)
		{
			dataAvailable();
		}
		
		close();
	}

	/* TBD: This method can be removed... */
	/**
	 * Common method to initialize
	 */
	protected void initialize() {
		try {
			startServer ();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Common method to end up
	 */
	protected void close() {
		try {
			stopServer();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
	
	/**
	 * Internal class variables
	 */
	private int 		 myPort_;	
	private boolean 	 serverIsStarted_;
	private ServerSocket listener_;
	private Socket 		 socket_;
}
