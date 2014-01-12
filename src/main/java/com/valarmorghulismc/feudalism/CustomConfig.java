package com.valarmorghulismc.feudalism;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomConfig {
	
	private JavaPlugin plugin;
	private File file;
	private FileConfiguration config;
	private String configName;
		
	public CustomConfig(JavaPlugin instance, File file, FileConfiguration config, String configName) {
		plugin = instance;
		
		this.file = file;
		this.config = config;
		this.configName = configName;
	}
	
	public void reloadCustomConfig(){
		if (file == null){
			file = new File(plugin.getDataFolder(), configName);
		}
		config = YamlConfiguration.loadConfiguration(file);
		
		InputStream defConfigStream = plugin.getResource(configName);
		if (defConfigStream != null){
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(file);
			config.setDefaults(defConfig);
		}
	}
	
	public FileConfiguration getCustomConfig(){
		if (config == null){
			this.reloadCustomConfig();
		}
		
		return config;
	}
	
	public void saveCustomConfig(){
		if (config == null || file == null){
			return;
		}
		
		try{
			this.getCustomConfig().save(file);
		}catch(IOException e){
			plugin.getLogger().log(Level.SEVERE, "Could not save config to " + file, e);
		}
	}
	
	public void saveDefualtCustomConfig(){
		if (file == null){
			file = new File(plugin.getDataFolder(), configName);
		}
		if (!file.exists()){
			plugin.saveResource(configName, false);
		}
	}
	
}
