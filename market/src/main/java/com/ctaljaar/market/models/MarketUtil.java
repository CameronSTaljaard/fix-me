package com.ctaljaar.market.models;

public class MarketUtil {

	protected int balance;
	protected int stock;
	protected String uniqueID;

	public MarketUtil(String uniqueID) {
		this.balance = 5000;
		this.stock = 100;
		this.uniqueID = uniqueID;
	}

	@Override
	public String toString() {
		return ("Market:" + "\nID: " + uniqueID + "\nBalance: " + balance + "\nStock: " + stock);
	}
}
