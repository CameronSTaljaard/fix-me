package com.ctaljaar.broker.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import com.ctaljaar.broker.Broker;
import com.ctaljaar.broker.model.BrokerStock;

public class BrokerUtil {

    public static void runBrokerCommand(String readLine, PrintWriter outputStream, BufferedReader routerInput, BufferedReader terminalInput) throws IOException {
        // Sends the input of the Broker
        String routerInputInfo;
        if (readLine.equalsIgnoreCase("options")) {
            BrokerPrinting.clearScreen();
            BrokerPrinting.welcomeMessage();
        }

        if (readLine.equalsIgnoreCase("buy") || readLine.equalsIgnoreCase("sell")) {
            BrokerPrinting.clearScreen();
            ArrayList<String> fixMessage = BrokerUtil.getFixMessage(readLine, terminalInput);
            String type = fixMessage.get(0);
            String instrument = fixMessage.get(1);
            int qty = Integer.parseInt(fixMessage.get(2));
            String market = fixMessage.get(3);
            int price = Integer.parseInt(fixMessage.get(4));

            if (readLine.equalsIgnoreCase("sell")){
                if (checkBrokerStock(instrument, qty))
                    return;
            }
            if (checkBalance(price * qty))
                return;
            String request = Broker.id + "|";
            for (String str : fixMessage){
                request += str + "|";
            }
            request += calculateChecksum(request.substring(0,request.length() - 1));
            System.out.println(request); //remove
            outputStream.println(request);
            BrokerPrinting.clearScreen();
            String routerMessage = routerInput.readLine();
            //update broker stock and balance
            updateStock(fixMessage);
            updateBalance(price, type);
            System.out.println("\n" + routerMessage);
        }
    
        if (readLine.equalsIgnoreCase("markets")) {
            outputStream.println(readLine);
            // Prints the Online Brokers
            BrokerPrinting.clearScreen();
            BrokerPrinting.printOnlineMarkets(routerInput, Broker.brokerSocket);
        }

        if (readLine.equalsIgnoreCase("account")) {
            outputStream.println(readLine);
            BrokerPrinting.clearScreen();
            // Prints the Online Brokers
            BrokerPrinting.printMyAccount();
        }
    }

    public static ArrayList<String> getFixMessage(String action, BufferedReader terminalInput)
            throws IOException {
        ArrayList<String> fixMessage = new ArrayList<>();
        String fixMessageQuestions[] = { "Instrument", "Quantity", "Market", "Price" };
        // ADD the Action to the fix Message
        fixMessage.add(action);
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

    public static Boolean checkBrokerStock(String stock, int qty) {
        BrokerStock brokerStock = Broker.brokerStocks1.get(stock);
        if (brokerStock == null){
            System.out.println("Intrument does not exists");
            return true;
        }
        System.out.println(brokerStock.getQuantity()); //remove
        System.out.println(qty); //remove
        System.out.println(Integer.parseInt(brokerStock.getQuantity()) - qty); //remove
        if ((Integer.parseInt(brokerStock.getQuantity()) - qty) < 0){
            System.out.println("You do not have sufficient quantity");
            return true;
        }
        return false;
    }

    public static boolean checkBalance(int price){
        if (Broker.balance < price){
            System.out.println("You have insufficient funds");
            return true;
        }
        return false;
    }

    public static void updateBalance(int price, String type){
        if (type.equalsIgnoreCase("buy"))
            Broker.balance -= price;
        else
            Broker.balance += price;
    }

    public static long calculateChecksum(String request){
        byte[] bytes = request.getBytes();
        Checksum crc32 = new CRC32();
        crc32.update(bytes, 0, bytes.length);
        return crc32.getValue();
    }

    public static boolean validCommand(String command) {
        String validCommands[] = { "buy", "sell", "markets", "account", "options" };
        boolean valid = Arrays.stream(validCommands).anyMatch(command.toLowerCase()::equals);
        if (valid)
            return true;
        return false;
    }

    public static void updateStock(ArrayList<String> fixMessage) throws IOException {
        if (Broker.brokerStocks1.get(fixMessage.get(1)) == null){
            Broker.brokerStocks1.put(fixMessage.get(1), new BrokerStock(fixMessage.get(1), fixMessage.get(2), fixMessage.get(4)));
            System.out.println(Broker.brokerStocks1.get(fixMessage.get(1)));  //remove
        }
        else {
            BrokerStock brokerStock = Broker.brokerStocks1.get(fixMessage.get(1));
            int requestQty = Integer.parseInt(fixMessage.get(2));
            int brokerQty = Integer.parseInt(brokerStock.getQuantity());
            int qty = (fixMessage.get(0).equalsIgnoreCase("buy")) ? brokerQty + requestQty : brokerQty - requestQty;
            System.out.println("qty: " + qty); //remove
            if (qty == 0)
                Broker.brokerStocks1.remove(fixMessage.get(1));
            else 
                brokerStock.setQuantity(Integer.toString(qty));
            System.out.println(Broker.brokerStocks1.get(fixMessage.get(1))); //remove
            //System.out.println("Executed");
        }
    }
}