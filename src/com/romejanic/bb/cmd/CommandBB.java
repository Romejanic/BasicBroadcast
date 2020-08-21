package com.romejanic.bb.cmd;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import com.romejanic.bb.BasicBroadcast;
import com.romejanic.bb.update.UpdateChecker;
import com.romejanic.bb.util.Config;
import com.romejanic.bb.util.Util;

public class CommandBB implements CommandExecutor {

	private final BasicBroadcast plugin;
	
	public CommandBB(BasicBroadcast plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!Util.hasPermission("config", sender)) {
			sender.sendMessage(ChatColor.RED + "Sorry, you don't have permission to use this command.");
			return true;
		}
		if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
			sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <list|reload|version|changes>");
		} else {
			Config config = this.plugin.config;
			switch(args[0].toLowerCase()) {
			case "say":
				if(args.length <= 1) {
					sender.sendMessage(ChatColor.RED + "Usage: /" + label + " say <message>");
				} else {
					String msg = Util.join(args, 1, " ").trim();
					String[] formatted = config.formatBroadcastMessage(msg);
					for(String line : formatted) {
						Bukkit.broadcastMessage(line);
					}
				}
				break;
			case "list":
				sender.sendMessage(ChatColor.BOLD + "List of broadcast messages:");
				Iterator<String[]> iter = config.getMessageIterator();
				while(iter.hasNext()) {
					sender.sendMessage("- " + Util.join(iter.next(), ","));
				}
				break;
			case "reload":
				this.plugin.getLogger().info("Reloading plugin, please wait...");
				this.plugin.config.reload();
				if(this.plugin.reschedule()) {
					sender.sendMessage(ChatColor.GREEN + "Reload complete!");
				} else {
					sender.sendMessage(ChatColor.RED + "There was a problem scheduling the broadcaster. Please check your config to ensure your period is valid, then reload again.");
				}
				break;
			case "version":
				PluginDescriptionFile desc = this.plugin.getDescription();
				String name = desc.getFullName();
				String authors = Util.join(desc.getAuthors(), ", ");
				sender.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + name);
				sender.sendMessage(ChatColor.GREEN + "by " + authors);
				checkForUpdates(sender, label);
				break;
			case "changes":
				if(args.length <= 1) {
					sender.sendMessage(ChatColor.RED + "Usage: /" + label + " changes <version>");
				} else {
					if(!UpdateChecker.hasChangelogs()) {
						checkForUpdates(null, null);
					}
					String version = args[1].toLowerCase().trim();
					String[] changes = UpdateChecker.getChangesFor(version);
					if(changes == null) {
						sender.sendMessage(ChatColor.RED + "Unknown version: " + version);
					} else {
						sender.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "== v" + version + " CHANGELOG ==");
						for(String change : changes) {
							sender.sendMessage(ChatColor.GREEN + change);
						}
					}
				}
				break;
			default:
				sender.sendMessage(ChatColor.RED + "Sub-command \"" + args[0] + "\" not recognized! Type /bb help for a list of commands.");
				break;
			}
		}
		return true;
	}

	private void checkForUpdates(CommandSender sender, String alias) {
		if(sender != null) sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "Checking for updates...");
		UpdateChecker.checkForUpdates(this.plugin, (status) -> {
			if(sender == null) return;
			if(status.didFail()) {
				sender.sendMessage(ChatColor.RED + "Error: " + status.error);
			} else if(status.isOutdated()) {
				sender.sendMessage(ChatColor.GREEN + "New version available! (v" + status.latestVersion + ")");
				sender.sendMessage(ChatColor.GREEN + "Download: " + status.latestURL);
				sender.sendMessage(ChatColor.GREEN + "Type " + ChatColor.BOLD + "/" + alias + " changes " + status.latestVersion + ChatColor.GREEN + " for a changelog");
			} else {
				sender.sendMessage(ChatColor.GREEN + "You are running the latest version!");
			}
		});
	}
	
}
