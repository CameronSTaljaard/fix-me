package com.ctaljaar.broker.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import com.ctaljaar.broker.Broker;

public class BrokerUtil {

    public static ArrayList<String> printFixMessageOrder(BufferedReader terminalInput) throws IOException {
        ArrayList<String> fixMessage = new ArrayList<>();

        String fixMessageQuestions[] = { "Instrument", "Quantity", "Market", "Price" };
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
        for (int i = 0; i < Broker.BrokerStocks.size(); i++) {

            if (Broker.BrokerStocks.get(i).contains(fixMessage.get(0))) {
                i++;
                String brokerQuantitySplit[] = Broker.BrokerStocks.get(i).split(" ");

                try {
                    int brokerQuantity = Integer.parseInt(brokerQuantitySplit[1]);
                    int fixMessageQuantity = Integer.parseInt(fixMessage.get(1));

                    if (brokerQuantity >= fixMessageQuantity)
                        return true;

                } catch (NumberFormatException e) {
                    System.out.println(e);
                }
            }

        }
        return false;

    }

    public static void removeAmountOfStock(ArrayList<String> fixMessage) {
        for (int i = 0; i < Broker.BrokerStocks.size(); i++) {

            if (Broker.BrokerStocks.get(i).contains(fixMessage.get(0))) {
                i++;
                String brokerQuantitySplit[] = Broker.BrokerStocks.get(i).split(" ");

                try {
                    int brokerQuantity = Integer.parseInt(brokerQuantitySplit[1]);
                    int fixMessageQuantity = Integer.parseInt(fixMessage.get(1));

                    if (brokerQuantity >= fixMessageQuantity) {
                        brokerQuantity -= fixMessageQuantity;

                        // Updates the Quantity in the stock
                        Broker.BrokerStocks.remove(i);
                        Broker.BrokerStocks.add(i, "Quantity: " + brokerQuantity);
                        System.out.println("Executed");
                        //Removes the Stock from Broker Account if the Quantity is 0
                        if (brokerQuantity <= 0) {
                            if (Broker.BrokerStocks.size() == 5) {
                                Broker.BrokerStocks.clear();
                            } else {
                                Broker.BrokerStocks.remove(--i);
                                Broker.BrokerStocks.remove(i);
                                Broker.BrokerStocks.remove(i);
                                Broker.BrokerStocks.remove(i);
                                Broker.BrokerStocks.remove(i);
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
        for (int i = 0; i < Broker.BrokerStocks.size(); i++) {

            if (Broker.BrokerStocks.get(i).contains(fixMessage.get(0))) {
                i++;
                String brokerQuantitySplit[] = Broker.BrokerStocks.get(i).split(" ");

                try {
                    int brokerQuantity = Integer.parseInt(brokerQuantitySplit[1]);
                    int fixMessageQuantity = Integer.parseInt(fixMessage.get(1));

                    brokerQuantity += fixMessageQuantity;
                    Broker.BrokerStocks.remove(i);
                    Broker.BrokerStocks.add(i, "Quantity: " + brokerQuantity);
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
        for (int i = 0; i < 4; i++) {
            String readLine = routerInput.readLine();
            Broker.BrokerStocks.add(readLine);
        }
        Broker.BrokerStocks.add(" ");
        System.out.println("Executed");
    }

    public static void sendFixMessage(PrintWriter outputStream, ArrayList<String> fixMessage) {
        if (fixMessage.size() == 4) {
            // outputStream.println("Query");
            outputStream.println("Instrument: " + fixMessage.get(0));
            outputStream.println("Quantity: " + fixMessage.get(1));
            outputStream.println("Market: " + fixMessage.get(2));
            outputStream.println("Price: " + fixMessage.get(3));
        }
    }

    public static void runBrokerCommand(String readLine, PrintWriter outputStream, BufferedReader routerInput,
            BufferedReader terminalInput) throws IOException {

        // Sends the input of the Broker

        String routerInputInfo;

        if (readLine.equalsIgnoreCase("buy")) {
            outputStream.println(readLine);
            BrokerPrinting.clearScreen();
            ArrayList<String> fixMessage = BrokerUtil.printFixMessageOrder(terminalInput);
            sendFixMessage(outputStream, fixMessage);
            BrokerPrinting.clearScreen();
            // Prints outcome of the Order for testing
            if (routerInput.readLine().equalsIgnoreCase("Order")) {
                routerInputInfo = routerInput.readLine();
                if (routerInputInfo.equalsIgnoreCase("Executed")) {
                    addStock(fixMessage, routerInput);

                } else if (routerInputInfo.equalsIgnoreCase("Rejected")) {
                    System.out.println("Rejected");

                    System.out.println("Something went wrong");
                }
            }
        }
        if (readLine.equalsIgnoreCase("sell")) {
            BrokerPrinting.clearScreen();
            Boolean brokerHasStock = false;
            ArrayList<String> fixMessage = BrokerUtil.printFixMessageOrder(terminalInput);
            brokerHasStock = BrokerUtil.checkTheAmountOfTheBrokerStock(fixMessage);
            if (brokerHasStock) {
                outputStream.println(readLine);
                sendFixMessage(outputStream, fixMessage);
                BrokerPrinting.clearScreen();
                // Prints outcome of the Order for testing
                if (routerInput.readLine().equalsIgnoreCase("Order")) {
                    routerInputInfo = routerInput.readLine();
                    if (routerInputInfo.equalsIgnoreCase("Executed")) {
                        // Read all the output from the routerInput to make the input empty
                        for (int i = 0; i < 4; i++) {
                            routerInputInfo = routerInput.readLine();
                        }
                        // Remove the amout you are selling from your stock
                        removeAmountOfStock(fixMessage);

                    } else if (routerInputInfo.equalsIgnoreCase("Rejected")) {
                        System.out.println("Rejected");

                        System.out.println("Something went wrong");
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
        String validCommands[] = { "buy", "sell", "brokers", "markets", "account" };
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
}
