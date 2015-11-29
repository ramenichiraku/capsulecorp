package Dynatac.Bus;

import java.util.ArrayList;
import java.util.List;

import Dynatac.Bus.IDynatacBus.IDynatacBusSuscriptor;;


public class DynatacBusBridge extends DynatacBusCommon implements IDynatacBusSuscriptor {
	
	/**
	 * 
	 * 
	 *
	 */
	class DynatacBusBridge_DynatacBusSuscriptor implements IDynatacBusSuscriptor {
		
		private IDynatacBus dataDestinationBus_ = null;
		private IDynatacBus dataOriginBus_ = null;
		
		DynatacBusBridge_DynatacBusSuscriptor (IDynatacBus dataOriginBus, IDynatacBus dataDestinationBus) {
			dataDestinationBus_ = dataDestinationBus;
			dataOriginBus_ = dataOriginBus;
			
			dataOriginBus_.setOnDataAvailable(this);
		}
		
		public void dataAvailable(String data) {
			dataDestinationBus_.write(data);
		}
	}	
	
	/****************************************
	 *  OBJECT CONSTRUCTION	
	 ****************************************/
	public DynatacBusBridge (IDynatacBus aBusLeft, IDynatacBus aBusRight) {
		
		busLeft_  = aBusLeft;
		busRight_ = aBusRight;
		
		busLeftSuscriptor_  = new  DynatacBusBridge_DynatacBusSuscriptor(busLeft_,busRight_);
		busRightSuscriptor_ = new  DynatacBusBridge_DynatacBusSuscriptor(busRight_,busLeft_);
		
		busLeft_.setOnDataAvailable(this);
		busRight_.setOnDataAvailable(this);
	}
	
	/****************************************
	 *  PUBLIC METHODS 						
	 ****************************************/
	public void write(String data) {
		busLeft_.write(data);
		busRight_.write(data);
	}
	
	public void dataAvailable(String data) {
		notifySuscriptors (data);				
	}
		
	/****************************************
	 *  INTERNAL METHODS 						
	 ****************************************/
	
	/****************************************
	 *  INTERNAL VARS 						
	 ****************************************/
	protected DynatacBusBridge_DynatacBusSuscriptor busLeftSuscriptor_ = null;
	protected DynatacBusBridge_DynatacBusSuscriptor busRightSuscriptor_ = null;
	private IDynatacBus busLeft_, busRight_;
}
	
