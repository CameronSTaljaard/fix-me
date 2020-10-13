package com.ctaljaar.router.model;

import java.util.Random;
import java.lang.StringBuilder;

public class RouterUtil {

	public static String generateID() {
		String allowedCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder code = new StringBuilder();
		Random rand = new Random();
		
        while (code.length() < 6) {
            int index = (int) (rand.nextFloat() * allowedCharacters.length());
            code.append(allowedCharacters.charAt(index));
        }
        String uniqueID = code.toString();
        return uniqueID;
	}
}