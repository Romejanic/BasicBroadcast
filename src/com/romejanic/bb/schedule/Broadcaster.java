package com.romejanic.bb.schedule;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.romejanic.bb.BasicBroadcast;
import com.romejanic.bb.util.Config;
import com.romejanic.bb.util.Util;

public class Broadcaster implements Runnable {

	private static final Random RANDOM = new Random();
	
	private final BasicBroadcast plugin;
	private final Config config;
	
	private int scheduleID = -1;
	
	private int index = 0;
	
	public Broadcaster(BasicBroadcast plugin) {
		this.plugin = plugin;
		this.config = plugin.config;
	}
	
	@Override
	public void run() {
		if(!this.config.shouldBroadcastWhenEmpty() && Bukkit.getOnlinePlayers().isEmpty()) return;
		
		boolean random = this.config.shouldSelectRandom();
		int idx = -1;
		
		if(random) { // implement select-at-random
			boolean avoidRepeats = this.config.shouldAvoidRepeats();
			do {
				idx = RANDOM.nextInt(this.config.getMessageCount());
			} while((!avoidRepeats && idx < 0) || idx == this.index);
			this.index = idx;
		} else {    // implement round-robin
			idx = this.index;
			this.index++;
			if(this.index > this.config.getMessageCount()) {
				this.index = 0;
			}
		}
		
		Bukkit.broadcastMessage(this.config.getMessageAt(idx));
	}

	public boolean schedule() {
		long secs = Util.parsePeriodString(this.plugin.config.getBroadcastPeriod());
		if(secs < 0l) return false;
		long ticks = secs * 20L; // convert number of seconds into number of ticks
		this.plugin.getLogger().info("Broadcasting every " + this.plugin.config.getBroadcastPeriod() + " (" + secs + "s, " + ticks + "t)");
		this.scheduleID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, this, 0l, ticks);
		return this.isActive();
	}
	
	public void unschedule() {
		if(!this.isActive()) return;
		Bukkit.getScheduler().cancelTask(this.scheduleID);
	}
	
	public boolean reschedule() {
		this.unschedule();
		return this.schedule();
	}
	
	public boolean isActive() {
		return this.scheduleID > -1;
	}
	
}