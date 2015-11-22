package Dynatac.Bus;

public interface IDynatacBus {
	public interface IDynatacBusSuscriptor
	{
		abstract public void dataAvailable (String data);
	}
	
	public abstract void write(String data);
	
	public abstract void setOnDataAvailable (IDynatacBusSuscriptor s);
}
