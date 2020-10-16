package com.ctaljaar.router.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.ctaljaar.router.Router;

public class MarketThreadUtil {

    public static void removeMarketFromOnlineList(MarketUtil thisMarket) {
        int index = Router.onlineMarkets.indexOf(thisMarket);
        Router.onlineMarkets.remove(index);
        System.out.println(Router.onlineMarkets);
    }

    public static void checkFixMessage(ArrayList<String> fixMessage) throws IOException {
        /*
         * There should be more Functions in the method
         * 
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * 
         * 
         */
        Boolean error = false;
        String brokerID[] = fixMessage.get(0).split(" ");
        String instrument = fixMessage.get(1);
        String quantity = fixMessage.get(2);
        String market = fixMessage.get(3);
        String price = fixMessage.get(4);

        while (!error) {

            error = checkInstrument(instrument);
            if (error) {
                sendFixMessageToBroker(brokerID[1], error);
                break;
            }
            sendFixMessageToBroker(brokerID[1], error);
            break;

        }

    }

    // Check if the instrument is in the market
    public static Boolean checkInstrument(String instrument) {
        for (MarketUtil markets : Router.onlineMarketsInfo) {
            if (instrument.equalsIgnoreCase(markets.stockName)) {
                return false;
            }
        }
        return true;
    }

    static void sendFixMessageToBroker(String brokerID, Boolean error) throws IOException {
        if (brokerID != null) {
            for (Connection broker : Router.onlineBrokers) {
                // Get the onlineBroker connections
                if (broker.uniqueID.equals(brokerID)) {
                    // Get that socket connection of the Broker you want
                    PrintWriter outputStream = new PrintWriter(broker.activeSocket.getOutputStream(), true);
                    // Send 'Order' to the Broker to tell it that you have the outcome of the Order
                    // coming through
                    outputStream.println("Order");
                    // Market sends outcome to the Broker
                    if (error == true) {
                        outputStream.println("Rejected");

                    } else {
                        outputStream.println("Executed");
                    }
                }
            }
        }
    }

}
