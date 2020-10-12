package com.ctaljaar.router;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Router {

	public static void main(String[] arg) throws Exception {

		int port = 5000;
		ServerSocket serverSocket = new ServerSocket(port);
		Socket ss = serverSocket.accept();
		System.out.println("connected");
		DataInputStream dataInputStream = new DataInputStream(ss.getInputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(true){
			String str = dataInputStream.readUTF();
		
			System.out.println("broker = " + str);
			System.out.println(ss.getPort());
			if(ss.getPort()== 5000) {
				System.out.println("Broker = " + str);
			}else if(ss.getPort()== 5001) {
				System.out.println("Market = " + str);
			}
			if(str.equalsIgnoreCase("exit"))
				break;
		}
		ss.close();

	}
}