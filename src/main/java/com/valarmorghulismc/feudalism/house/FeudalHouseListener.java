package com.valarmorghulismc.feudalism.house;

import com.valarmorghulismc.feudalism.chunk.ChunkInfo;
import com.valarmorghulismc.feudalism.chunk.ChunkLocation;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author ZNickq
 */
public class FeudalHouseListener implements Listener {
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.getClickedBlock() == null) {
			return;
		}
		
		Location loc = event.getClickedBlock().getLocation();
		ChunkLocation cl = new ChunkLocation(loc);
		House hh = House.getHouse(cl);
		if(hh == null) {
			return;
		}
		ChunkInfo ci = ChunkInfo.getChunkInfo(cl);
		
		if(ci.getShareState() == 2) {
			return;
		}
		
		if(!hh.isMember(event.getPlayer().getName())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED+"This area belongs to another house!");
			return;
		}
		if(ci.getShareState() == 1) {
			return;
		}
		if(ci.getShareState() <2 && ci.getOwner() != null && !ci.getOwner().equals(event.getPlayer().getName())) {
			if(ci.getRenter() == null || !ci.getRenter().equals(event.getPlayer().getName())) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED+"This area belongs to another player!");
			}
		}
		
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Location loc = event.getBlock().getLocation();
		ChunkLocation cl = new ChunkLocation(loc);
		House hh = House.getHouse(cl);
		if(hh == null) {
			return;
		}
		ChunkInfo ci = ChunkInfo.getChunkInfo(cl);
		
		if(ci.getShareState() == 2) {
			return;
		}
		
		if(!hh.isMember(event.getPlayer().getName())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED+"This area belongs to another house!");
			return;
		}
		if(ci.getShareState() == 1) {
			return;
		}
		if(ci.getShareState() <2 && ci.getOwner() != null && !ci.getOwner().equals(event.getPlayer().getName())) {
			if(ci.getRenter() == null || !ci.getRenter().equals(event.getPlayer().getName())) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED+"This area belongs to another player!");
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Location loc = event.getBlock().getLocation();
		ChunkLocation cl = new ChunkLocation(loc);
		House hh = House.getHouse(cl);
		if(hh == null) {
			return;
		}
		ChunkInfo ci = ChunkInfo.getChunkInfo(cl);
		
		if(ci.getShareState() == 2) {
			return;
		}
		
		if(!hh.isMember(event.getPlayer().getName())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED+"This are belongs to another house!");
			return;
		}
		if(ci.getShareState() == 1) {
			return;
		}
		if(ci.getShareState() <2 && ci.getOwner() != null && !ci.getOwner().equals(event.getPlayer().getName())) {
			if(ci.getRenter() == null || !ci.getRenter().equals(event.getPlayer().getName())) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED+"This area belongs to another player!");
			}
		}
	}
	
}
