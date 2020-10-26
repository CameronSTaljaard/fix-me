package com.ctaljaar.router.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.ctaljaar.router.model.RouterGlobals;

public class MarketThreadUtil {

    protected static String errorOutputStream = null;
    protected static String IDOutputStream = null;

    // Very unlikely this works.
    // Function is attempting to find a Market in a Connection arraylist with a
    // market constructor.
    public static void removeMarketFromOnlineList(MarketUtil thisMarket) {
        int index = RouterGlobals.onlineMarkets.indexOf(thisMarket);
        RouterGlobals.onlineMarkets.remove(index);
        System.out.println(RouterGlobals.onlineMarkets);
    }

    public static void checkFixMessage(ArrayList<String> fixMessage, String marketThreadID) throws IOException {
        IDOutputStream = marketThreadID;

        // Loop through all the functions and breaks if there is an error
        // i is the amount of error checking methods there are
        for (int i = 0; i < 4; i++) {
            i = errorCheck(i, fixMessage);
        }

    }

    public static int errorCheck(int i, ArrayList<String> fixMessage) throws IOException {
        Boolean error = false;
        String brokerID[] = fixMessage.get(0).split(" ");
        String checkSum = fixMessage.get(1);
        String action = fixMessage.get(2);
        String instrument = fixMessage.get(3);
        String quantity = fixMessage.get(4);
        String price = fixMessage.get(6);

        switch (i) {
            case 0:
                error = checkInstrument(instrument);
                break;
            case 1:
                error = checkSumOfPrice(instrument, quantity, price, action);
                break;
            case 2:
                error = checkSumQuantity(instrument, quantity, action);
                break;
            case 3:
                error = checkSum(brokerID[1], checkSum, action);
                break;
            default:
                System.out.println("Something went wrong!");
                break;
        }
        if (error) {
            sendFixMessageToBroker(brokerID[1], error, fixMessage);
            return i = 3;
        }
        if (i == 3) {
            sendFixMessageToBroker(brokerID[1], error, fixMessage);

        }
        return i;
    }

    // Check if the Instrument is in the Market
    public static Boolean checkInstrument(String instrument) {
        for (MarketUtil markets : RouterGlobals.onlineMarketsInfo) {
            if (instrument.equalsIgnoreCase(markets.stockName)) {
                return false;
            }
        }
        errorOutputStream = "This Stock does not exist in this Market [Router ID = " + IDOutputStream + "]";

        return true;
    }

    // Check if the broker quantity is more than the market quantity
    public static Boolean checkSumQuantity(String market, String quantity, String action) {
        String quantityValue[] = quantity.split(" ");
        for (MarketUtil markets : RouterGlobals.onlineMarketsInfo) {
            if (market.equalsIgnoreCase(markets.stockName)) {
                try {
                    int quantityInt = Integer.parseInt(quantityValue[1]);
                    if (quantityInt < markets.quantity || action.equalsIgnoreCase("Action: Sell")) {
                        markets.updateQuanity(quantityInt, action);

                        return false;
                    }
                } catch (NumberFormatException e) {
                    System.out.println(e);
                    System.out.println("CheckSum");
                }
            }
        }
        errorOutputStream = "The Quantity you want of the stock is more than the Stock has [Router ID = "
                + IDOutputStream + "]";

        return true;
    }

    public static Boolean checkSum(String brokerID, String checkSum, String action) {
        String checkSumID[] = checkSum.split(" ");
        String actionSplit[] = action.split(" ");

        if (checkSumID[0].equals(brokerID) && checkSumID[2].equals(actionSplit[1])) {
            return false;
        }
        return true;
    }

    public static Boolean checkSumOfPrice(String market, String quantity, String brokerPaid, String action) {
        String quantityValue[] = quantity.split(" ");
        String brokerPaidValue[] = brokerPaid.split(" ");
        for (MarketUtil markets : RouterGlobals.onlineMarketsInfo) {
            if (market.equalsIgnoreCase(markets.stockName)) {
                try {
                    int quantityInt = Integer.parseInt(quantityValue[1]);
                    int priceInt = Integer.parseInt(brokerPaidValue[1]);
                    if (action.equalsIgnoreCase("Action: Sell")) {
                        int totalAmountDue = quantityInt * markets.sellPrice;
                        if (totalAmountDue == priceInt) {
                            return false;
                        } else if (totalAmountDue < priceInt) {
                            errorOutputStream = "You are asking to much for this stock .The Sell price is "
                                    + markets.sellPrice + " per stock [Router ID = " + IDOutputStream + "]";
                            return true;

                        }
                    } else if (action.equalsIgnoreCase("Action: Buy")) {
                        int totalAmountDue = quantityInt * markets.buyPrice;
                        if (totalAmountDue == priceInt) {
                            return false;
                        } else if (totalAmountDue < priceInt) {
                            errorOutputStream = "You are paying to much for this stock .The Buy price is "
                                    + markets.sellPrice + " per stock [Router ID = " + IDOutputStream + "]";
                            return true;
                        }
                    }

                } catch (NumberFormatException e) {
                    System.out.println(e);
                    System.out.println("CheckSumOfPrice");

                }
            }
        }
        if (action.equalsIgnoreCase("Action: Buy"))
            errorOutputStream = "The Quantity that you want of the stock costs more than what you paid [Router ID = "
                    + IDOutputStream + "]";
        if (action.equalsIgnoreCase("Action: Sell"))
            errorOutputStream = "The Amount you want for your stock is more than what it is worth [Router ID = "
                    + IDOutputStream + "]";

        return true;
    }

    static void sendFixMessageToBroker(String brokerID, Boolean error, ArrayList<String> fixMessage)
            throws IOException {
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
                        outputStream.println(errorOutputStream);

                    } else {
                        outputStream.println("Executed");
                        outputStream.println(
                                fixMessage.get(2) + " was Executed successfully [Router ID = " + IDOutputStream + "]");
                    }
                }
            }
        }
    }
}
