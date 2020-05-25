package com.romejanic.bb.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class UpdateChecker {

	private static final Gson GSON = new Gson();
	private static final String UPDATE_JSON = "https://raw.githubusercontent.com/Romejanic/Minecraft-Mod-Files/master/BasicBroadcast/updates.json";
	
	private static final HashMap<String,String[]> changelogs = new HashMap<String,String[]>();
	private static String latestVersion;
	private static String latestDownload;
	
	private static BukkitTask currentTask;
	
	public static void checkForUpdates(JavaPlugin plugin, Consumer<UpdateStatus> cb) {
		if(currentTask != null) return;
		currentTask = new BukkitRunnable() {
			@Override
			public void run() {
				try {
					// create http request
					URL url = new URL(UPDATE_JSON);
					HttpURLConnection http = (HttpURLConnection)url.openConnection();
					http.setRequestMethod("GET");
					
					// convert response to json object
					BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
					JsonObject updateJson = GSON.fromJson(reader, JsonObject.class);
					http.disconnect();
					
					// store changelogs
					changelogs.clear();
					for(Entry<String,JsonElement> e : updateJson.get("changes").getAsJsonObject().entrySet()) {
						JsonArray jarr = e.getValue().getAsJsonArray();
						String[] arr = new String[jarr.size()];
						for(int i = 0; i < arr.length; i++) {
							arr[i] = jarr.get(i).getAsString();
						}
						changelogs.put(e.getKey(), arr);
					}
					
					// store latest download
					latestDownload = updateJson.get("latest-download").getAsString();
					
					// finally, check if there's a new version
					latestVersion = updateJson.get("latest").getAsString();
					String currentVersion = plugin.getDescription().getVersion();
					
					if(currentVersion.compareTo(latestVersion) < 0) {
						cb.accept(new UpdateStatus(latestVersion, latestDownload, changelogs.get(latestVersion)));
					} else {
						cb.accept(new UpdateStatus());
					}
				} catch (IOException e) {
					plugin.getLogger().log(Level.WARNING, "Failed to check for updates!", e);
					cb.accept(new UpdateStatus("Error while checking for updates! Check the console for more info"));
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	
}