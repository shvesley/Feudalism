/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valarmorghulismc.feudalism.admin;

import com.valarmorghulismc.feudalism.Feudalism;
import com.valarmorghulismc.feudalism.house.House;
import com.valarmorghulismc.feudalism.player.FeudalPlayer;
import com.valarmorghulismc.feudalism.religion.Religion;
import com.valarmorghulismc.feudalism.religion.Religion.ReligionRank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author ZNickq
 */
public class AdminCommands implements CommandExecutor {

	public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
		if (!(cs.hasPermission("feudalism.admin"))) {
			return true;
		}
		if (strings.length == 0) {
			cs.sendMessage(ChatColor.GOLD + "Administration Commands ");
			cs.sendMessage(ChatColor.GOLD + "----------------");
			cs.sendMessage(ChatColor.AQUA + "/fa set: " + ChatColor.YELLOW + "Access all the setting commands");
			cs.sendMessage(ChatColor.AQUA + "/fa rename [house] [new name]" + ChatColor.YELLOW + "Rename a house!");
			cs.sendMessage(ChatColor.AQUA + "/fa remove [house] " + ChatColor.YELLOW + "Remove a house!");
			return true;
		}
		if(strings[0].equals("rename")) {
			if (strings.length < 3) {
				cs.sendMessage(ChatColor.GOLD + "You need to specify the current house name, and the new one!");
				return true;
			}
			String shouse = strings[1];
			String shouse2 = strings[2];
			House h = House.getHouse(shouse);
			House h2 = House.getHouse(shouse2);
			if(h == null) {
				cs.sendMessage(ChatColor.RED+"That house doesn't exist!");
				return true;
			}
			if(h2 != null) {
				cs.sendMessage(ChatColor.RED+"That house already exists!");
				return true;
			}
			h.rename(shouse2);
			cs.sendMessage(ChatColor.RED+"House renamed to "+shouse2);
			return true;
		}
		if(strings[0].equals("remove")) {
			if (strings.length == 1) {
				cs.sendMessage(ChatColor.GOLD + "You need to specify a house name!");
				return true;
			}
			String shouse = strings[1];
			House h = House.getHouse(shouse);
			if(h == null) {
				cs.sendMessage(ChatColor.RED+"That house doesn't exist!");
				return true;
			}
			h.disband();
			cs.sendMessage(ChatColor.RED+"House disbanded!");
			return true;
		}

		if (strings[0].equals("set")) {
			if (strings.length == 1) {
				cs.sendMessage(ChatColor.GOLD + "Administration Set Commands ");
				cs.sendMessage(ChatColor.GOLD + "----------------");
				cs.sendMessage(ChatColor.AQUA + "/fa set archbishop [player]: " + ChatColor.YELLOW + "Set that player as the archbishop of his religion.");
				cs.sendMessage(ChatColor.AQUA + "/fa set bonus [house] [amount]: " + ChatColor.YELLOW + "Set a house's chunk limit bonus!");
				return true;
			}
			String swhat = strings[1];
			if (swhat.equals("archbishop")) {
				return setArch((Player) cs, strings);
			}
			if (swhat.equals("bonus")) {
				return setBonus((Player) cs, strings);
			}

		}

		return true;
	}
	
	private boolean setBonus(Player who, String[] strings) {
		if (strings.length < 4) {
			who.sendMessage(ChatColor.RED + "You need to specify a house and a bonus!");
			return true;
		}
		String shouse = strings[2];
		String sbonus = strings[3];
		House house = House.getHouse(shouse);
		Integer ibonus;
		if(house == null) {
			who.sendMessage(ChatColor.RED+shouse+" is not a valid house!");
			return true;
		}
		try {
			ibonus = Integer.parseInt(sbonus);
		} catch(Exception ex) {
			who.sendMessage(ChatColor.RED+sbonus+" is not a valid number!");
			return true;
		}
		house.getHouseStats().setBonus(ibonus);
		who.sendMessage(ChatColor.GREEN+"Set "+house.getName()+"'s bonus to "+ibonus);
		return true;
	}

	private boolean setArch(Player who, String[] strings) {
		if (strings.length < 3) {
			who.sendMessage(ChatColor.RED + "You need to specify a player!");
			return true;
		}
		String player = strings[2];
		FeudalPlayer fplr = FeudalPlayer.getPlayer(player, false);
		if (fplr == null) {
			who.sendMessage(ChatColor.RED + "Player not found!");
			return true;
		}
		Religion rel = fplr.getReligion();
		if (rel == null) {
			who.sendMessage(ChatColor.RED + "That player doesn't have a religion!");
			return true;
		}
		fplr.setHouse(null, null);
		String ll = rel.getLeader();
		FeudalPlayer oldL = FeudalPlayer.getPlayer(ll, false);
		if (oldL != null) { //Wat
			for (World world : Bukkit.getWorlds()) {
				for (String perm : Feudalism.getConfiguration().getArchbishopPerms(rel)) {
					Feudalism.getPermission().playerRemove(world, oldL.getName(), perm);
				}
			}
			oldL.setReligionRank(ReligionRank.NOVICE);
		}
		Player tplr = Bukkit.getPlayer(fplr.getName());
		if(tplr != null) {
			tplr.sendMessage("Giving you arch perms now:");
		}
		for (World world : Bukkit.getWorlds()) {
			for (String perm : Feudalism.getConfiguration().getArchbishopPerms(rel)) {
				if(tplr != null) {
					tplr.sendMessage("Giving ya: "+perm);
				}
				Feudalism.getPermission().playerAdd(world, fplr.getName(), perm);
			}
		}
		fplr.setReligionRank(ReligionRank.ARCHBISHOP);
		who.sendMessage(ChatColor.GREEN + fplr.getName() + " is now the archbishop of " + rel.getName() + "!");
		return true;
	}
}

