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
            if (checkBalance(price))
                return;

            String request = constructRequest(Broker.id, fixMessage);
            outputStream.println(request);

            String routerMessage = routerInput.readLine();
            //update broker stock and balance
            if (routerMessage.contains("Executed")){
                updateStock(fixMessage);
                updateBalance(price, type);
            }
            BrokerPrinting.clearScreen();
            System.out.println("\n" + routerMessage);
        }
    
        if (readLine.equalsIgnoreCase("markets")) {
            outputStream.println(Broker.id + "|" + readLine);
            // Prints the Online Brokers
            BrokerPrinting.clearScreen();
            BrokerPrinting.printOnlineMarkets(routerInput, Broker.brokerSocket);
        }

        if (readLine.equalsIgnoreCase("account")) {
            BrokerPrinting.clearScreen();
            // Prints the Online Brokers
            BrokerPrinting.printMyAccount();
        }
    }

    public static String constructRequest(String brokerID, ArrayList<String> fixMessage){
        String request = Broker.id + "|";
        for (String str : fixMessage){
            request += str + "|";
        }
        request += calculateChecksum(request.substring(0,request.length() - 1));
        return request;
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
        String ret = "";
        while (ret.isEmpty() || ret.equals("0")) {
            System.out.print("\n" + get + ": ");
            ret = terminalInput.readLine();
            if (get.equalsIgnoreCase("Quantity") || get.equalsIgnoreCase("Price")){
                if (!ret.matches("\\d+")){
                    ret = "";
                }
            }
        }
        return ret;
    }

    public static Boolean checkBrokerStock(String stock, int qty) {
        BrokerStock brokerStock = Broker.brokerStocks1.get(stock);
        if (brokerStock == null){
            System.out.println("Intrument does not exists");
            return true;
        }
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
            Broker.brokerStocks1.put(fixMessage.get(1), new BrokerStock(fixMessage.get(1), fixMessage.get(2)));
        }
        else {
            BrokerStock brokerStock = Broker.brokerStocks1.get(fixMessage.get(1));
            int requestQty = Integer.parseInt(fixMessage.get(2));
            int brokerQty = Integer.parseInt(brokerStock.getQuantity());
            int qty = (fixMessage.get(0).equalsIgnoreCase("buy")) ? brokerQty + requestQty : brokerQty - requestQty;
            if (qty == 0)
                Broker.brokerStocks1.remove(fixMessage.get(1));
            else 
                brokerStock.setQuantity(Integer.toString(qty));
        }
    }
}