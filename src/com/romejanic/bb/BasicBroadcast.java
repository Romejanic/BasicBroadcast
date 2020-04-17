package com.romejanic.bb;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.romejanic.bb.cmd.CommandBB;
import com.romejanic.bb.util.Config;
import com.romejanic.bb.util.Util;

public class BasicBroadcast extends JavaPlugin {

	private Config config;
	private boolean enabled = false;
	
	@Override
	public void onEnable() {
		this.config = new Config(this);
		this.getCommand("bb").setExecutor(new CommandBB());
		
		String period = this.config.getBroadcastPeriod();
		long periodSecs = Util.parsePeriodString(period);
		System.out.println("Period is set to '" + period + "' (i.e. " + periodSecs + " seconds)");
		
		getLogger().info("All set!");
		getLogger().info("If you like this plugin feel free to give it a star on GitHub: " + ChatColor.BOLD + "https://github.com/Romejanic/BasicBroadcast");
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Cleaned up plugin. Thanks for using!");
	}
	
}