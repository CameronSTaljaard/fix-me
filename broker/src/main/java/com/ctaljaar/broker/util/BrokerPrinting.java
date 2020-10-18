package com.ctaljaar.broker.util;

import java.io.BufferedReader;
import java.io.IOException;

public class BrokerPrinting {
	public static void welcomeMessage() {
		System.out.println("Welcome to the marketplace.");
		System.out.println("Please execute a command to continue.");
		System.out.println("Valid commands:");
		System.out.println("--------");
		System.out.println("Buy");
		System.out.println("Sell");
		System.out.println("Markets");
		System.out.println("Brokers");
		System.out.println("--------");
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
}
