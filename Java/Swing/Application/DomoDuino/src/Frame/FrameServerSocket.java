package Frame;

import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.PrintWriter;



public class FrameServerSocket implements IFrame, Runnable {
	private List<IFrameSuscriptor > suscriptors_ = new ArrayList<IFrameSuscriptor>();
	
	/// Constructor
	private ServerSocket listener_;
	private Socket socket_;
	
	BufferedReader inputBuffer_;
    PrintWriter    outputBuffer_;
	
	void open (int port) throws IOException
	{
		listener_ = new ServerSocket(port);
		socket_   = listener_.accept();		
		
		inputBuffer_ = new BufferedReader(new InputStreamReader(socket_.getInputStream()));
		outputBuffer_= new PrintWriter(socket_.getOutputStream(), true);
	}
	
	void close () throws IOException
	{
		socket_.close();
		listener_.close();
	}
	/* IFrame methods*/

	public void write(String data) {
		outputBuffer_.write(data);
	}

	public void setOnDataAvailable(IFrameSuscriptor s) {
		if (!suscriptors_.contains(s))
		{
			suscriptors_.add(s);
		}
	}

	protected void notifySuscriptors (String data)
	{
		for (int z = 0; z<suscriptors_.size(); z++)
		{
			IFrameSuscriptor s = suscriptors_.get(z);
			
			s.dataAvailable(data);
		}
	}

	public void run() {
		while (true)
		{
			try {
				String line = null;
				line = inputBuffer_.readLine();
				notifySuscriptors (line);
			} catch (IOException e) {
				
				e.printStackTrace();
			}	
		}
	}
}