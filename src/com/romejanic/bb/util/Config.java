package com.romejanic.bb.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {

	private final JavaPlugin plugin;
	
	private String cachedChatPrefix;
	private List<String> cachedMessages = new ArrayList<String>();
	
	public Config(JavaPlugin plugin) {
		plugin.saveDefaultConfig();
		this.plugin = plugin;
		this.cacheValues();
	}
	
	public void reload() {
		this.plugin.reloadConfig();
		this.cacheValues();
	}
	
	public void save() {
		// TODO: update values in config object
		this.plugin.saveConfig();
	}
	
	private void cacheValues() {
		this.cachedChatPrefix = Util.parseColors(this.getConfig().getString("broadcasting.prefix"));
		
		this.cachedMessages.clear();
		Iterator<?> iter = this.getConfig().getList("messages").iterator();
		while(iter.hasNext()) {
			Object next = iter.next();
			if(next instanceof String) {
				this.cachedMessages.add(Util.parseColors((String)next));
			}
		}
	}
	
	private FileConfiguration getConfig() {
		return this.plugin.getConfig();
	}
	
	//===========CONFIG GETTERS============//
	
	public List<String> getMessageList() {
		return this.cachedMessages;
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