/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valarmorghulismc.feudalism.chunk;

import com.valarmorghulismc.feudalism.Feudalism;
import com.valarmorghulismc.feudalism.chunk.types.ChunkType;
import com.valarmorghulismc.feudalism.house.House;
import com.valarmorghulismc.feudalism.house.House.HouseRank;
import com.valarmorghulismc.feudalism.house.House.HouseStats;
import com.valarmorghulismc.feudalism.player.FeudalPlayer;
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
public class ChunkCommands implements CommandExecutor {

	public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
		if (strings.length == 0) {
			cs.sendMessage(ChatColor.GOLD + "Chunk Commands");
			cs.sendMessage(ChatColor.GOLD + "----------------");
			cs.sendMessage(ChatColor.AQUA + "/c info: " + ChatColor.YELLOW + "View info about the chunk you're currently in");
			cs.sendMessage(ChatColor.AQUA + "/c rent [info]: " + ChatColor.YELLOW + "Rent a chunk, or check its' renting info");
			cs.sendMessage(ChatColor.AQUA + "/c buy [info]: " + ChatColor.YELLOW + "Buy the chunk, or check its' selling info");
			cs.sendMessage(ChatColor.AQUA + "/c set: " + ChatColor.YELLOW + "Access all the setting commands");
			cs.sendMessage(ChatColor.AQUA + "/c limits: " + ChatColor.YELLOW + "View your house's limits");
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
		if (arg.equals("info")) {
			House h = cll.getHouse();
			plr.sendMessage(ChatColor.GOLD + "Chunk info: " + ChatColor.YELLOW + cll.getX() * 10 + " " + cll.getZ() * 10);
			plr.sendMessage(ChatColor.GOLD + "----------------");
			plr.sendMessage(ChatColor.AQUA + "House: " + ChatColor.YELLOW + (h != null ? h.getName() : "None"));
			plr.sendMessage(ChatColor.AQUA + "Type: " + ChatColor.YELLOW + (cinfo.getType() != null ? cinfo.getType().getName() : "None"));
			plr.sendMessage(ChatColor.AQUA + "Owner: " + ChatColor.YELLOW + (cinfo.getOwner() != null ? cinfo.getOwner() : "None"));
			plr.sendMessage(ChatColor.AQUA + "Renter: " + ChatColor.YELLOW + (cinfo.getRenter() != null ? cinfo.getRenter() : "None"));
			plr.sendMessage(ChatColor.AQUA + "Share state: " + ChatColor.YELLOW + cinfo.getShareStateName());
			plr.sendMessage(ChatColor.AQUA + "On sale: " + ChatColor.YELLOW + cinfo.isOnSale());
			plr.sendMessage(ChatColor.AQUA + "On rent: " + ChatColor.YELLOW + cinfo.isOnRent());
			return true;
		}
		if (arg.equals("set")) {
			if (strings.length == 1) {
				plr.sendMessage(ChatColor.GOLD + "Set Chunk Commands");
				plr.sendMessage(ChatColor.GOLD + "----------------");
				plr.sendMessage(ChatColor.AQUA+"/c set type [type] - "+ChatColor.YELLOW+"Set a chunk's type, or list all the possible types");
				plr.sendMessage(ChatColor.AQUA+"/c set share [private/house/all] - "+ChatColor.YELLOW+"Set the sharing level of the current chunk");
				plr.sendMessage(ChatColor.AQUA+"/c set rent [amount]- "+ChatColor.YELLOW+"Toggle whether the chunk is on rent, or set your renting price");
				plr.sendMessage(ChatColor.AQUA+"/c set price [amount] - "+ChatColor.YELLOW+"Set the selling price, without any arguments it will remove the chunk from sale");
				return true;
			}
			String sarg = strings[1];
			if (sarg.equals("type")) {
				return setType(strings, cinfo, plr, fplr, cll);
			}
			if (sarg.equals("share")) {
				return setShare(strings, cinfo, plr, cll);
			}
			if (sarg.equals("rent")) {
				return setRent(strings, cinfo, plr, fplr);
			}
			if (sarg.equals("price")) {
				return setPrice(strings, cinfo, plr, fplr, cll);
			}
		}
		if (arg.equals("rent")) {
			if (strings.length > 1) {
				if (strings[1].equals("info")) {
					if (cinfo.isOnRent() && cinfo.getRenter() == null) {
						plr.sendMessage(ChatColor.YELLOW + "This chunk is on rent, and is available for " + cinfo.getRentPrice() + "!");
					}
					if (cinfo.isOnRent() && cinfo.getRenter() != null) {
						plr.sendMessage(ChatColor.YELLOW + "This chunk is on rent, and is occupied!");
					}
					if (!cinfo.isOnRent()) {
						plr.sendMessage(ChatColor.RED + "This chunk isn't on rent!");
					}
				}
				return true;
			}
			House house = cll.getHouse();
			if (!cinfo.isOnRent() || fplr.getHouse() == null || !house.getName().equals(fplr.getHouse().getName())) {
				plr.sendMessage(ChatColor.RED + "You can't rent this chunk!");
				return true;
			}
			if (economy.getBalance(plr.getName()) < cinfo.getRentPrice()) {
				plr.sendMessage(ChatColor.RED + "Not enough money, you need " + cinfo.getRentPrice() + "!");
				return true;
			}
			economy.withdrawPlayer(plr.getName(), cinfo.getRentPrice());
			economy.depositPlayer(cinfo.getOwner(), cinfo.getRentPrice());

			cinfo.setOnRent(false);
			cinfo.setRenter(plr.getName());
			plr.sendMessage(ChatColor.YELLOW + "Rented chunk!");
			return true;
		}
		if (arg.equals("limits")) {
			House fhouse = fplr.getHouse();
			if(fhouse == null) {
				plr.sendMessage(ChatColor.RED+"You need to be in a house!");
				return true;
			}
			HouseStats hs = fhouse.getHouseStats();
			plr.sendMessage(ChatColor.GREEN+"House limits: ");
			for(ChunkType ct : ChunkType.getAll()) {
				plr.sendMessage(ChatColor.YELLOW+ct.getName()+": "+hs.get(ct));
			}
			return true;
		}
		if (arg.equals("buy")) {
			if (strings.length > 1) {
				if (strings[1].equals("info")) {
					if (cinfo.isOnSale()) {
						plr.sendMessage(ChatColor.YELLOW + "This chunk is on sale for " + cinfo.getSalePrice() + "!");
					} else {
						plr.sendMessage(ChatColor.RED + "This chunk is not on sale!");
					}
				}
			}
			House house = cll.getHouse();
			if (!cinfo.isOnSale() || house == null) {
				plr.sendMessage(ChatColor.RED + "This chunk is not on sale!");
				return true;
			}
			int salePrice = cinfo.getSalePrice();
			if (economy.getBalance(plr.getName()) < salePrice) {
				plr.sendMessage(ChatColor.RED + "Not enough money, you need " + salePrice + "!");
				return true;
			}
			String owner = cinfo.getOwner();
			economy.withdrawPlayer(plr.getName(), salePrice);
			economy.depositPlayer(owner, salePrice);
			plr.sendMessage(ChatColor.YELLOW + "Bought chunk for " + salePrice + "!");
			cinfo.setOnSale(false);
			cinfo.setOwner(plr.getName());
			return true;
		}
		return true;
	}

	private boolean setShare(String[] strings, ChunkInfo cinfo, Player plr, ChunkLocation cll) {
		if (strings.length == 2) {
			plr.sendMessage(ChatColor.RED + "You need to specify private/house/all!");
			return true;
		}
		if (cinfo.getOwner() == null || !cinfo.getOwner().equals(plr.getName())) {
			plr.sendMessage(ChatColor.RED + "You don't own this chunk!");
			return true;
		}
		String ff = strings[2];
		if (ff.equals("private")) {
			cinfo.setShareState(0);
			plr.sendMessage(ChatColor.YELLOW + "Only you or a renter can access this chunk!");
		}
		if (ff.equals("house")) {
			if (cll.getHouse() == null) {
				plr.sendMessage(ChatColor.RED + "This chunk hasn't been claimed by any house!");
				return true;
			}
			cinfo.setShareState(1);
			plr.sendMessage(ChatColor.YELLOW + "Anyone from House " + cll.getHouse().getName() + " can access this chunk!");
		}
		if (ff.equals("all")) {
			cinfo.setShareState(2);
			plr.sendMessage(ChatColor.YELLOW + "Anyone can access this chunk!");
		}
		return true;
	}

	private boolean setType(String[] strings, ChunkInfo cinfo, Player plr, FeudalPlayer fplr, ChunkLocation cll) {

		if (strings.length == 2) {
			ChunkType ctype = cinfo.getType();
			if (ctype == null) {
				String allTypes = "";
				plr.sendMessage(ChatColor.RED + "This chunk doesn't have a type!");
				for (ChunkType cctype : ChunkType.getAll()) {
					allTypes += cctype.getName() + ", ";
				}
				allTypes = allTypes.substring(0, allTypes.length() - 1);
				plr.sendMessage(ChatColor.RED + "Possible types: " + allTypes);
				return true;
			}
			plr.sendMessage(ChatColor.YELLOW + "This chunk is a " + ctype.getName() + "!");
			return true;
		}
		House house = fplr.getHouse();
		if (house == null || cll.getHouse() == null) {
			plr.sendMessage(ChatColor.RED + "You need to be in House " + cll.getHouse().getName() + "'s territory to do that!");
			return true;
		}
		if (!cll.getHouse().getName().equals(house.getName())) {
			plr.sendMessage(ChatColor.RED + "This plot doesn't belong to House " + cll.getHouse().getName() + "!");
			return true;
		}
		String type = strings[2];
		ChunkType ctype = ChunkType.getByName(type);
		if (ctype == null) {
			plr.sendMessage(ChatColor.RED + "Invalid plot type!");
			return true;
		}
		if(cinfo.getType() != null) {
			if(cinfo.getType() == ChunkType.THRONE_ROOM) {
				plr.sendMessage(ChatColor.RED + "You can't change the throne room's type!");
				return true;
			}
		}
		if (!(house.canSet(cll, ctype))) {
			plr.sendMessage(ChatColor.RED + "House " + house.getName() + " already has too many " + strings[2] + " plots!");
			return true;
		}
		cinfo.setType(ctype);
		plr.sendMessage(ChatColor.YELLOW + "This plot is now a " + strings[2] + "!");
		return true;
	}

	private boolean setRent(String[] strings, ChunkInfo cinfo, Player plr, FeudalPlayer fplr) {
		if (strings.length > 2) {
			String price = strings[2];
			try {
				Integer it = Integer.parseInt(price);
				fplr.setRentPrice(it);
				plr.sendMessage(ChatColor.RED + "Set your chunk rent price to " + it + "!");
			} catch (Exception ex) {
				plr.sendMessage(price + " is not a valid number!");
			}
			return true;
		}
		if (!cinfo.getOwner().equals(plr.getName())) {
			plr.sendMessage(ChatColor.RED + "You don't own this chunk!");
			return true;
		}
		if (!fplr.getRank().hasPower(HouseRank.BARON)) {
			plr.sendMessage(ChatColor.RED + "Only barons and higher are allowed to rent chunks!");
			return true;
		}
		boolean wasOnRent = cinfo.isOnRent();
		cinfo.setOnRent(!cinfo.isOnRent());
		if (wasOnRent) {
			plr.sendMessage(ChatColor.YELLOW + "Chunk is no longer on rent!");
		} else {
			plr.sendMessage(ChatColor.YELLOW + "Chunk is now on rent!");
		}
		return true;
	}

	private boolean setPrice(String[] strings, ChunkInfo cinfo, Player plr, FeudalPlayer fplr, ChunkLocation cll) {
		if (!(cll.isOwner(fplr))) {
			plr.sendMessage(ChatColor.RED + "You do not own this chunk!");
			return true;
		}
		if (strings.length == 2) {
			if(!cinfo.isOnSale()) {
				plr.sendMessage(ChatColor.RED+"This chunk is not on sale!");
				return true;
			}
			cinfo.setOnSale(false);
			plr.sendMessage(ChatColor.YELLOW+"Chunk no longer on sale!");
			return true;
		}
		try {
			int pp = Integer.parseInt(strings[2]);
			cinfo.setSalePrice(pp);
			cinfo.setOnSale(true);
			plr.sendMessage(ChatColor.GREEN + "Selling chunk for " + pp + "!");
		} catch (Exception ex) {
			plr.sendMessage(ChatColor.YELLOW + strings[2] + " is not a number!");
		}
		return true;
	}
}
