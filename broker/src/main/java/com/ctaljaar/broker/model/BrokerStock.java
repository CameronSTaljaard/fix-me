package com.ctaljaar.broker.model;

public class BrokerStock{
    protected static String instrument;
    protected static String quantity;
    protected static String price;

    public BrokerStock(String instrument, String quantity, String price){
        this.instrument = instrument;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
	public String toString() {
		return ("Instrument: " + instrument + " Quantity: " + quantity + " Price: " + price);
	}

    public String getBrokerStockPrice(){
        return this.price;
    }

    public String getBrokerStockQuantity(){
        return this.quantity;
    }

    public String getBrokerStockInstrument(){
        return this.instrument;
    }

    public void setBrokerStockPrice(String price){
        this.price = price;
    }

    public void setBrokerStockQuantity(String quantity){
        this.quantity = quantity;
    }
}