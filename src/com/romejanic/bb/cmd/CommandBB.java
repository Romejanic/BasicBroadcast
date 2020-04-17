package com.romejanic.bb.cmd;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandBB implements CommandExecutor {

	// add, remove, list, reload
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Usage: /bb <add|remove|list|reload> ...");
		} else {
			sender.sendMessage(Arrays.toString(args));
		}
		return true;
	}

}
