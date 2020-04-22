package com.romejanic.bb.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {

	private final JavaPlugin plugin;
	
	private String cachedChatPrefix;
	private List<String[]> cachedMessages = new ArrayList<String[]>();
	
	public Config(JavaPlugin plugin) {
		plugin.saveDefaultConfig();
		this.plugin = plugin;
		this.cacheValues();
	}
	
	public void reload() {
		this.plugin.reloadConfig();
		this.cacheValues();
	}
	
	private void cacheValues() {
		this.cachedChatPrefix = Util.parseColors(this.getConfig().getString("broadcasting.prefix"));
		
		this.cachedMessages.clear();
		Iterator<?> iter = this.getConfig().getList("messages").iterator();
		while(iter.hasNext()) {
			Object next = iter.next();
			if(next instanceof String) {
				this.cachedMessages.add(formatBroadcastMessage((String)next));
			}
		}
	}
	
	private String[] formatBroadcastMessage(String msg) {
		msg = Util.parseColors(msg);
		String prefix = "";
		if(this.useChatPrefix()) {
			prefix = this.getChatPrefix() + " ";
			if(this.shouldResetColour()) {
				prefix += ChatColor.RESET;
			}
			msg = prefix + msg;
		}
		List<String> temp = new ArrayList<String>();
		int i = msg.indexOf('\\');
		while(i > -1 && i < msg.length()-1 && msg.charAt(i+1) == 'n') {
			temp.add(msg.substring(0,i).trim());
			msg = prefix + msg.substring(i+2).trim();
			i = msg.indexOf('\\');
		}
		temp.add(msg);
		return temp.toArray(new String[temp.size()]);
	}
	
	private FileConfiguration getConfig() {
		return this.plugin.getConfig();
	}
	
	//===========CONFIG GETTERS============//
	
	public int getMessageCount() {
		return this.cachedMessages.size();
	}
	
	public Iterator<String[]> getMessageIterator() {
		return this.cachedMessages.iterator();
	}
	
	public String[] getMessageAt(int i) {
		return this.cachedMessages.get(i);
	}

	public String getChatPrefix() {
		return this.cachedChatPrefix;
	}
	
	public boolean useChatPrefix() {
		return this.getConfig().getBoolean("broadcasting.use-prefix", true);
	}
	
	public String getBroadcastPeriod() {
		return this.getConfig().getString("broadcasting.period");
	}
	
	public boolean shouldSelectRandom() {
		return this.getConfig().getBoolean("broadcasting.select-random", false);
	}
	
	public boolean shouldAvoidRepeats() {
		return this.getConfig().getBoolean("broadcasting.avoid-repeats", true);
	}
	
	public boolean shouldBroadcastWhenEmpty() {
		return this.getConfig().getBoolean("broadcasting.broadcast-when-empty", false);
	}
	
	public boolean shouldResetColour() {
		return this.getConfig().getBoolean("broadcasting.reset-colour", true);
	}
	
}