/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valarmorghulismc.feudalism;

import com.valarmorghulismc.feudalism.religion.Religion;
import java.util.Arrays;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author ZNickq
 */
public class Configuration {
	private FileConfiguration config;
	
	public Configuration(Feudalism instance) {
		config = instance.getConfig();
		
		addDefaults();
		instance.saveConfig();
	}
	
	public int getTaxPeriod() {
		return config.getInt("tax-period");
	}

	public int getPastureTickInterval() {
		return config.getInt("pasture.tick-interval-in-mins");
	}
	
	public int getPastureSpawningAnimals() {
		return config.getInt("pasture.number-of-spawning-animals");
	}

	public int getPastureEntityLimit() {
		return config.getInt("pasture.entity-limit");
	}
	
	public List<String> getArchbishopPerms(Religion rel) {
		return config.getStringList("Religion."+rel.getName()+".ArchPerms");
	}
	
	private void addDefaults() {
		config.addDefault("tax-period", 10);
		config.addDefault("pasture.tick-interval-in-mins", 20);
		config.addDefault("pasture.number-of-spawning-animals", 2);
		config.addDefault("pasture.entity-limit", 10);
		config.addDefault("house.create-price", 100.0);
		for(Religion rel : Religion.getAll()) {
			config.addDefault("Religion." + rel.getName() + ".ArchPerms", Arrays.asList("perm1.node1", "perm2.node2"));
		}
		config.options().copyDefaults(true);
	}

	public double getHousePrice() {
		return config.getDouble("house.create-price");
	}
	
}
