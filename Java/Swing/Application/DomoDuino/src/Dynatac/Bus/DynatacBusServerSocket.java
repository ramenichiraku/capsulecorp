package Dynatac.Bus;

import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;
import Dynatac.Bus.IDynatacBus.IDynatacBusListener;


public class DynatacBusServerSocket extends DynatacBusCommon implements IDynatacBusListener,Runnable {

	private class InternalMiniServer extends DynatacBusBase implements Runnable {
		/**
		* Constructor
		*/
		public InternalMiniServer (Socket aSocket) {

//			super("InternalMiniServer");
			socket_ = aSocket;
			
			// Open the streams
			//
			try {
				streamInitializations (socket_.getInputStream(), socket_.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			new Thread (this).start();
		}

		public void run(){
		    while (true)
		    {
		    	dataReady();
		    }
		}
		
		protected void close() {
			try {
				socket_.close();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}

		
		/**
		 * Internal class variables
		 */
		private Socket socket_ = null;
	}

	/**
	* Constructor
	*/
	public DynatacBusServerSocket (int aPort) {
		myPort_ = aPort;
		myBridge_ = new DynatacBusBridge();
		myBridge_.installListener(this);
		
		new Thread (this).start();
	}
	
	public void write(String data) {
		myBridge_.write(data);
	}

	/**
	 * Starts listening a socket
	 * 
	 * @throws IOException
	 */
	private void waitForConnections() throws IOException
	{
		Socket aSocket = listener_.accept();
		
		InternalMiniServer miniServer = new InternalMiniServer(aSocket);
		
		myBridge_.addBus(miniServer);
	}
	
	public void dataAvailable(String data, IDynatacBus bus) {
		notifyListeners (data);
	}
	

	/**
	 * Listener thread
	 */
	public void run() {
		initialize ();
	
		while (true)
		{
			try {
				waitForConnections ();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		//close();
	}

	/**
	 * Common method to initialize
	 */
	protected void initialize() {
		try {
			listener_ = new ServerSocket(myPort_);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Common method to end up
	 */
	protected void close() {
	}
	
	/**
	 * Internal class variables
	 */
	private int 		 myPort_;	
	private ServerSocket listener_;
	
	private DynatacBusBridge myBridge_;
}
