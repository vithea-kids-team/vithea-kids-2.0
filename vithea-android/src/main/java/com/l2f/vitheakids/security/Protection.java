package com.l2f.vitheakids.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Updated by Soraia Meneses Alarcão on 21/07/2017
 */

public class Protection {
	
	private static MessageDigest md = null;
	
	static {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
    }

	private static char[] hexCodes(byte[] text) {

        char[] hexOutput = new char[text.length * 2];
        String hexString;

        for (int i = 0; i < text.length; i++) {
            hexString = "00" + Integer.toHexString(text[i]);
            hexString.toUpperCase().getChars(hexString.length() - 2, hexString.length(), hexOutput, i * 2);
        }
        return hexOutput;
    }

	public static String cypher(String pwd) {

        if (md != null) {
        	System.out.println("pwd received: " + pwd + " pwd cyphred " +
        			new String(hexCodes(md.digest(pwd.getBytes()))));

            return new String(hexCodes(md.digest(pwd.getBytes())));

        }
        return null;
    }

}




