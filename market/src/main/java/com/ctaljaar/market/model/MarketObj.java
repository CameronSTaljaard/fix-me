package com.ctaljaar.market.model;

import java.io.*;
import java.util.*;
import com.ctaljaar.market.model.Instrument;

public class MarketObj{
    ArrayList<Instrument> instruments = new ArrayList<>();
    protected String marketID;
    String[] intsrumentNames = {"Vodacom", "MTN", "LG", "Samsung", "Dell", "IBM"};

    public MarketObj(String uniqueID){
        this.marketID = uniqueID;
        int i = 1;
        for (String name : intsrumentNames){
            instruments.add(new Instrument(("D" + i++), name));
        }
    }

    public Boolean checkStock(String id, int qty){
        Instrument inst = getInstrument(id);
        return inst.checkStock(qty);
    }

    public Instrument getInstrument(String id){
        for (int x = 0; x < instruments.size(); x++){
            if (instruments.get(x).getID().equals(id)){
                return instruments.get(x);
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
}