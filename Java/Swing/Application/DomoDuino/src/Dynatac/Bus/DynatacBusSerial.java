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
	/** 
	 * Singleton pattern and protected constructor
	 * */
	private static DynatacBusSerial instance_ = null;
	protected DynatacBusSerial ()
	{
		initialize ();
		System.out.println("SerialManager created");
	}
	public static DynatacBusSerial getInstance () {
		if (instance_ == null)
		{
			instance_ = new DynatacBusSerial();
		}
		
		return instance_;
	}
	
	///////////////////////////////////////////////////	
	/// Internal variables
	private List<IDynatacBusSuscriptor> suscriptors_ = new ArrayList<IDynatacBusSuscriptor>();			
	private static SerialPort serialPort;
	
	/**
	* A BufferedReader which will be fed by a InputStreamReader 
	* converting the bytes into characters 
	* making the displayed results codepage independent
	*/
	
	private static BufferedReader input;
	/** The output stream to the port */
	private static OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;

	
    /** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { 
			"/dev/tty.usbserial-A9007UX1", // Mac OS X
			"/dev/ttyACM0", // Raspberry Pi
			"/dev/ttyUSB0", // Linux
			"COM3", // Windows
			};
			
	/// Internal methods

	/* 
	 * SerialPortEventListener methods 
	 * */
	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
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
	

	
	/**
	 * Public methods, all static because it is a singleton 
	 * 
	 */
	private synchronized void initialize() {
/*
        // the next line is for Raspberry Pi and 
        // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
        System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");
*/
		
		CommPortIdentifier portId = null;
		//Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
		java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();

		System.out.println ("Start looking for ports..");
		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = portEnum.nextElement();
			System.out.println ("Expected name: "+currPortId);
			for (String portName : PORT_NAMES) {
				System.out.println("Trying port name: " + portName);
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					System.out.println("COM port found:."+portName);
					break;
				}
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
	/*
	private synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}
	*/	

}
