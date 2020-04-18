package com.romejanic.bb.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.romejanic.bb.BasicBroadcast;

public class CommandBB implements CommandExecutor {

	private final BasicBroadcast plugin;
	
	public CommandBB(BasicBroadcast plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender.isOp() || sender.hasPermission("bb.config")) {
			sender.sendMessage(ChatColor.RED + "Sorry, you don't have permission to use this command.");
			return true;
		}
		if(args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Usage: /bb <add|remove|list|reload> ...");
		} else {
			switch(args[0].toLowerCase()) {
			case "reload":
				this.plugin.getLogger().info("Reloading plugin, please wait...");
				this.plugin.config.reload();
				if(this.plugin.reschedule()) {
					sender.sendMessage(ChatColor.GREEN + "Reload complete!");
				} else {
					sender.sendMessage(ChatColor.RED + "There was a problem scheduling the broadcaster. Please check your config to ensure your period is valid, then reload again.");
				}
				break;
			default:
				sender.sendMessage(ChatColor.RED + "Sub-command \"" + args[0] + "\" not recognized! Type /bb help for a list of commands.");
				break;
			}
		}
		return true;
	}

}
