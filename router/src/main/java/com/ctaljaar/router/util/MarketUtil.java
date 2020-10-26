package com.ctaljaar.router.util;

public class MarketUtil {

	protected int balance;
	protected String stockName;
	protected String uniqueID;
	protected int quantity;
	protected int buyPrice;
	protected int sellPrice;
	protected String marketName;

	public MarketUtil(String uniqueID, String marketName, String stockName) {
		this.quantity = 50;
		this.buyPrice = 100;
		this.sellPrice = 50;
		this.marketName = marketName;
		this.stockName = stockName;
		this.uniqueID = uniqueID;
	}

	@Override
	public String toString() {
		return (marketName + "\nID: " + uniqueID + "\n" + "\n" + stockName + "\nBuy: " + buyPrice + "\nSell: "
				+ sellPrice + "\nQuantity: " + String.valueOf(quantity) + "\n");
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
