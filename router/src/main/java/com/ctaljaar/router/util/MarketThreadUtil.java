package com.ctaljaar.router.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.ctaljaar.router.model.RouterGlobals;

public class MarketThreadUtil {

	// Very unlikely this works.
	// Function is attempting to find a Market in a Connection arraylist with a market constructor.
    public static void removeMarketFromOnlineList(MarketUtil thisMarket) {
        int index = RouterGlobals.onlineMarkets.indexOf(thisMarket);
        RouterGlobals.onlineMarkets.remove(index);
        System.out.println(RouterGlobals.onlineMarkets);
    }

    public static void checkFixMessage(ArrayList<String> fixMessage) throws IOException {
        /*
         * There should be more Functions in the method
         * 
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * 
         * 
         */
        // Loop through all the functions and breaks if there is an error
        for (int i = 0; i < 2; i++) {
            i = errorCheck(i, fixMessage);
        }

    }

    public static int errorCheck(int i, ArrayList<String> fixMessage) throws IOException {
        Boolean error = false;
        String brokerID[] = fixMessage.get(0).split(" ");
        String action = fixMessage.get(1);
        String instrument = fixMessage.get(2);
        String quantity = fixMessage.get(3);
        String market = fixMessage.get(4);
        String price = fixMessage.get(5);

        switch (i) {
            case 0:
                error = checkInstrument(instrument);
                break;
            case 1:
                error = checkSum(instrument, quantity, action);
                break;

            default:
                System.out.println("Something went wrong!");
                break;
        }
        if (error) {
            sendFixMessageToBroker(brokerID[1], error);
            return i = 2;
        }
        if (i == 1) {
            sendFixMessageToBroker(brokerID[1], error);

        }
        return i;
    }

    // Check if the instrument is in the market
    public static Boolean checkInstrument(String instrument) {
        for (MarketUtil markets : RouterGlobals.onlineMarketsInfo) {
            if (instrument.equalsIgnoreCase(markets.stockName)) {
                return false;
            }
        }
        return true;
    }

    // Check if the broker quantity is more than the market quantity
    public static Boolean checkSum(String instrument, String quantity, String action) {
        String quantityValue[] = quantity.split(" ");
        for (MarketUtil markets : RouterGlobals.onlineMarketsInfo) {
            if (instrument.equalsIgnoreCase(markets.stockName)) {
                int quantityInt = Integer.parseInt(quantityValue[1]);
                if (quantityInt < markets.quantity) {
                    markets.updateQuanity(quantityInt, action);
                    return false;
                }
            }
        }
        return true;
    }

    static void sendFixMessageToBroker(String brokerID, Boolean error) throws IOException {
        if (brokerID != null) {
            for (Connection broker : RouterGlobals.onlineBrokers) {
                // Get the onlineBroker connections
                if (broker.uniqueID.equalsIgnoreCase(brokerID)) {
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
