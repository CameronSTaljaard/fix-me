package com.ctaljaar.broker.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class BrokerUtil {

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

    public static void runBrokerCommand(String readLine, PrintWriter outputStream, BufferedReader routerInput,
            BufferedReader terminalInput) throws IOException {

		// Sends the input of the Broker
		outputStream.println(readLine);
		
        if (readLine.contains("buy") || readLine.contains("sell")) {
			ArrayList<String> fixMessage = BrokerUtil.printFixMessageOrder(terminalInput);
			sendFixMessage(outputStream, fixMessage);
			// Prints outcome of the Order for testing
			if (routerInput.readLine().equalsIgnoreCase("Order")) {
				System.out.println(routerInput.readLine());
			}
        }
        // if the Broker sends 'list' then Router will send all the online Brokers
        if (readLine.equalsIgnoreCase("brokers")) {
			// Prints the Online Brokers
            BrokerPrinting.printOnlineBrokers(routerInput);
		}
		if (readLine.equalsIgnoreCase("markets")) {
			// Prints the Online Brokers
			BrokerPrinting.printOnlineMarkets(routerInput);
		}   
	}
	
	public static boolean validCommand(String command) {
		String validCommands[] = {"buy","sell","brokers","markets"};
		boolean valid = Arrays.stream(validCommands).anyMatch(command.toLowerCase()::equals);
		if (valid)
			return true;
		return false;
	}
}
