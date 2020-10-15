package com.ctaljaar.broker.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

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

    public static void checkBrokerMessage(String readLine, PrintWriter outputStream, BufferedReader routerInput)
            throws IOException {
        outputStream.println(readLine);// Sends the input of the Broker
        // if the Broker sends 'list' then Router will send all the online Brokers
        if (readLine.equalsIgnoreCase("list")) {
            // Prints the Online Brokers
            BrokerUtil.printOnlineBrokers(routerInput);
        }
        if (readLine.equalsIgnoreCase("markets")) {
            // Prints the Online Brokers
            BrokerUtil.printOnlineMarkets(routerInput);

        }

    }

}
