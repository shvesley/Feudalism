/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valarmorghulismc.feudalism.house;

import com.valarmorghulismc.feudalism.chunk.ChunkLocation;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author ZNickq
 */
public class ClaimedLandTicker implements Runnable {

	private Map<String, Location> lastSeen = new HashMap<String, Location>();

	public void run() {
		//System.out.println("Ticked");
		for (Player onlinePlr : Bukkit.getOnlinePlayers()) {
			Location ls = lastSeen.get(onlinePlr.getName());
			if (ls == null) {
				lastSeen.put(onlinePlr.getName(), onlinePlr.getLocation());
				continue;
			}
			ChunkLocation from = new ChunkLocation(ls);
			ChunkLocation to = new ChunkLocation(onlinePlr.getLocation());
			//System.out.println(from.getX() + " " + from.getZ() + " to " + to.getX() + " " + to.getZ());
			if (from.equals(to)) {
				lastSeen.put(onlinePlr.getName(), onlinePlr.getLocation());
				continue;
			}
			House fromh = House.getHouse(from);
			House toh = House.getHouse(to);
			if (toh == null) {
				if (fromh != null) {
					onlinePlr.sendMessage(ChatColor.BLUE + "You are now entering unclaimed land!");
				}
				lastSeen.put(onlinePlr.getName(), onlinePlr.getLocation());
				continue;
			}
			if (fromh != null && fromh.getName().equals(toh.getName())) {
				lastSeen.put(onlinePlr.getName(), onlinePlr.getLocation());
				continue;
			}
			onlinePlr.sendMessage(ChatColor.BLUE + "You are now entering the territory of " + ChatColor.GREEN + "House " + toh.getName() + " - " + toh.getWords());
			lastSeen.put(onlinePlr.getName(), onlinePlr.getLocation());
		}
	}
}
