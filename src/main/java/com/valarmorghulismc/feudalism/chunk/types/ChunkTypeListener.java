/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valarmorghulismc.feudalism.chunk.types;

import com.valarmorghulismc.feudalism.Feudalism;
import com.valarmorghulismc.feudalism.chunk.ChunkInfo;
import com.valarmorghulismc.feudalism.chunk.ChunkLocation;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author ZNickq
 */
public class ChunkTypeListener implements Listener{
	private Feudalism instance;
	
	public ChunkTypeListener(Feudalism instance) {
		this.instance = instance;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onTryFarm(PlayerInteractEvent event) {
		//System.out.println("Attempting farm!");
		if(!event.hasItem() || event.isCancelled() || event.getClickedBlock() == null || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			//System.out.println("Returning!");
			return;
		}
		if(isForFarming(event.getItem().getType()) && event.getClickedBlock().getType() == Material.SOIL) {
			//System.out.println("Is for farming, soil!");
			ChunkLocation curIn = new ChunkLocation(event.getClickedBlock().getLocation());
			ChunkInfo curInfo = ChunkInfo.getChunkInfo(curIn);
			if(curInfo.getType() == null || curInfo.getType() != ChunkType.FARM) {
				//System.out.println("Going on");
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.GOLD+"You can only plant crops on farms!");
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onTryShop(PlayerInteractEvent event) {
		if(event.isCancelled() || event.getClickedBlock() == null || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		if(event.getClickedBlock().getType() != Material.SIGN && event.getClickedBlock().getType() != Material.SIGN_POST) {
			return;
		}
		Sign sign = (Sign) event.getClickedBlock().getState();
		/*
		 * Following the format from here:
		 * http://dev.bukkit.org/server-mods/hyperconomy/pages/transaction-signs/
		 */
		String ss = sign.getLines()[1];
		ss = ChatColor.stripColor(ss);
		if(ss.equalsIgnoreCase("[sell]") || ss.equalsIgnoreCase("[buy]") || ss.equalsIgnoreCase("[trade]")) {
			ChunkLocation curIn = new ChunkLocation(event.getClickedBlock().getLocation());
			ChunkInfo curInfo = ChunkInfo.getChunkInfo(curIn);
			if(curInfo.getType() == null || curInfo.getType() != ChunkType.MERCHANT) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.GOLD+"You can't shop here, this isn't a merchant plot!");
			}
		}
	}

	private boolean isForFarming(Material type) {
		return type == Material.SEEDS || type == Material.MELON_SEEDS || type == Material.PUMPKIN_SEEDS || type == Material.CARROT || type == Material.SUGAR_CANE || type == Material.POTATO_ITEM;
	}
	
}
