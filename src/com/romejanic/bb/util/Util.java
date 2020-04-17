package com.romejanic.bb.util;

import org.bukkit.ChatColor;

public class Util {

	public static String parseColors(String in) {
		return in.replaceAll("&", String.valueOf(ChatColor.COLOR_CHAR));
	}
	
}