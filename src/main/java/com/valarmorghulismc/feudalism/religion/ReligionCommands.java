/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valarmorghulismc.feudalism.religion;

import com.valarmorghulismc.feudalism.Feudalism;
import com.valarmorghulismc.feudalism.chunk.ChunkInfo;
import com.valarmorghulismc.feudalism.chunk.ChunkLocation;
import com.valarmorghulismc.feudalism.house.House;
import com.valarmorghulismc.feudalism.player.FeudalPlayer;
import com.valarmorghulismc.feudalism.religion.Religion.ReligionRank;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author ZNickq
 */
public class ReligionCommands implements CommandExecutor {

	public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
		if (strings.length == 0) {
			cs.sendMessage(ChatColor.GOLD + "Religion Commands");
			cs.sendMessage(ChatColor.GOLD + "----------------");
			cs.sendMessage(ChatColor.AQUA + "/rel info: " + ChatColor.YELLOW + "View info about your religion");
			cs.sendMessage(ChatColor.AQUA + "/rel promote [player] [rank]: " + ChatColor.YELLOW + "Set a player's religion rank");
			cs.sendMessage(ChatColor.AQUA + "/rel set [new religion]: " + ChatColor.YELLOW + "Change your religion. This religion only applies if you're not in a house");
			return true;
		}
		if (!(cs instanceof Player)) {
			cs.sendMessage(ChatColor.RED + "Only players can use these commands!");
			return true;
		}
		Player plr = (Player) cs;
		FeudalPlayer fplr = FeudalPlayer.getPlayer(cs.getName(), true);
		ChunkLocation cll = new ChunkLocation(plr.getLocation());
		ChunkInfo cinfo = ChunkInfo.getChunkInfo(cll);
		Economy economy = Feudalism.getEconomy();
		String arg = strings[0];
		Religion rel = fplr.getReligion();
		if (rel == null) {
			plr.sendMessage(ChatColor.RED + "You need to have a religion to use the religion commands!");
			return true;
		}
		if (arg.equals("info")) {
			plr.sendMessage(ChatColor.GOLD + "Religion info: " + rel.getName());
			plr.sendMessage(ChatColor.GOLD + "----------------");
			plr.sendMessage(ChatColor.AQUA + "Your rank: " + ChatColor.YELLOW + fplr.getReligionRank().getPrintName());
		}
		if (arg.equals("set")) {
			if (strings.length < 2) {
				plr.sendMessage(ChatColor.RED + "You need to specify the new religion!");
				return true;
			}
			String rname = strings[1];
			Religion nrel = Religion.getById(Religion.getIdFromName(rname));
			if(nrel == null) {
				plr.sendMessage(ChatColor.RED + rname+" is not a valid religion!");
				return true;
			}
			if(fplr.getHouse() != null) {
				plr.sendMessage(ChatColor.RED+"You can't be in a religion, you're part of a house!");
			}
			fplr.setReligion(nrel);
			plr.sendMessage(ChatColor.GREEN+"Your religion is now "+fplr.getReligion().getName()+"!");
		}
		if (arg.equals("promote")) {
			if (strings.length < 3) {
				plr.sendMessage(ChatColor.RED + "You need to specify a player, and a rank!");
				return true;
			}
			String tplr = strings[1];
			String trank = strings[2];

			if (fplr.getReligionRank() != ReligionRank.ARCHBISHOP) {
				plr.sendMessage(ChatColor.RED + "You are not an archbishop!");
				return true;
			}
			FeudalPlayer tfplr = FeudalPlayer.getPlayer(tplr, false);
			if (tfplr == null) {
				plr.sendMessage(ChatColor.RED + "That player doesn't exist!");
				return true;
			}
			Religion hh2 = tfplr.getReligion();
			if (hh2 == null) {
				plr.sendMessage(ChatColor.RED + "That player doesn't have a religion!");
				return true;
			}
			if (!(hh2.getName().equals(fplr.getHouse().getReligion().getName()))) {
				plr.sendMessage(ChatColor.RED + "That player's religion is different from yours!");
				return true;
			}
			ReligionRank rr;
			try {
				rr = ReligionRank.valueOf(trank);
			} catch (Exception ex) {
				plr.sendMessage(ChatColor.RED + trank + " isn't a valid religion!");
				return true;
			}
			if (rr == ReligionRank.ARCHBISHOP) {
				plr.sendMessage(ChatColor.RED + "You can't set another archbishop!");
				return true;
			}
			if(tfplr.getHouse() != null) {
				plr.sendMessage(ChatColor.RED+"That player is in a house, he can't be anything other than a follower!");
				return true;
			}
			tfplr.setReligionRank(rr);
			plr.sendMessage(ChatColor.GOLD + tfplr.getName() + " is now a " + rr.getPrintName() + "!");
			Player tbplr = Bukkit.getPlayer(tfplr.getName());
			if (tbplr != null) {
				tbplr.sendMessage(ChatColor.GOLD + "You are now a " + rr.getPrintName() + "!");
			}
		}

		return true;
	}
}
