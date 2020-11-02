package com.ctaljaar.router.market;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Locale;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import com.ctaljaar.router.util.*;
import com.ctaljaar.router.model.RouterGlobals;

/*You have to use Thread to make a new Thread each instance of this class 
because Runnable only makes one Thread
*/
public class MarketThread extends Thread {

	Socket marketSocket;
	ServerSocket marketServerSocket;

	public MarketThread(Socket marketSocket) {
		this.marketSocket = marketSocket;
	}

	@Override
	public void run() {
		try {
			String marketID = RouterUtil.generateID();
			System.out.println("Market joined with ID: " + marketID);
			Connection thisMarketID = new Connection(marketSocket, marketID);
			// Adds this market ID to the online Markets
			RouterGlobals.onlineMarkets.add(thisMarketID);
			BufferedReader marketInput = new BufferedReader(new InputStreamReader(marketSocket.getInputStream()));
			PrintWriter outputStream = new PrintWriter(marketSocket.getOutputStream(), true);
			outputStream.println("ID");
			outputStream.println(marketID);
			String marketMessage;

			while (true) {
				marketMessage = marketInput.readLine();
				if (marketMessage != null) {
					if (marketMessage.contains("Market start")) {
						String marketName = marketInput.readLine();
						RouterGlobals.markets.put(marketName.toLowerCase(), thisMarketID);
					} else if (marketMessage.equalsIgnoreCase("exit")) {
						// Removes this broker from the online brokers
						//MarketThreadUtil.removeMarketFromOnlineList(thisMarket);
						break;
					} 
					if (marketMessage.contains("Market:")){
						String[] request = marketMessage.split("\\*");
						String brokerID = request[3];
						sendToBroker(RouterGlobals.getBrokerSocket(brokerID), request[1] + "*" + request[2]);
					}

					if (marketMessage.contains("End of List")){
						String[] request = marketMessage.split("\\|");
						sendToBroker(RouterGlobals.getBrokerSocket(request[1]), request[0]);
					}

					if (marketMessage.contains("Executed") || marketMessage.contains("Rejected")){
						//send to broker
						System.out.println("Market message: " + marketMessage);
						String[] request = marketMessage.split("\\|");
						if (validateChecksum(request[0] + "|" + request[1] + "|" + request[2], Long.parseLong(request[3])))
							System.out.println("Checksum invalid");
						sendToBroker(RouterGlobals.getBrokerSocket(request[2]), request[0] + "|" + request[1]);
					}
				}  
			}
			marketSocket.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void sendToBroker(Connection broker, String message) throws IOException{
		PrintWriter brokerOutputStream = new PrintWriter(broker.getSocket().getOutputStream(), true);
		brokerOutputStream.println(message);
	}

	public static boolean validateChecksum(String request, long checksum){
        byte[] bytes = request.getBytes();
        Checksum crc32 = new CRC32();
        crc32.update(bytes, 0, bytes.length);
        if (crc32.getValue() == checksum)
            return false;
        return true;
    }
}