package com.ctaljaar.market.model;

import java.util.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Instrument {
    protected String id;
    protected String name;
    protected int price;
    protected int stock;
    Random rand = new Random();

    public Instrument(String id, String name){
        this.id = id;
        this.name = name;
        this.price = randPrice();
        this.stock = randStock();
    }

    @Override
	public String toString() {
		return ("Product ID: " + id + " Name: " + name + " Price: " + price + " Available stock: " + stock);
	}

    private int randPrice(){
        return rand.nextInt(150) + 1;
    }

    private int randStock(){
        return rand.nextInt(50) + 1;
    }

    public void sellStock(int sold){
        this.stock -= sold;
    }

    public void buyStock(int buy){
        this.stock += buy;
    }

    public Boolean checkStock(int need){
        if (stock >= need)
            return true;
        return false;
    }
}