package com.ctaljaar.broker.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrokerStock{
    protected String instrument;
    protected String quantity;
    protected String price;

    public BrokerStock(String instrument, String quantity, String price){
        this.instrument = instrument;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
	public String toString() {
		return ("Instrument: " + instrument + "\n Quantity: " + quantity + "\n Price: " + price);
	}
}