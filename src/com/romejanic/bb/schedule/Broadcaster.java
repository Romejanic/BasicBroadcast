package com.romejanic.bb.schedule;

import org.bukkit.Bukkit;

import com.romejanic.bb.BasicBroadcast;
import com.romejanic.bb.util.Util;

public class Broadcaster implements Runnable {

	private final BasicBroadcast plugin;
	private int scheduleID = -1;
	
	
	
	public Broadcaster(BasicBroadcast plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		Bukkit.broadcastMessage("Broadcasting test");
	}

	public boolean schedule() {
		long secs = Util.parsePeriodString(this.plugin.config.getBroadcastPeriod());
		if(secs < 0l) return false;
		secs *= 20L; // convert number of seconds into number of ticks
		this.scheduleID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, this, 0l, secs);
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