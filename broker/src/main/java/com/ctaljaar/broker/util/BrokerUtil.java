package com.ctaljaar.broker.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class BrokerUtil {

    public static void printOnlineBrokers(BufferedReader routerInput) throws IOException {
        String routerMessage;
        System.out.println("-------Connected brokers------");
        System.out.println("");

        // Reads the data from the router
        while ((routerMessage = routerInput.readLine()) != null) {
            if (routerMessage.equalsIgnoreCase("End of list"))
                break;
            System.out.println(routerMessage);
        }
        System.out.println("");
        System.out.println("-----------End of list---------");
    }

    public static void printOnlineMarkets(BufferedReader routerInput) throws IOException {
        int i = 0;
        String routerMessage;
        System.out.println("---------Online Markets-------");
        System.out.println("");

        // Reads the data from the router
        while ((routerMessage = routerInput.readLine()) != null) {
            // Prints a space between the different markets
            if (i == 7) {
                System.out.println("");
                i = 0;
            }

            if (routerMessage.equalsIgnoreCase("End of list"))
                break;
            System.out.println(routerMessage);
            i++;

        }
        System.out.println("-----------End of list---------");
    }

    public static ArrayList<String> printFixMessageOrder(BufferedReader terminalInput) throws IOException {
        ArrayList<String> fixMessage = new ArrayList<>();
        // String fixMessage[] = readLine.split(" ");
        System.out.print("\nInstrument: ");
        fixMessage.add(terminalInput.readLine());

        System.out.print("\nQuantity: ");
        fixMessage.add(terminalInput.readLine());
        System.out.print("\nMarket: ");
        fixMessage.add(terminalInput.readLine());
        System.out.print("\nPrice: ");
        fixMessage.add(terminalInput.readLine());
        return fixMessage;

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

    public static void checkBrokerMessage(String readLine, PrintWriter outputStream, BufferedReader routerInput,
            BufferedReader terminalInput) throws IOException {

        outputStream.println(readLine);// Sends the input of the Broker

        if (readLine.contains("buy")||readLine.contains("sell")) {
            ArrayList<String> fixMessage = BrokerUtil.printFixMessageOrder(terminalInput);
            sendFixMessage(outputStream, fixMessage);
        }
        // if the Broker sends 'list' then Router will send all the online Brokers
        if (readLine.equalsIgnoreCase("list")) {
            // Prints the Online Brokers
            BrokerUtil.printOnlineBrokers(routerInput);
        }
        if (routerInput.readLine().equalsIgnoreCase("Order")) {
            // Prints outcome of the Order for testing
            System.out.println(routerInput.readLine());
        }
        if (readLine.equalsIgnoreCase("markets")) {
            // Prints the Online Brokers
            BrokerUtil.printOnlineMarkets(routerInput);

        }
    }

}
