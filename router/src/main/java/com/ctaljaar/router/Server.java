package com.ctaljaar.router;

public class Server implements Runnable {

	int delay;
	String name;

	public Server (int delay, String name) {
		this.delay = delay;
		this.name = name;
	}

	@Override
	public void run() {
		try {
			// System.out.println("Sleeping for " + delay);
			Thread.sleep(delay);
			System.out.println("Done task " + name);
		} catch (InterruptedException e) {
			System.out.println("Interupted exception.");
		}

	}
}