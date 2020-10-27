package com.ctaljaar.router.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.InputStreamReader;
// import java.io.InputStreamReader;

import com.ctaljaar.router.model.RouterGlobals;
import com.ctaljaar.router.broker.BrokerThread;

public class BrokerThreadUtil {

    public static void checkBrokerMessage(String brokerMessage, PrintWriter outputStream, BufferedReader brokerInput, String brokerThreadID) throws IOException {
            if (brokerMessage.equalsIgnoreCase("markets")){
                for (Connection market : RouterGlobals.onlineMarkets) {
                    PrintWriter marketOutputStream = new PrintWriter(market.getSocket().getOutputStream(), true);
                    BufferedReader marketInput = new BufferedReader(new InputStreamReader(market.getSocket().getInputStream()));
                    // Send the data to the Broker
                    marketOutputStream.println("markets");
                }
            }
        System.out.println("Broker message = " + brokerMessage);

        // if (brokerMessage.equalsIgnoreCase("BrokerID")) {
        //     System.out.println(brokerThreadID);
        //     outputStream.println(brokerThreadID);
        // } else if (brokerMessage.equalsIgnoreCase("list")) {
        //     for (Connection connection : RouterGlobals.onlineBrokers) {
        //         // Send the data to the Broker
        //         outputStream.println("Broker: " + connection);
        //     }
        //     // Sends this to the Broker if all the Online Brokers have been sent
        //     outputStream.println("End of list");
        // } else if (brokerMessage.equalsIgnoreCase("markets")) {
        //     for (MarketUtil markets : RouterGlobals.onlineMarketsInfo) {
        //         // Send the data to the Broker
        //         outputStream.println(markets);
        //     }
        //     // Sends this to the Broker if all the Online Brokers have been sent
        //     outputStream.println("End of list");
        // } else if (brokerMessage.equalsIgnoreCase("buy") || brokerMessage.equalsIgnoreCase("sell")) {
        //     ArrayList<String> fixMessage = new ArrayList<>();
        //     // fixMessage.add("BrokerID: " + BrokerThread.thisBroker.uniqueID);
        //     for (int i = 0; i < 5; i++) {
        //         String fixMessageInfo = brokerInput.readLine();
        //         if (checkSum(fixMessageInfo, BrokerThread.thisBroker.uniqueID) == true) {
        //             System.out.println(fixMessageInfo);
        //             fixMessage.add(fixMessageInfo);
        //         } else {
        //             System.out.println("The checkSum does not match! ");
        //         }
        //     }
        //     sendFixMessageToMarket(getMarketID(fixMessage.get(3)), fixMessage, outputStream, brokerThreadID);

        // } else if (brokerMessage.equalsIgnoreCase("exit")) {
        //     // Removes this broker from the online brokers
        //     removeBrokerFromOnlineList(BrokerThread.thisBroker);
        //     BrokerThread.thisBroker = null;
        // } else {
        //     // System.out.println("Broker message = " + brokerMessage);
        // }
    }

    private static Boolean checkSum(String message, String brokerID) {
        int checkSum = 0;
        String checkSumMessage = brokerID;
        String messageSplit[] = message.split("-");
        checkSumMessage += "-" + messageSplit[1].replaceAll(" ", "-");
        for (int i = 0; i < messageSplit[1].length(); i++) {
            Integer a = Character.getNumericValue(messageSplit[1].charAt(i));
            String binary = Integer.toBinaryString(a);
            int binaryInt = Integer.parseInt(binary);
            checkSum += binaryInt;
        }

        if (message.equals(checkSumMessage += "-" + checkSum))
            return true;
        return false;

    }

    static void removeBrokerFromOnlineList(Connection thisBroker) {
        int index = RouterGlobals.onlineBrokers.indexOf(thisBroker);
        RouterGlobals.onlineBrokers.remove(index);
        System.out.println(RouterGlobals.onlineBrokers);
    }

    static void sendFixMessageToMarket(String marketID, ArrayList<String> fixMessage, PrintWriter brokerOutputStream,
            String brokerThreadID) throws IOException {
        if (marketID != null) {
            for (Connection markets : RouterGlobals.onlineMarkets) {
                // Get the onlineMarket connections
                if (markets.uniqueID.equals(marketID)) {
                    // Get that socket connection of the market you want
                    PrintWriter marketOutputStream = new PrintWriter(markets.activeSocket.getOutputStream(), true);
                    // Send Query to the Market to tell it that you have a Query coming through
                    marketOutputStream.println("Query");
                    for (int i = 0; i < 5; i++) {
                        // Send the broker Fix message to the market
                        marketOutputStream.println(fixMessage.get(i));
                    }
                }
            }
        } else {
            brokerOutputStream.println("error");
            brokerOutputStream.println("This Market does not exist [Router ID = " + brokerThreadID + "]");
        }
    }

    static String getMarketID(String market) {
        // Loops through the markets to get the ID of the instrument
        String marketSplit[] = market.split("-");
        for (MarketUtil markets : RouterGlobals.onlineMarketsInfo) {
            String marketNameSplit[] = markets.marketName.split(" ");
            if (marketSplit[1].equalsIgnoreCase(marketNameSplit[1])) {
                return markets.uniqueID;
            }
        }
        return null;
    }
}
