/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valarmorghulismc.feudalism.chunk;

import com.valarmorghulismc.feudalism.Feudalism;
import com.valarmorghulismc.feudalism.house.Treasury;
import java.util.Map;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author ZNickq
 */
public class RentTaxRunnable implements Runnable {

	public void run() {
		Map<ChunkLocation, ChunkInfo> allChunks = ChunkInfo.getAllChunks();
		for (ChunkLocation cl : allChunks.keySet()) {
			ChunkInfo ci = allChunks.get(cl);
			if (ci.getRenter() != null) {
				int price = ci.getRentPrice();
				EconomyResponse success = Feudalism.getEconomy().withdrawPlayer(ci.getRenter(), price);
				if (!success.transactionSuccess()) {
					Player plr = Bukkit.getPlayer(ci.getRenter());
					ci.setOnRent(true);
					ci.setRenter(null);
					if (plr != null) {
						plr.sendMessage(ChatColor.YELLOW + "You did not have enough money to pay the taxes for chunk " + cl.getX() + "," + cl.getZ() + ", so you were removed!");
					}
				} else {
					Treasury tt = cl.getHouse().getTreasury();
					int pp = tt.getHtp();
					int forThaBoss;
					try {
						forThaBoss = (price * pp) / 100;
					} catch (Exception ex) {
						forThaBoss = 0;
					}
					price -= forThaBoss;
					Feudalism.getEconomy().depositPlayer(ci.getOwner(), price);
					tt.addMoney(forThaBoss);

				}
			}
		}
	}
}
