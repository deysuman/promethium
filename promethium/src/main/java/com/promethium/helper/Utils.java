package com.promethium.helper;

import java.util.regex.Pattern;


public class Utils {	
	
	/* Pretty simple with Regular Expression (but note this is much less efficient and much harder to read than worpet's answer that uses an Apache Commons Utility) */
	// Regex of Ipv4
	private static final Pattern ipv4 = Pattern.compile(
	        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
	
	// Regex of Database Name
	private static final Pattern databaseName  = Pattern.compile("[0-9a-zA-Z$_]+");
	
	// Regex of Url	
	private static final Pattern urlPattern = Pattern.compile("[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

	
	// Validate Ipv4
	
	/*
	 * Require parameter are String 
	 * return parameter are boolean
	 * example data 0.0.0.0 is true
	 */
	
	public static boolean validateIpv4(final String ip) {
	    return ipv4.matcher(ip).matches();
	}

	
	// Validate databasename
	
	/*
	 * Require parameter are String 
	 * return parameter are boolean
	 * example data "my_db" is true
	 */
		
	public static boolean validatedbName(final String _dbname) {
	    return databaseName.matcher(_dbname).matches();
	}
	
	// Validate database port
	
	/*
	 * Require parameter are String 
	 * return parameter are boolean
	 * example data "8080" is true
	 */
		
	public static boolean validatePort(final String _dbport) {
		return _dbport.matches("-?\\d+(\\.\\d+)?");
	}
	
	// Validate url
	
	/*
	 * Require parameter are String 
	 * return parameter are boolean
	 * example data "http://google.com" is true
	 */
		
	public static boolean validUrl(final String _url) {
		return urlPattern.matcher(_url).matches();
	}
	
	// Validate int
	
	/*
	 * Require parameter are String 
	 * return parameter are boolean
	 * example data "1" is true
	 */
	
	public static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
}
