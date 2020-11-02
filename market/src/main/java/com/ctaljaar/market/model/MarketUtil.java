package com.ctaljaar.market.model;

import java.util.*;
import java.io.*;
import java.util.ArrayList;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

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

    public static long calculateChecksum(String request){
        byte[] bytes = request.getBytes();
        Checksum crc32 = new CRC32();
        crc32.update(bytes, 0, bytes.length);
        return crc32.getValue();
    }
}