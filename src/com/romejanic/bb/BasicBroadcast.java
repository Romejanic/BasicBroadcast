package com.romejanic.bb;

import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.romejanic.bb.cmd.CommandBB;
import com.romejanic.bb.schedule.Broadcaster;
import com.romejanic.bb.util.Config;

public class BasicBroadcast extends JavaPlugin {

	private static final int BSTATS_PLUGIN_ID = 7641;
	
	public Config config;
	private Broadcaster broadcaster;
	
	private boolean enabled = false;
	
	private Metrics metrics;
	
	@Override
	public void onEnable() {
		this.metrics = new Metrics(this, BSTATS_PLUGIN_ID);
		
		this.config = new Config(this, this.metrics);
		this.broadcaster = new Broadcaster(this);
		this.getCommand("bb").setExecutor(new CommandBB(this));
		
		this.reschedule();
		getLogger().info("If you like this plugin feel free to give it a star on GitHub: " + ChatColor.BOLD + "https://github.com/Romejanic/BasicBroadcast");
	}
	
	@Override
	public void onDisable() {
		this.broadcaster.unschedule();
		getLogger().info("Cleaned up plugin. Thanks for using!");
	}
	
	public boolean reschedule() {
		this.enabled = this.broadcaster.reschedule();
		if(this.enabled) getLogger().info("All set!");
		else getLogger().warning("There was a problem scheduling the broadcast! Check your config then run /bb reload");
		return this.enabled;
	}
	
}