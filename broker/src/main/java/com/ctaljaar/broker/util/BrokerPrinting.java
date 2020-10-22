package com.ctaljaar.broker.util;

import java.io.BufferedReader;
import java.io.IOException;

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
        System.out.println("                Brokers");
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
                //Takes the price of the stocks and subtracts it from the Broker balance
                balance = BrokerUtil.brokerBalance(Broker.brokerStocks.get(i));
            } else {
                System.out.println(Broker.brokerStocks.get(i));
            }
            //Prints the Broker Balance
            if (i == Broker.brokerStocks.size() - 1) {
                System.out.println("Balance: " + balance);
            }
        }
        System.out.println("------------- End ------------");
    }

}
