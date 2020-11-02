package com.ctaljaar.broker.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrokerStock{
    protected String instrument;
    protected String quantity;

    public BrokerStock(String instrument, String quantity){
        this.instrument = instrument;
        this.quantity = quantity;
    }

    @Override
	public String toString() {
		return ("Instrument: " + instrument + "\nQuantity: " + quantity);
	}
}