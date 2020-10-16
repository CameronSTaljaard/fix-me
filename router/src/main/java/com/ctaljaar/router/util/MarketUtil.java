package com.ctaljaar.router.util;

public class MarketUtil {

	protected int balance;
	protected String stockName;
	protected String uniqueID;
	protected int quantity;

	public MarketUtil(String uniqueID, String stockName) {
		this.balance = 5000;
		this.quantity = 50;
		this.stockName = stockName;
		this.uniqueID = uniqueID;
	}

	@Override
	public String toString() {
		return ("Market:" + "\nID: " + uniqueID + "\nBalance: " + balance + "\n" + stockName
				+ "\nBuy: 100 \nSell: 50 \nQuantity: " + String.valueOf(quantity));
	}

	public String getStock() {
		return stockName;
	}

	public void updateQuanity(int quantity, String action) {
		if (action.equalsIgnoreCase("Action: Buy")) {
			this.quantity -= quantity;
		} else if (action.equalsIgnoreCase("Action: Sell")) {
			this.quantity += quantity;
		}
	}
}
