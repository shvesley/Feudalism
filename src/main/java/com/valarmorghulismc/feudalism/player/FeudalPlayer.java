package com.valarmorghulismc.feudalism.player;

import com.valarmorghulismc.feudalism.Feudalism;
import com.valarmorghulismc.feudalism.Util;
import com.valarmorghulismc.feudalism.house.House;
import com.valarmorghulismc.feudalism.house.House.HouseRank;
import com.valarmorghulismc.feudalism.religion.Religion;
import com.valarmorghulismc.feudalism.religion.Religion.ReligionRank;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author ZNickq
 */
public final class FeudalPlayer {

	private static Map<String, FeudalPlayer> existing = new HashMap<String, FeudalPlayer>();

	public static void save(File dataFolder) throws FileNotFoundException, IOException {
		File mainFolder = new File(dataFolder, "players");
		mainFolder.mkdirs();

		for (FeudalPlayer fplr : existing.values()) {
			fplr.savePlayer(mainFolder, false);
		}
	}

	public static void init() {
		Feudalism.getInstance().getPlayerDataFolder().mkdirs();
	}
	private String name;
	private HouseRank rank;
	private ReligionRank rrank;
	private Religion religion;
	private String house;
	private String channel;
	private String invitation;
	private int rentPrice;

	public FeudalPlayer(String player) {
		name = player;
		rank = null;
		house = null;
		rrank = ReligionRank.FOLLOWER;
		invitation = "";
		channel = "global";
		setDisplayName();
	}

	protected FeudalPlayer() {
	}

	public void savePlayer(File mainFolder, boolean removeData) {
		if (removeData) {
			existing.remove(name);
		}
		try {
			File thisPlayer = new File(mainFolder, name + ".dat");
			if (thisPlayer.exists()) {
				thisPlayer.delete();
			}
			thisPlayer.getParentFile().mkdirs();
			thisPlayer.createNewFile();
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(thisPlayer));
			dos.writeInt(Feudalism.CURRENT_VERSION);
			dos.writeUTF(channel);
			dos.writeUTF(rank == null ? "[null]" : rank.name());
			dos.writeUTF(house == null ? "[null]" : house);
			dos.writeUTF(invitation == null ? "[null]" : invitation);
			dos.writeInt(religion == null? -1 : religion.getId());
			dos.writeUTF(rrank == null ? "[null]" : rrank.name());
			dos.writeInt(rentPrice);
			dos.flush();
			dos.close();
		} catch (IOException ex) {
			Logger.getLogger(FeudalPlayer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void loadPlayer(File mainFolder) {
		File thisPlayer = new File(mainFolder, name + ".dat");
		if (!thisPlayer.exists()) {
			return;
		}
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(new FileInputStream(thisPlayer));
			int version = dis.readInt();
			channel = "global";
			if(version > 1) {
				channel = dis.readUTF();
			}
			String lrank = dis.readUTF();
			String lhouse = dis.readUTF();
			String linvitation = dis.readUTF();
			Religion rell = Religion.getById(dis.readInt());
			ReligionRank lrrank = ReligionRank.valueOf(dis.readUTF());
			int lrentPrice = dis.readInt();
			if (!(lrank.equals("[null]"))) {
				rank = HouseRank.valueOf(lrank);
			}
			if (!(lhouse.equals("[null]"))) {
				house = lhouse;
			}
			if (!(linvitation.equals("[null]"))) {
				invitation = linvitation;
			}
			rentPrice = lrentPrice;
			rrank = lrrank;
			religion = rell;
		} catch (Exception ex) {
			Logger.getLogger(FeudalPlayer.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				dis.close();
			} catch (IOException ex) {
				Logger.getLogger(FeudalPlayer.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	
	public void setChannel(String nChannel) {
		channel = nChannel;
	}
	
	public String getChannel() {
		return channel;
	}

	public String getName() {
		return name;
	}

	public HouseRank getRank() {
		return rank;
	}

	public House getHouse() {
		House hh = House.getHouse(house);
		if(hh == null && house != null) {
			house = null;
			rank = null;
		}
		return hh;
	}
	
	public Religion getReligion() {
		if(house != null) {
			return getHouse().getReligion();
		}
		return religion;
	}
	
	public void setReligion(Religion nR) {
		this.religion = nR;
		this.rrank = ReligionRank.FOLLOWER;
	}

	public ReligionRank getReligionRank() {
		if(house == null && rrank != ReligionRank.FOLLOWER) {
			rrank = ReligionRank.FOLLOWER;
		}
		return rrank;
	}

	public void setReligionRank(ReligionRank newR) {
		rrank = newR;
		setDisplayName();
	}

	public void setRank(HouseRank newRank) {
		rank = newRank;
		setDisplayName();
	}

	public void setHouse(House newHouse, HouseRank newRank) {
		if (newHouse != null) {
			house = newHouse.getName();
		} else {
			house = null;
		}
		rank = newRank;
		rrank = ReligionRank.FOLLOWER;
		setDisplayName();
	}

	public void setInvitation(String newHouse) {
		invitation = newHouse;
	}

	public static FeudalPlayer getPlayer(String player, boolean create) {
		FeudalPlayer toRet = existing.get(player);
		if (toRet == null && create) {
			toRet = new FeudalPlayer(player);
			toRet.loadPlayer(new File(Feudalism.getInstance().getDataFolder(), "players"));
			existing.put(player, toRet);
		}
		return toRet;

	}

	public boolean createHouse(String name, String religion) {
		int id = Religion.getIdFromName(religion);
		if (id == -1) {
			return false;
		}
		Religion rel = Religion.getById(id);
		rel.setMoney(rel.getMoney()+Feudalism.getConfiguration().getHousePrice());
		House nhouse = new House(name, id);
		House.createHouse(nhouse);
		nhouse.addMember(this, HouseRank.LORD);
		return true;
	}

	public String getInvitation() {
		return invitation;
	}

	public int getRentPrice() {
		return rentPrice;
	}

	public void setRentPrice(int pp) {
		rentPrice = pp;
	}

	public void setDisplayName() {
		Player plr = Bukkit.getPlayer(name);
		if (plr == null) {
			return;
		}
		if(getHouse() == null && getReligionRank() != null) {
			if(getReligionRank() == ReligionRank.ARCHBISHOP) {
				plr.setDisplayName(getReligion().getPrefix()+" "+plr.getName()+" of "+getReligion().getName());
			}
		}
		String title = "" + ChatColor.WHITE;
		String hhouse = getHouse() != null ? getHouse().getName() : null;
		if (getRank() == null) {
			title += getName();
		} else if (getRank() == HouseRank.PEASANT) {
			title += Util.getChatFromDye(getHouse().getColor()) + getName() + " " + hhouse;
		} else if (getRank() == HouseRank.KNIGHT) {
			title += Util.getChatFromDye(getHouse().getColor()) + "Ser " + getName() + " " + getHouse().getName() + ChatColor.YELLOW;
		} else if (getRank() == HouseRank.LORD || getRank() == HouseRank.BARON) {
			try {
				title += Util.getChatFromDye(getHouse().getColor()) + getRank().getPrintName() + " " + getName() + " " + hhouse;
			} catch (NullPointerException ex) {
				System.out.println("Hmmm: " + getHouse());
				System.out.println("Hmmm: " + getRank());
				System.out.println("Hmmm: " + getName());
				ex.printStackTrace();
			}
		} else if (getRank() == HouseRank.CONSTABLE) {
			title += Util.getChatFromDye(getHouse().getColor()) + getRank().getPrintName() + " " + getName() + " " + hhouse;
		}
		if (!plr.getDisplayName().equals(title)) {
			plr.setDisplayName(title);
		}
	}
}
