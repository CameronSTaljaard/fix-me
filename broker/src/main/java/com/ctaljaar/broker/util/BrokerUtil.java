package com.ctaljaar.broker.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import com.ctaljaar.broker.Broker;

public class BrokerUtil {

    public static ArrayList<String> printFixMessageOrder(String action, BufferedReader terminalInput)
            throws IOException {
        ArrayList<String> fixMessage = new ArrayList<>();

        String fixMessageQuestions[] = { "Instrument", "Quantity", "Market", "Price" };
        // ADD the Action to the fix Message
        fixMessage.add(action);
        for (int i = 0; i < fixMessageQuestions.length; i++)
            fixMessage.add(checkInput(terminalInput, fixMessageQuestions[i]));

        return fixMessage;
    }

    public static String checkInput(BufferedReader terminalInput, String get) throws IOException {
        System.out.print("\n" + get + ": ");
        String ret = terminalInput.readLine();
        while (ret.isEmpty()) {
            System.out.print("\n" + get + ": ");
            ret = terminalInput.readLine();
        }
        return ret;
    }

    public static Boolean checkTheAmountOfTheBrokerStock(ArrayList<String> fixMessage) {
        for (int i = 0; i < Broker.brokerStocks.size(); i++) {

            if (Broker.brokerStocks.get(i).contains(fixMessage.get(1))) {
                i++;
                String brokerQuantitySplit[] = Broker.brokerStocks.get(i).split(" ");

                try {
                    int brokerQuantity = Integer.parseInt(brokerQuantitySplit[1]);
                    int fixMessageQuantity = Integer.parseInt(fixMessage.get(2));

                    if (brokerQuantity >= fixMessageQuantity)
                        return true;

                } catch (NumberFormatException e) {
                    System.out.println(e);
                    System.out.println("CheckTheAmountOfTheBrokerStock");
                }
            }

        }
        return false;

    }

    // Removes the amout of stock you sold and adds the price to sold it for to your
    // value of the stock
    public static void removeAmountOfStock(ArrayList<String> fixMessage) {
        for (int i = 0; i < Broker.brokerStocks.size(); i++) {

            if (Broker.brokerStocks.get(i).contains(fixMessage.get(1))) {
                i++;
                String brokerQuantitySplit[] = Broker.brokerStocks.get(i).split(" ");
                String brokerPriceSplit[] = Broker.brokerStocks.get(i + 2).split(" ");

                try {
                    int brokerQuantity = Integer.parseInt(brokerQuantitySplit[1]);
                    int fixMessageQuantity = Integer.parseInt(fixMessage.get(2));
                    int brokerPrice = Integer.parseInt(brokerPriceSplit[1]);
                    int fixMessagePrice = Integer.parseInt(fixMessage.get(4));

                    if (brokerQuantity >= fixMessageQuantity) {
                        brokerQuantity -= fixMessageQuantity;
                        if (fixMessage.get(0).equalsIgnoreCase("buy"))
                            brokerPrice += fixMessagePrice;
                        if (fixMessage.get(0).equalsIgnoreCase("sell"))
                            brokerPrice -= fixMessagePrice;

                        // Updates the Quantity in the stock
                        Broker.brokerStocks.remove(i);
                        Broker.brokerStocks.add(i, "Quantity: " + brokerQuantity);
                        // Updates the Value of your stock
                        Broker.brokerStocks.remove(i + 2);
                        Broker.brokerStocks.add(i + 2, "Price: " + brokerPrice);
                        System.out.println("Executed");
                        // Removes the Stock from Broker Account if the Quantity is 0
                        if (brokerQuantity <= 0) {
                            if (Broker.brokerStocks.size() == 5) {
                                Broker.brokerStocks.clear();
                            } else {
                                Broker.brokerStocks.remove(--i);
                                Broker.brokerStocks.remove(i);
                                Broker.brokerStocks.remove(i);
                                Broker.brokerStocks.remove(i);
                                Broker.brokerStocks.remove(i);
                            }
                        }

                    } else {
                        System.out.println("Something went wrong");
                    }
                } catch (NumberFormatException e) {
                    System.out.println(e);
                }
            }
        }
    }

    public static Boolean addToExistingStock(ArrayList<String> fixMessage) {
        for (int i = 0; i < Broker.brokerStocks.size(); i++) {

            if (Broker.brokerStocks.get(i).equalsIgnoreCase("Instrument: " + fixMessage.get(1))) {
                i++;
                String brokerQuantitySplit[] = Broker.brokerStocks.get(i).split(" ");
                String brokerPriceSplit[] = Broker.brokerStocks.get(i + 2).split(" ");

                try {
                    int brokerQuantity = Integer.parseInt(brokerQuantitySplit[1]);
                    int fixMessageQuantity = Integer.parseInt(fixMessage.get(2));
                    int brokerPrice = Integer.parseInt(brokerPriceSplit[1]);
                    int fixMessagePrice = Integer.parseInt(fixMessage.get(4));

                    brokerQuantity += fixMessageQuantity;
                    // IF broker buys stocks his value of the stock increases
                    if (fixMessage.get(0).equalsIgnoreCase("buy"))
                        brokerPrice += fixMessagePrice;
                    // IF broker sells stocks his value of the stock decreases
                    if (fixMessage.get(0).equalsIgnoreCase("sell"))
                        brokerPrice -= fixMessagePrice;
                    Broker.brokerStocks.remove(i);
                    Broker.brokerStocks.add(i, "Quantity: " + brokerQuantity);
                    Broker.brokerStocks.remove(i + 2);
                    Broker.brokerStocks.add(i + 2, "Price: " + brokerPrice);
                    System.out.println("Executed");
                    return false;
                } catch (NumberFormatException e) {
                    System.out.println(e);
                }
            }
        }
        return true;
    }

    public static void addNewStock(ArrayList<String> fixMessage, BufferedReader routerInput) throws IOException {

        Broker.brokerStocks.add("Instrument: " + fixMessage.get(1));
        Broker.brokerStocks.add("Quantity: " + fixMessage.get(2));
        Broker.brokerStocks.add("Market: " + fixMessage.get(3));
        Broker.brokerStocks.add("Price: " + fixMessage.get(4));
        Broker.brokerStocks.add(" ");
        System.out.println("Executed");
    }

    public static void sendFixMessage(PrintWriter outputStream, ArrayList<String> fixMessage) {
        if (fixMessage.size() == 5) {
            // outputStream.println("Query");
            outputStream.println("Action: " + fixMessage.get(0));
            outputStream.println("Instrument: " + fixMessage.get(1));
            outputStream.println("Quantity: " + fixMessage.get(2));
            outputStream.println("Market: " + fixMessage.get(3));
            outputStream.println("Price: " + fixMessage.get(4));
        }
    }

    public static void runBrokerCommand(String readLine, PrintWriter outputStream, BufferedReader routerInput,
            BufferedReader terminalInput) throws IOException {

        // Sends the input of the Broker

        String routerInputInfo;
        if (readLine.equalsIgnoreCase("options")) {
            BrokerPrinting.clearScreen();
            BrokerPrinting.welcomeMessage();
        }
        if (readLine.equalsIgnoreCase("buy")) {
            outputStream.println(readLine);
            BrokerPrinting.clearScreen();
            ArrayList<String> fixMessage = BrokerUtil.printFixMessageOrder(readLine, terminalInput);
            sendFixMessage(outputStream, fixMessage);
            // Router returns error if something went wrong
            String routerCheck = routerInput.readLine();
            BrokerPrinting.clearScreen();
            // Prints outcome of the Order for testing
            if (routerCheck.equalsIgnoreCase("Order")) {
                routerInputInfo = routerInput.readLine();
                if (routerInputInfo.equalsIgnoreCase("Executed")) {
                    addStock(fixMessage, routerInput);

                } else if (routerInputInfo.equalsIgnoreCase("Rejected")) {
                    System.out.println("Rejected");

                    System.out.println(routerInput.readLine());
                }
            }
            if (routerCheck.equalsIgnoreCase("error")) {
                System.out.println(routerInput.readLine());
            }
        }
        if (readLine.equalsIgnoreCase("sell")) {
            BrokerPrinting.clearScreen();
            Boolean brokerHasStock = false;
            ArrayList<String> fixMessage = BrokerUtil.printFixMessageOrder(readLine, terminalInput);
            brokerHasStock = BrokerUtil.checkTheAmountOfTheBrokerStock(fixMessage);
            if (brokerHasStock) {
                outputStream.println(readLine);
                sendFixMessage(outputStream, fixMessage);
                BrokerPrinting.clearScreen();
                // Prints outcome of the Order for testing
                if (routerInput.readLine().equalsIgnoreCase("Order")) {
                    routerInputInfo = routerInput.readLine();
                    if (routerInputInfo.equalsIgnoreCase("Executed")) {
                        // Remove the amout you are selling from your stock
                        removeAmountOfStock(fixMessage);

                    } else if (routerInputInfo.equalsIgnoreCase("Rejected")) {
                        System.out.println("Rejected");

                        System.out.println(routerInput.readLine());
                    }
                } else {
                    System.out.println("Something went wrong the order");
                }
            } else {
                BrokerPrinting.clearScreen();
                System.out.println(
                        "The quantity of stock you want to sell is more than you own or you don't even own that stock");
            }

        }

        // if the Broker sends 'list' then Router will send all the online Brokers
        if (readLine.equalsIgnoreCase("brokers")) {
            outputStream.println(readLine);
            BrokerPrinting.clearScreen();

            // Prints the Online Brokers
            BrokerPrinting.printOnlineBrokers(routerInput);
        }
        if (readLine.equalsIgnoreCase("markets")) {
            outputStream.println(readLine);
            // Prints the Online Brokers
            BrokerPrinting.clearScreen();

            BrokerPrinting.printOnlineMarkets(routerInput);
        }
        if (readLine.equalsIgnoreCase("account")) {
            outputStream.println(readLine);
            BrokerPrinting.clearScreen();

            // Prints the Online Brokers
            BrokerPrinting.printMyAccount();
        }
    }

    public static boolean validCommand(String command) {
        String validCommands[] = { "buy", "sell", "brokers", "markets", "account", "options" };
        boolean valid = Arrays.stream(validCommands).anyMatch(command.toLowerCase()::equals);
        if (valid)
            return true;
        return false;
    }

    public static void addStock(ArrayList<String> fixMessage, BufferedReader routerInput) throws IOException {
        Boolean newStock = false;
        newStock = addToExistingStock(fixMessage);
        if (newStock)
            addNewStock(fixMessage, routerInput);

    }

    public static int brokerBalance(int balance,String price) {
        String priceSplit[] = price.split(" ");
        try {
            int priceValue = Integer.parseInt(priceSplit[1]);
            balance -= priceValue;
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
        return balance;
    }
}
