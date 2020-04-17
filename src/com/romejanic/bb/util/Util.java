package com.romejanic.bb.util;

import org.bukkit.ChatColor;

public class Util {

	public static String parseColors(String in) {
		return in.replaceAll("&", String.valueOf(ChatColor.COLOR_CHAR));
	}

	/**
	 * Parses period string from configuration into a number
	 * of seconds.
	 * 
	 * E.g. "5 min" -> 300
	 *      "3days" -> 4,320
	 * 
	 * @param in The period string to parse.
	 * @return The period represented as seconds, or -1 if input is invalid.
	 */
	public static long parsePeriodString(String in) {
		int numEnd = 0;
		while(Character.isDigit(in.charAt(numEnd))) {
			numEnd++;
		}
		if(numEnd == 0) return -1; // error: no number at start
		int periodLen = Integer.parseInt(in.substring(0, numEnd));
		String periodUnit = in.substring(numEnd).trim().toLowerCase();
		switch(periodUnit) {
		case "sec":
			return periodLen;
		case "min":
			return periodLen * 60;
		case "hr":
			return periodLen * 60 * 60;
		case "days":
			return periodLen * 60 * 60 * 24;
		default:
			return -1; // error: invalid unit entered
		}
	}

}