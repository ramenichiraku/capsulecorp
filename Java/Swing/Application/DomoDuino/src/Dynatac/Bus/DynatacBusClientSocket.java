package Dynatac.Bus;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.PrintStream;



public class DynatacBusClientSocket implements IDynatacBus, Runnable {
	private List<IDynatacBusSuscriptor > suscriptors_ = new ArrayList<IDynatacBusSuscriptor>();
	private boolean connected_;
	
	String remoteAddr_ = "";
	int remotePort_ = 0;
	
	private Socket socket_ = null;
	private BufferedReader input_  = null;
	private PrintStream    output_ = null;
	
	public DynatacBusClientSocket (String addr, int port) {
		remoteAddr_ = addr;
		remotePort_ = port;
		connected_ = false;
		
		new Thread (this).start();
	}
	


	private void connect (String addr, int port) throws IOException
	{
		socket_ = new Socket (addr, port);
		input_  = new BufferedReader(new InputStreamReader(socket_.getInputStream()));
		output_ = new PrintStream(socket_.getOutputStream()); 		

		connected_ = true;
	}
	
	private void disconnect () throws IOException
	{
		input_  = null;
		output_ = null;
		if (socket_ != null)
		{
			socket_.close();
			socket_ = null;
		}

		connected_ = false; 
	}
	

	/* IDynatacBus methods*/
	public void write(String data) {
		if (connected_)
		{
			output_.println(data);
		}
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

	public void run() {
		try {
			connect (remoteAddr_, remotePort_);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		while (true)
		{
			if (connected_)
			{
				// if available data, notify suscriptors
				try {
					String line = null;
					line = input_.readLine();
					notifySuscriptors (line);
				} catch (IOException e) {
					
					e.printStackTrace();
				}	
			}
			else
			{
				try {
					disconnect();
					connect (remoteAddr_, remotePort_);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		//disconnect();
		
	}

}