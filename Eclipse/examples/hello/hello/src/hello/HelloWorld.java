package hello;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import hello.SerialTest.SerialTestSuscriptor;;

public class HelloWorld implements SerialTestSuscriptor {
	
	protected JspWriter myOutput = null;
	protected SerialTest  serial   = null;
	protected boolean     finished = false;
	
	protected HelloWorld () 
	{
		
	}
	
	public HelloWorld (JspWriter o) 
	{
		System.out.println ("Creating HelloWorld constructor.");
		finished = false;
		myOutput = o;	
		
		serial = SerialTest.getSerialTest();
		
		System.out.println ("Got serial object.");
		
		serial.initialize(this);
		System.out.println ("Created HelloWorld constructor.");
	}
	
    public void printTitle(String msg) throws IOException
    {
    	String texto = "<h1>"+msg+"</h1>";
    	
		myOutput.println(texto);

    }
    
    public void printText(String msg) throws IOException
    {
    	String texto = "<p>"+msg+"</p>";
    	
    	myOutput.println(texto);
    }
    
    public boolean isFinished ()
    {
    	return finished;
    }

	@Override
	public void newLine(String s) {
		if (s == "")
		{
			serial.close();
			finished = true;
		}
		else
		{
			try {
				printText (s);
				System.out.println(s); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void connectionError() {
		serial.close();
		finished = true;		
	}
}
