package Dynatac.Bus;

public interface IDynatacBus {
	public interface IDynatacBusListener
	{
		abstract public void dataAvailable (String data, IDynatacBus bus);
	}
	
	public abstract void write(String data);
	
	public abstract void installListener (IDynatacBusListener s);
	
	public abstract boolean removeListener (IDynatacBusListener s);
}
