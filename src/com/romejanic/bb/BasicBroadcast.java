package com.romejanic.bb;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class BasicBroadcast extends JavaPlugin {

	@Override
	public void onEnable() {
		getLogger().info("All set!");
		getLogger().info("If you like this plugin feel free to give it a star on GitHub: " + ChatColor.BOLD + "https://github.com/Romejanic/BasicBroadcast");
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Cleaned up plugin. Thanks for using!");
	}
	
}