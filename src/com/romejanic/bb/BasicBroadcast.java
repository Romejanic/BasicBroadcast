package com.romejanic.bb;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.romejanic.bb.util.Config;

public class BasicBroadcast extends JavaPlugin {

	private Config config;
	
	@Override
	public void onEnable() {
		this.config = new Config(this);
		getLogger().info("All set!");
		getLogger().info("If you like this plugin feel free to give it a star on GitHub: " + ChatColor.BOLD + "https://github.com/Romejanic/BasicBroadcast");
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Cleaned up plugin. Thanks for using!");
	}
	
}