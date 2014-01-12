package com.valarmorghulismc.feudalism.house;

import com.valarmorghulismc.feudalism.Feudalism;
import com.valarmorghulismc.feudalism.Util;
import com.valarmorghulismc.feudalism.chunk.ChunkInfo;
import com.valarmorghulismc.feudalism.chunk.ChunkLocation;
import com.valarmorghulismc.feudalism.chunk.types.ChunkType;
import com.valarmorghulismc.feudalism.house.House.HouseRank;
import com.valarmorghulismc.feudalism.player.FeudalPlayer;
import com.valarmorghulismc.feudalism.religion.Religion;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author ZNickq
 */
public class HouseCommands implements CommandExecutor {

	public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
		if (strings.length == 0) {
			cs.sendMessage(ChatColor.GOLD + "House Commands");
			cs.sendMessage(ChatColor.GOLD + "----------------");
			cs.sendMessage(ChatColor.AQUA + "/h list: " + ChatColor.YELLOW + "List all the Houses");
			cs.sendMessage(ChatColor.AQUA + "/h info: " + ChatColor.YELLOW + "View info about the chunk you're currently in");
			cs.sendMessage(ChatColor.AQUA + "/h banish [player]: " + ChatColor.YELLOW + "Banish a player from your house!");
			cs.sendMessage(ChatColor.AQUA + "/h invite [player]: " + ChatColor.YELLOW + "Invite a player into your house");
			cs.sendMessage(ChatColor.AQUA + "/h accept: " + ChatColor.YELLOW + "Accept an invitation into a house");
			cs.sendMessage(ChatColor.AQUA + "/h create [name] [religion]: " + ChatColor.YELLOW + "Create a new house");
			cs.sendMessage(ChatColor.AQUA + "/h claim: " + ChatColor.YELLOW + "Claim the current chunk for your house");
			cs.sendMessage(ChatColor.AQUA + "/h unclaim: " + ChatColor.YELLOW + "Unclaim the current chunk");
			cs.sendMessage(ChatColor.AQUA + "/h betray: " + ChatColor.YELLOW + "Betray your current house");
			cs.sendMessage(ChatColor.AQUA + "/h disband: " + ChatColor.YELLOW + "Disband your current house");
			cs.sendMessage(ChatColor.AQUA + "/h ranks: " + ChatColor.YELLOW + "View info about the available ranks within a House");
			cs.sendMessage(ChatColor.AQUA + "/h promote [player] [rank]: " + ChatColor.YELLOW + "Set a player's rank");
			cs.sendMessage(ChatColor.AQUA + "/h set: " + ChatColor.YELLOW + "Access all the setting commands");
			return true;
		}
		if (!(cs instanceof Player)) {
			cs.sendMessage(ChatColor.RED + "Only players can use these commands!");
			return true;
		}
		Player plr = (Player) cs;
		FeudalPlayer fplr = FeudalPlayer.getPlayer(cs.getName(), true);
		String arg = strings[0];
		if (arg.equals("info")) {
			HouseRank hrk = fplr.getRank();
			if (hrk == null) {
				cs.sendMessage(ChatColor.YELLOW + "You are not in a House!");
				return true;
			}
			List<String> lords = fplr.getHouse().getMembers(HouseRank.LORD);
			String ll = "";
			for (String lord : lords) {
				ll += lord + ", ";
			}
			ll = ll.substring(0, ll.length() - 2);
			plr.sendMessage(ChatColor.GOLD + "House " + ChatColor.GOLD + fplr.getHouse().getName());
			plr.sendMessage(ChatColor.GOLD + "----------------");
			plr.sendMessage(ChatColor.GOLD + "Words: " + ChatColor.WHITE + fplr.getHouse().getWords());
			plr.sendMessage(ChatColor.GOLD + "Religion: " + ChatColor.WHITE + fplr.getHouse().getReligion().getName());
			plr.sendMessage(ChatColor.GOLD + "Lord: " + ChatColor.WHITE + ll);
			plr.sendMessage(ChatColor.GOLD + "Your rank: " + ChatColor.WHITE + hrk.getPrintName());
			plr.sendMessage(ChatColor.GOLD + "Total land claimed: " + ChatColor.WHITE + fplr.getHouse().getAllOwnedChunks().size() + " chunks");
			plr.sendMessage(ChatColor.GOLD + "Bank: " + ChatColor.WHITE + Feudalism.getEconomy().format(fplr.getHouse().getTreasury().getTotalMoney()));
			return true;
		}
		if (arg.equals("banish")) {
			HouseRank hrk = fplr.getRank();
			if(hrk == null) {
				cs.sendMessage(ChatColor.RED+"You are not in a house!");
				return true;
			}
			if(hrk != HouseRank.LORD) {
				cs.sendMessage(ChatColor.RED+"You must be a lord to do this!");
				return true;
			}
			if(strings.length < 2) {
				cs.sendMessage(ChatColor.RED + "You need to specify a name!");
				return true;
			}
			String who = strings[1];
			if(!fplr.getHouse().isMember(who)) {
				cs.sendMessage(ChatColor.RED+"That player is not in your house!");
				return true;
			}
			fplr.getHouse().removeMember(FeudalPlayer.getPlayer(who, true));
			cs.sendMessage(ChatColor.GREEN+"Player banished from your house!");
			return true;
		}
		if (arg.equals("invite")) {
			HouseRank hrk = fplr.getRank();
			if (strings.length < 2) {
				cs.sendMessage(ChatColor.RED + "You need to specify a name!");
				return true;
			}
			if (hrk == null) {
				cs.sendMessage(ChatColor.YELLOW + "You are not in a House!");
			} else {
				if (hrk.getPower() < HouseRank.CONSTABLE.getPower()) {
					cs.sendMessage(ChatColor.YELLOW + "You are not allowed to do that!");
					return true;
				}
				String toInvite = strings[1];
				Player oplr = Bukkit.getPlayer(toInvite);
				if (oplr == null) {
					cs.sendMessage(ChatColor.RED + "Player not found!");
					return true;
				}
				FeudalPlayer.getPlayer(oplr.getName(), true).setInvitation(fplr.getHouse().getName());
				cs.sendMessage(ChatColor.YELLOW + "Invited " + oplr.getName() + " to House " + fplr.getHouse().getName() + "!");
				oplr.sendMessage(ChatColor.YELLOW + "You have been invited to House " + fplr.getHouse().getName() + " by " + plr.getName() + "!");
				oplr.sendMessage(ChatColor.YELLOW + "Type /h accept to accept!");

			}
			return true;
		}
		if (arg.equals("accept")) {
			House house = House.getHouse(fplr.getInvitation());
			if (house == null) {
				cs.sendMessage(ChatColor.YELLOW + "You either don't have any pending invitation, or the house doesn't exist anymore!");
				return true;
			}
			house.broadcastMessage(ChatColor.YELLOW + fplr.getName() + " has joined House " + house.getName() + "!");
			house.addMember(fplr);
			cs.sendMessage(ChatColor.YELLOW + "Welcome to House " + house.getName() + "!");
			return true;
		}
		if (arg.equals("create")) {
			if (strings.length < 3) {
				cs.sendMessage(ChatColor.RED + "You need to specify a name and a religion!");
				return true;
			}
			House hrk = fplr.getHouse();
			if (hrk != null) {
				cs.sendMessage(ChatColor.YELLOW + "You are in a House already!");
			} else {
				String hname = "";
				for (int i = 2; i < strings.length; i++) {
					hname += strings[i] + " ";
				}
				hname = hname.trim();
				EconomyResponse er = Feudalism.getEconomy().withdrawPlayer(cs.getName(), Feudalism.getConfiguration().getHousePrice());
				if (!er.transactionSuccess()) {
					cs.sendMessage(ChatColor.YELLOW + "Not enough money to create house!");
					return true;
				}
				if (fplr.createHouse(strings[1], hname)) {
					cs.sendMessage(ChatColor.YELLOW + "House created!");
					return true;
				}
				cs.sendMessage(ChatColor.YELLOW + "Could not create house! Maybe you got the religion name wrong?");
				String ll = "";
				for (Religion r : Religion.getAll()) {
					ll += r.getName() + ", ";
				}
				ll = ll.substring(0, ll.length() - 1);
				cs.sendMessage(ChatColor.YELLOW + "Possible religions: " + ll);
				return true;
			}
			return true;

		}
		if (arg.equals("claim")) {
			House hrk = fplr.getHouse();
			if (hrk == null) {
				cs.sendMessage(ChatColor.YELLOW + "You are not in a House!");
				return true;
			}
			ChunkLocation cl = new ChunkLocation(plr.getLocation());
			if (fplr.getRank().getPower() < HouseRank.LORD.getPower()) {
				cs.sendMessage(ChatColor.YELLOW + "You need to be a lord or higher to do that!");
				return true;
			}
			int nr = hrk.canClaim(cl);
			if (nr == 3) {
				hrk.addChunk(cl);
				ChunkInfo cinfo = ChunkInfo.getChunkInfo(cl);
				cinfo.setOwner(fplr.getName());
				if (hrk.getAllOwnedChunks().size() == 1) {
					cinfo.setType(ChunkType.THRONE_ROOM);
					cs.sendMessage(ChatColor.GREEN + "Claimed the throne room!");
				} else {
					cs.sendMessage(ChatColor.GREEN + "Claimed chunk! " + hrk.getDebugStats());
				}
				hrk.broadcastMessage(ChatColor.GREEN + "New land has been claimed for your house at " + cl.getX() * 10 + ", " + cl.getZ() * 10 + "!");
			} else if (nr == 1) {
				cs.sendMessage(ChatColor.RED + "This chunk has already been claimed!");
			} else {
				cs.sendMessage(ChatColor.RED + "Can't claim more chunks!");
			}
			return true;
		}
		if (arg.equals("promote")) {
			return setRank(strings, plr, fplr);
		}
		if (arg.equals("unclaim")) {
			House hrk = fplr.getHouse();
			if (hrk == null) {
				cs.sendMessage(ChatColor.YELLOW + "You are not in a House!");
				return true;
			}
			ChunkLocation cl = new ChunkLocation(plr.getLocation());
			if (fplr.getRank().getPower() < HouseRank.LORD.getPower()) {
				cs.sendMessage(ChatColor.YELLOW + "You need to be a lord or higher to do that!");
				return true;
			}
			if (!(hrk.ownsChunk(cl))) {
				cs.sendMessage(ChatColor.YELLOW + "Your house doesn't own this chunk!");
			}
			hrk.removeChunk(cl);
			cs.sendMessage(ChatColor.YELLOW + "Unclaimed chunk!");
			return true;
		}
		if (arg.equals("set")) {
			if (strings.length == 1) {
				plr.sendMessage(ChatColor.GOLD + "Set House Commands");
				plr.sendMessage(ChatColor.GOLD + "----------------");
				plr.sendMessage(ChatColor.AQUA + "/h set visual [on/off] - " + ChatColor.YELLOW + "Toggle visual display on/off");
				plr.sendMessage(ChatColor.AQUA + "/h set color - " + ChatColor.YELLOW + "Set the house's color");
				plr.sendMessage(ChatColor.AQUA + "/h set words - " + ChatColor.YELLOW + "Set the house's words");
				return true;
			}
			String sarg = strings[1];
			if (sarg.equals("visual")) {
				return setVisual(strings, plr, fplr);
			}
			if (sarg.equals("color")) {
				return setColor(strings, plr, fplr);
			}
			if (sarg.equals("words")) {
				return setWords(strings, fplr, cs);
			}
		}
		if (arg.equals("disband")) {
			if (fplr.getRank() != HouseRank.LORD) {
				plr.sendMessage(ChatColor.YELLOW + "You're not the lord of this house!");
				return true;
			}
			fplr.getHouse().disband();
			plr.sendMessage(ChatColor.GREEN + "Disbanded house!");
		}
		if (arg.equals("betray")) {
			if (fplr.getRank() == null) {
				plr.sendMessage(ChatColor.YELLOW + "You are not currently in a house!");
				return true;
			}
			if (fplr.getRank() == HouseRank.LORD) {
				plr.sendMessage(ChatColor.YELLOW + "You are this house's lord, you can't betray it!");
				return true;
			}
			House house = fplr.getHouse();
			house.removeMember(fplr);
			house.broadcastMessage(ChatColor.AQUA + plr.getName() + ChatColor.YELLOW + " has betrayed House " + house.getName() + "!");
			plr.sendMessage(ChatColor.YELLOW + "Betrayed House " + house.getName() + "!");
		}
		if (arg.equals("ranks")) {
			plr.sendMessage(ChatColor.GOLD + "Description of ranks:");
			plr.sendMessage(ChatColor.AQUA + "Lord: " + ChatColor.YELLOW + "The founder/heir to the founder of the House.");
			plr.sendMessage(ChatColor.AQUA + "Constable: " + ChatColor.YELLOW + "Town assistant equivalent, acts in the Lord's name.");
			plr.sendMessage(ChatColor.AQUA + "Baron: " + ChatColor.YELLOW + "Players which have been granted fiefs from their Lord or Lady.");
			plr.sendMessage(ChatColor.AQUA + "Knight: " + ChatColor.YELLOW + "Given their own land, in return for combat services. ");
			plr.sendMessage(ChatColor.AQUA + "Peasant: " + ChatColor.YELLOW + "Members of the House which have been granted no land of their own, but instead work/pay taxes for land.");
			return true;
		}
		if (arg.equals("list")) {
			Map<String, House> allH = House.getAllHouses();
			Set<String> ss = allH.keySet();
			List<String> nl = new ArrayList<String>();
			nl.addAll(ss);
			Collections.sort(nl);
			plr.sendMessage(ChatColor.GOLD + "House List");
			plr.sendMessage(ChatColor.GOLD + "------------------");
			for (String houseName : nl) {
				House hh = allH.get(houseName);
				String initial = ChatColor.GREEN + hh.getName();
				if (!hh.getWords().isEmpty()) {
					initial += " - " + hh.getWords();
				}
				plr.sendMessage(initial + ": " + ChatColor.AQUA + hh.getSize() + " Members");
			}
			return true;
		}
		if (arg.equals("ally")) {
		}
		if (arg.equals("faulty")) {
		}
		return true;
	}

	private boolean setVisual(String[] strings, Player plr, FeudalPlayer fplr) {

		if (strings.length == 2) {
			plr.sendMessage(ChatColor.YELLOW + "You need to specify on/off!");
			return true;
		}
		String ss = strings[2];
		boolean on;
		if (ss.equals("on")) {
			on = true;
		} else if (ss.equals("off")) {
			on = false;
		} else {
			plr.sendMessage(ChatColor.YELLOW + "Invalid state!");
			return true;
		}
		House hrk = fplr.getHouse();
		if (hrk == null) {
			plr.sendMessage(ChatColor.YELLOW + "You are not in a House!");
			return true;
		}
		plr.sendMessage(ChatColor.YELLOW + "Toggling visual grid!");
		Location loc = plr.getLocation();
		for (ChunkLocation cll : hrk.getAllOwnedChunks()) {
			int bx = cll.getX() * 10;
			int bz = cll.getZ() * 10;
			int by = 128;
			loc.setY(by);
			int xense = bx > -1 ? 1 : -1;
			int zense = bz > -1 ? 1 : -1;
			for (int i = bx; i != bx + 10 * xense; i += xense) {
				for (int j = bz; j != bz + 10 * zense; j += zense) {
					loc.setX(i);
					loc.setZ(j);
					if (on) {
						plr.sendBlockChange(loc, Material.WOOL, hrk.getColor().getWoolData());
					} else {
						plr.sendBlockChange(loc, plr.getWorld().getBlockTypeIdAt(loc), hrk.getColor().getWoolData());
					}
				}
			}
		}
		return true;
	}

	private boolean setWords(String[] strings, FeudalPlayer fplr, CommandSender cs) {
		String words = "";
		for (int i = 2; i < strings.length; i++) {
			words += strings[i] + " ";
		}
		House hrk = fplr.getHouse();
		if (hrk == null) {
			cs.sendMessage(ChatColor.YELLOW + "You are not in a house right now!");
			return true;
		}
		if (!fplr.getRank().hasPower(HouseRank.CONSTABLE)) {
			cs.sendMessage(ChatColor.YELLOW + "You are not powerful enough in your house!");
			return true;
		}
		hrk.setWords(words);
		cs.sendMessage(ChatColor.YELLOW + "Set House " + hrk.getName() + "'s words!");
		return true;
	}

	private boolean setRank(String[] strings, Player plr, FeudalPlayer fplr) {
		if (strings.length < 3) {
			plr.sendMessage(ChatColor.YELLOW + "You need to specify a player's name and a rank!");
			return true;
		}
		String playerName = strings[1];
		String rank = strings[2];
		House phouse = fplr.getHouse();
		if (phouse == null) {
			plr.sendMessage(ChatColor.YELLOW + "You are not in a house!");
			return true;
		}
		if (!fplr.getRank().hasPower(HouseRank.CONSTABLE)) {
			plr.sendMessage(ChatColor.YELLOW + "You are not powerful enough in House " + phouse.getName() + "!");
			return true;
		}
		FeudalPlayer ofplr = FeudalPlayer.getPlayer(playerName, false);
		Player oplr = Bukkit.getPlayer(playerName);
		if (oplr == null) {
			plr.sendMessage(ChatColor.YELLOW + "Player " + playerName + " not found!");
			return true;
		}
		if (ofplr.getRank() != null) {
			if (ofplr.getRank() == HouseRank.LORD) {
				plr.sendMessage(ChatColor.YELLOW + "The house's lord can't be demoted!");
				return true;
			}
		}
		try {
			HouseRank toPromote = HouseRank.valueOf(rank.toUpperCase());
			if ((toPromote == HouseRank.CONSTABLE || toPromote == HouseRank.LORD) && !(fplr.getRank() == HouseRank.LORD)) {
				plr.sendMessage(ChatColor.RED + "You can't promote someone that high!");
				return true;
			}
			if (toPromote == HouseRank.LORD) {
				plr.sendMessage(ChatColor.RED + "There can only be one lord!");
				return true;
			}
			ofplr.setRank(toPromote);
			plr.sendMessage(ChatColor.YELLOW + playerName + "'s rank is now " + ofplr.getRank().getPrintName() + "!");
			if (oplr != null) {
				oplr.sendMessage(ChatColor.YELLOW + "Your rank is now " + ofplr.getRank().getPrintName() + "!");
			}

			return true;
		} catch (IllegalArgumentException ex) {
			plr.sendMessage(ChatColor.YELLOW + "Invalid rank!");
			return true;
		}
	}

	private boolean setColor(String[] strings, Player plr, FeudalPlayer fplr) {
		if (strings.length < 3) {
			String ww = "";
			for (DyeColor dc : DyeColor.values()) {
				if (dc == DyeColor.GRAY) {
					ww += Util.getChatFromDye(dc) + "grey, ";
				}
				ww += Util.getChatFromDye(dc) + dc.name().toLowerCase() + ", ";
			}
			ww = ww.substring(0, ww.length() - 1);
			plr.sendMessage(ChatColor.YELLOW + "You need to specify a color! Possible colors: " + ww);
			return true;
		}
		String cname = strings[2];
		if (cname.equalsIgnoreCase("gray")) { //Why...just why?
			cname = "grey";
		}
		House hrk = fplr.getHouse();
		if (hrk == null) {
			plr.sendMessage(ChatColor.YELLOW + "You are not in a house right now!");
			return true;
		}
		if (!fplr.getRank().hasPower(HouseRank.CONSTABLE)) {
			plr.sendMessage(ChatColor.YELLOW + "You are not powerful enough in your house!");
			return true;
		}
		try {
			DyeColor dc;
			if (cname.equals("grey")) {
				dc = DyeColor.GRAY;
			} else {
				dc = DyeColor.valueOf(cname.toUpperCase());
			}
			hrk.setColor(dc);
			plr.sendMessage(ChatColor.YELLOW + "Set house's color to " + dc.name().toLowerCase() + "!");
			return true;
		} catch (Exception ex) {
			plr.sendMessage(ChatColor.YELLOW + "Invalid color!");
			return true;
		}
	}
}
