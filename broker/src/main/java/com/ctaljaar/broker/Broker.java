package com.ctaljaar.broker;

import java.io.*;
import java.net.Socket;

public class Broker {
	public static void main(String[] args) throws Exception {
		String readLine;
		int i = 0;
		String ip = "localhost";
		int port = 5000;
		Socket brokerSocket = new Socket(ip, port);
		DataOutputStream outputStream = new DataOutputStream(brokerSocket.getOutputStream());

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		boolean done = false;
		while (!done) {
			readLine = br.readLine();
			/* The first message that the Broker sends will also send 1 byte to the Router the following messages will
			 send 2 bytes to the Router with the input of the Broker*/
			if (i == 0) {
				outputStream.writeByte(1);// Sends 1 byte
				outputStream.writeUTF(readLine);// Sends the input of the Broker
				outputStream.flush();
				i++;
				if (readLine.equalsIgnoreCase("exit"))
					break;

			} else if (i == 1) {
				outputStream.writeByte(2);// Sends 2 bytes
					outputStream.writeUTF(readLine);// Sends the input of the Broker
					if (readLine.equalsIgnoreCase("exit"))
						break;
			} else {
				done = true;
			}
		}
		brokerSocket.close();

	}
}
