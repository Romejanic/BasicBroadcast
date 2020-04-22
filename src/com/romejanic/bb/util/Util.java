package com.romejanic.bb.util;

import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Util {

	public static String parseColors(String in) {
		return in.replaceAll("&", String.valueOf(ChatColor.COLOR_CHAR));
	}

	public static String join(List<String> list, String div) {
		StringBuilder sb = new StringBuilder();
		Iterator<String> iter = list.iterator();
		while(iter.hasNext()) {
			sb.append(iter.next());
			if(iter.hasNext()) {
				sb.append(div);
			}
		}
		return sb.toString();
	}
	
	public static String join(String[] arr, String div) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < arr.length; i++) {
			sb.append(arr[i]);
			if(i < arr.length - 1) {
				sb.append(div);
			}
		}
		return sb.toString();
	}
	
	public static boolean hasPermission(String perm, CommandSender sender) {
		return sender.isOp()
			|| sender.hasPermission("bb.*")
			|| sender.hasPermission("bb." + perm);
	}
	
	public static boolean isMultiline(String line) {
		int idx = line.indexOf('\\');
		return idx > -1 && idx < line.length()-1 && line.charAt(idx+1) == 'n';
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