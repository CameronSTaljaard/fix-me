package com.ctaljaar.router.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.InputStreamReader;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import java.net.Socket;

import com.ctaljaar.router.model.RouterGlobals;
import com.ctaljaar.router.broker.BrokerThread;

public class BrokerThreadUtil {

    public static boolean validateChecksum(String request, long checksum){
        byte[] bytes = request.getBytes();
        Checksum crc32 = new CRC32();
        crc32.update(bytes, 0, bytes.length);
        if (crc32.getValue() == checksum)
            return false;
        return true;
    }

    public static void checkBrokerMessage(String brokerMessage, PrintWriter outputStream, BufferedReader brokerInput, String brokerThreadID) throws IOException {
        System.out.println("Broker message = " + brokerMessage);

        if (brokerMessage.contains("markets")){
            // Ask market for a stocklist
            int i = 1;
            for (Connection market : RouterGlobals.onlineMarkets){
                if (i == RouterGlobals.onlineMarkets.size())
                    sendToMarket(brokerMessage + "|" + "End of List", market);
                else
                    sendToMarket(brokerMessage, market);
                i++;
            }
        }

        if (brokerMessage.contains("buy") || brokerMessage.contains("sell")){
                
                //validate checksum
                String[] request = brokerMessage.split("\\|");
                if (validateChecksum(brokerMessage.substring(0, brokerMessage.length() - (request[6].length() + 1)), Long.parseLong(request[6]))){
                    System.out.println("Invalid Checksum");
                    //send error to broker
                }
                Socket market = RouterGlobals.markets.get(request[4].toLowerCase()).getSocket();
                if (market.getInetAddress().isReachable(5))
                    outputStream.println("Rejected market does not exist");
                if  (RouterGlobals.markets.get(request[4].toLowerCase()) == null){
                    outputStream.println("Rejected market does not exist");
                }
                else {
                    //send request to market
                    sendToMarket(brokerMessage, RouterGlobals.markets.get(request[4].toLowerCase()));
                }
        }
    }

    private static void sendToMarket(String brokerMessage, Connection market) throws IOException{
        PrintWriter marketOutputStream = new PrintWriter(market.getSocket().getOutputStream(), true);
        BufferedReader marketInput = new BufferedReader(new InputStreamReader(market.getSocket().getInputStream()));
        marketOutputStream.println(brokerMessage);
    }


    // static void removeBrokerFromOnlineList(Connection thisBroker) {
    //     int index = RouterGlobals.onlineBrokers.indexOf(thisBroker);
    //     RouterGlobals.onlineBrokers.remove(index);
    //     System.out.println(RouterGlobals.onlineBrokers);
    // }
}
