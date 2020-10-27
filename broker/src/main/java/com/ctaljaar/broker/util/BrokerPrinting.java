package com.ctaljaar.broker.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.ctaljaar.broker.Broker;

public class BrokerPrinting {
    public static void welcomeMessage() {
        System.out.println(" ");
        System.out.println("     Welcome to the marketplace");
        System.out.println("Please execute a command to continue");
        System.out.println("---------------------------------------");
        System.out.println("Valid commands:");
        System.out.println("                Buy");
        System.out.println("                Sell");
        System.out.println("                Markets");
        System.out.println("                Account");
        System.out.println("---------------------------------------");
        System.out.println(" ");
    }

    public static void printOnlineBrokers(BufferedReader routerInput) throws IOException {
        String routerMessage;

        System.out.println("-------Connected brokers------");
        System.out.println("");
        // Reads the data from the router
        while ((routerMessage = routerInput.readLine()) != null) {
            if (routerMessage.equalsIgnoreCase("End of list"))
                break;
            System.out.println(routerMessage + "\n");
        }
        System.out.println("");
        System.out.println("-----------End of list---------");
    }

    public static void printOnlineMarkets(BufferedReader routerInput, Socket brokerSocket) throws IOException {
        String routerMessage;

        System.out.println("---------Online Markets-------");
        System.out.println("");
        // Reads the data from the router
        while ((routerMessage = routerInput.readLine()) != null) {
            if (routerMessage.equalsIgnoreCase("End of list"))
                break;
            System.out.println(routerMessage + "\n");
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(brokerSocket.getInputStream());
                ArrayList<String> stockList = (ArrayList<String>) objectInputStream.readObject();
                if (stockList != null){
                   for (String stock : stockList)
                       System.out.println(stock);
                }
            } catch ( ClassNotFoundException e ) {
                System.out.println("The socket for reading the object has problem");
                e.printStackTrace();
            }    
        }
        System.out.println("\n-----------End of list---------");
    }
   
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printMyAccount() {
        int balance = 5000;

        System.out.println("-----------My Account---------");
        if (Broker.brokerStocks.size() == 0) {
            System.out.println(" ");
            System.out.println("          Your STOCKS");
            System.out.println("             Empty");
            System.out.println(" ");
            System.out.println("Balance: " + balance);
            System.out.println(" ");
        }
        for (int i = 0; i < Broker.brokerStocks.size(); i++) {
            if (i == 0) {
                System.out.println(" ");
                System.out.println("          Your STOCKS");
            }
            if (Broker.brokerStocks.get(i).contains("Price:")) {
                // Takes the price of the stocks and subtracts it from the Broker balance
                balance = BrokerUtil.brokerBalance(balance, Broker.brokerStocks.get(i));
            } else {
                System.out.println(Broker.brokerStocks.get(i));
            }
            // Prints the Broker Balance
            if (i == Broker.brokerStocks.size() - 1) {
                System.out.println("Balance: " + balance);
            }
        }
        System.out.println("------------- End ------------");
    }
}
