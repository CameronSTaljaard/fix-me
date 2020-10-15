package com.ctaljaar.router.util;

public class MarketUtil {

	protected int balance;
	protected String stockName;
	protected String uniqueID;

	public MarketUtil(String uniqueID,String stockName) {
		this.balance = 5000;
		this.stockName = stockName;
		this.uniqueID = uniqueID;
	}

	@Override
	public String toString() {
		return ("Market:" + "\nID: " + uniqueID + "\nBalance: " + balance + "\n" + stockName + "\nBuy: 100 \nSell: 50 \nQuantity: 50");
	}

	public String getStock(){
		return stockName;
	}
	
}
