package com.ctaljaar.broker.util;

import java.io.BufferedReader;
import java.io.IOException;

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

}
