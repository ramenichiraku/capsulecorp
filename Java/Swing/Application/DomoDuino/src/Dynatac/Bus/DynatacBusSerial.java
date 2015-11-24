package Dynatac.Bus;

/* java serial access*/
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener;


//import java.util.Enumeration;
import java.util.List;


import java.util.ArrayList;

public class DynatacBusSerial implements IDynatacBus, SerialPortEventListener {
	/// Constructor
	String myPort_;

	public DynatacBusSerial (String port)
	{
		myPort_ = port;

		initialize();
		System.out.println("SerialManager created with port" + myPort_);
	}

	protected void finalize( ) //throws Throwable   
	{
	 	close();
	}

	
	///////////////////////////////////////////////////	
	/// Internal variables
	private List<IDynatacBusSuscriptor> suscriptors_ = new ArrayList<IDynatacBusSuscriptor>();			
	private SerialPort serialPort;
	
	/**
	* A BufferedReader which will be fed by a InputStreamReader 
	* converting the bytes into characters 
	* making the displayed results codepage independent
	*/
	
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private final int DATA_RATE = 9600;

	
    /** The port we're normally going to use. */
    /*
	private static final String PORT_NAMES[] = { 
			"/dev/tty.usbserial-A9007UX1", // Mac OS X
			"/dev/ttyACM0", // Raspberry Pi
			"/dev/ttyUSB0", // Linux
			"COM3", // Windows
			};
    */
			
	/// Internal methods

	/* 
	 * SerialPortEventListener methods 
	 * */
	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine=input.readLine();
				
				for (int z = 0; z<suscriptors_.size(); z++)
				{
					IDynatacBusSuscriptor s = suscriptors_.get(z);
					
					s.dataAvailable(inputLine);
				}				
			} catch (Exception e) {
				System.err.println(e.toString());
				//aSuscriptor.connectionError();
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}
	
	public void write(String data) {
		System.out.println("Sent: " + data);
		try {
			output.write(data.getBytes());
		} catch (Exception e) {
			System.out.println("could not write to port");
			e.printStackTrace();
		}
	}
	
	public void setOnDataAvailable(IDynatacBusSuscriptor s) {
		if (!suscriptors_.contains(s))
		{
			suscriptors_.add(s);
		}
	}

	public static List<String> getDetectedPorts () {
		List<String> names = new ArrayList<String>();
	
		@SuppressWarnings("unchecked")
		java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
			
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = portEnum.nextElement();

			String portName = currPortId.getName();

			names.add (portName);
		}

		return names;
	}
	

	
	/**
	 * Public methods, all static because it is a singleton 
	 * 
	 */
	private void initialize() {
/*
        // the next line is for Raspberry Pi and 
        // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
        System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");
*/
		
		CommPortIdentifier portId = null;
		//Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
		@SuppressWarnings("unchecked")
		java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();

		System.out.println ("Trying to open port " + myPort_);
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = portEnum.nextElement();
			if (currPortId.getName().equals(myPort_))
			{
				portId = currPortId;
				System.out.println("COM port found:."+myPort_);
				break;
			}
		}

		if (portId == null) {
			System.err.println("Could not find COM port.");
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		
		System.out.println("Initialized");
	}
	
	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	private synchronized void close() {
		System.out.println("Closing serial port.");;
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

}
