package com.ctaljaar.broker.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.ctaljaar.broker.Broker;
import com.ctaljaar.broker.model.BrokerStock;

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
           
                //ObjectInputStream objectInputStream = new ObjectInputStream(brokerSocket.getInputStream());
                String[] stockList = routerInput.readLine().split("\\|");
                if (stockList != null){
                   for (String stock : stockList)
                       System.out.println(stock);
                }
           
        }
        System.out.println("\n-----------End of list---------");
    }
   
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printMyAccount() {
        System.out.println("-----------My Account---------");
        if (Broker.brokerStocks.size() == 0) {
            System.out.println(" ");
            System.out.println("          Your STOCKS");
            if (Broker.brokerStocks1 == null)
                System.out.println("             Empty");
            else{
                for (BrokerStock brokerStock: Broker.brokerStocks1.values()){
                    System.out.println(brokerStock.toString());
                    System.out.println(" ");
                }
            }
            System.out.println("Balance: " + Broker.balance);
            System.out.println(" ");
        }
        System.out.println("------------- End ------------");
    }
}
