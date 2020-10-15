package com.ctaljaar.router.util;

import java.io.PrintWriter;

import com.ctaljaar.router.Router;
import com.ctaljaar.router.broker.BrokerThread;

public class BrokerThreadUtil {

    public static void checkBrokerMessage(String brokerMessage, PrintWriter outputStream) {
        if (brokerMessage.equalsIgnoreCase("list")) {
            for (Connection connection : Router.onlineBrokers) {
                // Send the data to the Broker
                outputStream.println("Broker: " + connection);
            }
            // Sends this to the Broker if all the Online Brokers have been sent
            outputStream.println("End of list");
        } else if (brokerMessage.equalsIgnoreCase("markets")) {
            for (MarketUtil markets : Router.onlineMarkets) {
                // Send the data to the Broker
                outputStream.println(markets);
            }
            // Sends this to the Broker if all the Online Brokers have been sent
            outputStream.println("End of list");
        } else if (brokerMessage.equalsIgnoreCase("exit")) {
            // Removes this broker from the online brokers
            removeBrokerFromOnlineList(BrokerThread.thisBroker);
            BrokerThread.thisBroker = null;
        } else {
            System.out.println("Broker message = " + brokerMessage);
        }
    }

    static void removeBrokerFromOnlineList(Connection thisBroker) {
        int index = Router.onlineBrokers.indexOf(thisBroker);
        Router.onlineBrokers.remove(index);
        System.out.println(Router.onlineBrokers);
    }
}
