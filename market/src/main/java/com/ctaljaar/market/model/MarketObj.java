package com.ctaljaar.market.model;

import java.io.*;
import java.util.*;
import lombok.Getter;
import lombok.Setter;

import com.ctaljaar.market.model.Instrument;

public class MarketObj{
    ArrayList<Instrument> instruments = new ArrayList<>();
    String[] intsrumentNames = {"Vodacom", "MTN", "LG", "Samsung", "Dell", "IBM"};

    public MarketObj(){
        int i = 1;
        for (String name : intsrumentNames){
            instruments.add(new Instrument(("D" + i++), name));
        }
    }

    public Boolean checkStock(String name, int qty){
        Instrument inst = getInstrument(name);
        return inst.checkStock(qty);
    }

    // public String checkInstrument(String name){
    //     for (Instrument instrument : instruments){
    //         if (instrument.getName().equalsIgnoreCase(name)){
    //             return instrument;
    //         }
    //     }
    //     return null;
    // }

    public Instrument getInstrument(String name){
        for (Instrument instrument : instruments){
            if (instrument.getName().equalsIgnoreCase(name)){
                return instrument;
            }
        }
        return null;
    }

    public ArrayList<String> constructStockList(){
        ArrayList<String> stockList = new ArrayList<>();
        for (Instrument instrument : instruments){
            stockList.add(instrument.toString());
        }
        return stockList;
    }

    public boolean checkInstrument(String name){
        if (getInstrument(name) != null)
            return false;
        return true;
    }

    public boolean checkQuantity(String name, int qty){
        if (checkStock(name, qty))
            return false;
        return true;
    }

    public boolean checkPrice(String type, String name, int price, int qty){
        if (type.equalsIgnoreCase("buy")){
            if (getInstrument(name).getPrice() * qty == price)
                return false;
        }
        else {
            if ((getInstrument(name).getPrice() / 2) * qty == price)
                return false;
        }
        return true;
    }

    public String validateRequest(String type, String name, int qty, int price){
        //validate instrument
        System.out.println();
        if (checkInstrument(name))
            System.out.println("Rejected|Instrument does not exist");
            //return "Rejected|Instrument does not exist";
        //validate quantity
        if (checkQuantity(name, qty))
            System.out.println("Rejected|insufficient quantity");
            //return "Rejected|insufficient quantity";
        //validate price
        if (checkPrice(type, name, price, qty))
            System.out.println("Rejected|Invalid price. Buying: Stock price times quantity. Selling: Stock price divided by 2, times quantity");
            //return "Rejected|Invalid price. Buying: Stock price times quantity. Selling: Stock price divided by 2, times quantity";
        System.out.println("Executed");
        return "Executed";
    }

    public void processRequest(String type, String name, int qty){
        Instrument instrument = getInstrument(name);
        System.out.println(instrument.toString());
        //update market stock
        if (type.equalsIgnoreCase("buy"))
            instrument.sellStock(qty);
        else 
            instrument.buyStock(qty);
        System.out.println(instrument.toString());
    }
}