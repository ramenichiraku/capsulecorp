package Dynatac.Bus;

import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;


public class DynatacBusServerSocket implements IDynatacBus, Runnable {
	private List<IDynatacBusSuscriptor > suscriptors_ = new ArrayList<IDynatacBusSuscriptor>();
	private int myPort_;
	
	private boolean serverIsStarted_;
	private ServerSocket listener_;
	private Socket socket_;
	
	private BufferedReader inputBuffer_ = null;
	private PrintStream    outputBuffer_ = null;
	
	
	/// Constructor
	public DynatacBusServerSocket (int aPort) {
		myPort_ = aPort;
		new Thread (this).start();
	}

	private void startServer (int myPort) throws IOException
	{
		listener_ = new ServerSocket(myPort);
		socket_   = listener_.accept();		
		
		inputBuffer_ = new BufferedReader(new InputStreamReader(socket_.getInputStream()));
		outputBuffer_= new PrintStream(socket_.getOutputStream());
		serverIsStarted_ = true;
	}
	
	private void stopServer () throws IOException
	{
		serverIsStarted_ = false;
		socket_.close();
		listener_.close();
	}
	
	/* IDynatacBus methods*/
	public void write(String data) {
		System.out.println("Sent: " + data);
		outputBuffer_.println(data);
	}

	public void setOnDataAvailable(IDynatacBusSuscriptor s) {
		if (!suscriptors_.contains(s))
		{
			suscriptors_.add(s);
		}
	}

	protected void notifySuscriptors (String data)
	{
		for (int z = 0; z<suscriptors_.size(); z++)
		{
			IDynatacBusSuscriptor s = suscriptors_.get(z);
			
			s.dataAvailable(data);
		}
	}

	/* Runnable methods */
	public void run() {
		try {
			startServer (myPort_);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		
		while (serverIsStarted_)
		{
			while (inputBuffer_ == null)
			{
				System.out.println("no input buffer...");
				
			}
			try {
				String line = null;
				line = inputBuffer_.readLine();
				notifySuscriptors (line);
				System.out.println("New line received: " + line);
			} catch (IOException e) {
				
				e.printStackTrace();
			}	
		}
		
		try {
			stopServer();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}