package com.ctaljaar.market.model;

import java.util.*;
import java.io.*;
import java.util.ArrayList;

import com.ctaljaar.market.model.MarketObj;

public class MarketUtil{
    public static String setMarketID(BufferedReader inputStream) throws Exception{
        String readLine;
        readLine = inputStream.readLine();
		if (readLine.equalsIgnoreCase("ID")){
				readLine = inputStream.readLine();
				System.out.println("ID: " + readLine);
				return readLine;
		}
        return null;
    }

    public static String marketStart(PrintWriter outputStream, BufferedReader terminalInput) throws Exception{
        String readLine;
        outputStream.println("Market start");
		System.out.println("What is the market name?");
		readLine = terminalInput.readLine();
		outputStream.println("Market: " + readLine);
        return readLine;
    }
}