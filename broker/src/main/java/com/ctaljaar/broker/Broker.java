package com.ctaljaar.broker;

import java.io.*;
import java.net.Socket;

public class Broker {
	public static void main(String[] args) throws Exception {
		String ip = "localhost";
		int port = 5000;
		Socket brokerSocket = new Socket(ip, port);
		DataOutputStream outputStream = new DataOutputStream(brokerSocket.getOutputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(true){
			String readLine = br.readLine();
			outputStream.writeUTF(readLine);
			if(readLine.equalsIgnoreCase("exit"))
				break;
		}
		brokerSocket.close();
	}
}
