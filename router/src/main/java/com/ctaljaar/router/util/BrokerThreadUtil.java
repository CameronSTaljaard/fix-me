package com.ctaljaar.router.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.ctaljaar.router.Router;
import com.ctaljaar.router.broker.BrokerThread;

public class BrokerThreadUtil {

    public static void checkBrokerMessage(String brokerMessage, PrintWriter outputStream, BufferedReader brokerInput)
            throws IOException {
        if (brokerMessage.equalsIgnoreCase("list")) {
            for (Connection connection : Router.onlineBrokers) {
                // Send the data to the Broker
                outputStream.println("Broker: " + connection);
            }
            // Sends this to the Broker if all the Online Brokers have been sent
            outputStream.println("End of list");
        } else if (brokerMessage.equalsIgnoreCase("markets")) {
            for (MarketUtil markets : Router.onlineMarketsInfo) {
                // Send the data to the Broker
                outputStream.println(markets);
            }
            // Sends this to the Broker if all the Online Brokers have been sent
            outputStream.println("End of list");
        } else if (brokerMessage.equalsIgnoreCase("buy")) {
            ArrayList<String> fixMessage = new ArrayList<>();
            fixMessage.add("BrokerID: "+BrokerThread.thisBroker.uniqueID);
            for (int i = 0; i < 4; i++) {
                String fixMessageInfo = brokerInput.readLine();
                fixMessage.add(fixMessageInfo);
            }
            // To get just the marketID

            // String marketID = fixMessage.get(2).split(" ");
            sendFixMessageToMarket(getMarketID(fixMessage.get(1)), fixMessage);

        } else if (brokerMessage.equalsIgnoreCase("exit")) {
            // Removes this broker from the online brokers
            removeBrokerFromOnlineList(BrokerThread.thisBroker);
            BrokerThread.thisBroker = null;
        } else {
            // System.out.println("Broker message = " + brokerMessage);
        }
    }

    static void removeBrokerFromOnlineList(Connection thisBroker) {
        int index = Router.onlineBrokers.indexOf(thisBroker);
        Router.onlineBrokers.remove(index);
        System.out.println(Router.onlineBrokers);
    }

    static void sendFixMessageToMarket(String marketID, ArrayList<String> fixMessage) throws IOException {
        if (marketID != null) {
            for (Connection markets : Router.onlineMarkets) {
                // Get the onlineMarket connections
                if (markets.uniqueID.equals(marketID)) {
                    // Get that socket connection of the market you want
                    PrintWriter outputStream = new PrintWriter(markets.activeSocket.getOutputStream(), true);
                    // Send Query to the Market to tell it that you have a Query coming through
                    outputStream.println("Query");
                    for (int i = 0; i < 5; i++) {
                        // Send the broker Fix message to the market
                        outputStream.println(fixMessage.get(i));
                    }

                }
            }
        }
    }

    static String getMarketID(String instrument) {
        // Loops through the markets to get the ID of the instrument
        for (MarketUtil markets : Router.onlineMarketsInfo) {
            if (instrument.equalsIgnoreCase(markets.stockName)) {
                return markets.uniqueID;
            }
        }
        return null;
    }
}
