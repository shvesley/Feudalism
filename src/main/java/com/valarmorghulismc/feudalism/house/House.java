package com.valarmorghulismc.feudalism.house;

import com.valarmorghulismc.feudalism.Feudalism;
import com.valarmorghulismc.feudalism.chunk.ChunkInfo;
import com.valarmorghulismc.feudalism.chunk.ChunkLocation;
import com.valarmorghulismc.feudalism.chunk.types.ChunkType;
import com.valarmorghulismc.feudalism.player.FeudalPlayer;
import com.valarmorghulismc.feudalism.religion.Religion;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

public class House {

	public enum HouseRank {

		LORD("Lord", 5), CONSTABLE("Constable", 4), BARON("Baron", 3), KNIGHT("Knight", 2), PEASANT("Peasant", 1);
		private String pname;
		private int power;

		private HouseRank(String pname, int power) {
			this.pname = pname;
			this.power = power;
		}

		public String getPrintName() {
			return pname;
		}

		public int getPower() {
			return power;
		}

		public boolean hasPower(HouseRank houseRank) {
			return power >= houseRank.power;
		}
	}
	private static Map<String, House> houses = new HashMap<String, House>();
	private Set<String> inIt = new HashSet<String>();
	private Set<ChunkLocation> ownedChunks = new HashSet<ChunkLocation>();
	private HouseStats hstats = new HouseStats();
	private Religion religion;
	private DyeColor color;
	private String name, words;
	private Treasury treasury;

	public House(String name, int religion) {
		this.name = name;
		this.words = "";
		color = DyeColor.WHITE;
		this.religion = Religion.getById(religion);
		this.treasury = new Treasury();
	}

	public static void load(File dataFolder) throws FileNotFoundException, IOException {
		File loadFile = new File(dataFolder, "houses.dat");
		if (!(loadFile.exists())) {
			return;
		}
		DataInputStream dis = new DataInputStream(new FileInputStream(loadFile));
		int version = dis.readInt();
		int hhouses = dis.readInt();
		for (int i = 1; i <= hhouses; i++) {
			String name = dis.readUTF();
			String words = dis.readUTF();
			DyeColor ccolor = DyeColor.getByData(dis.readByte());
			int rid = dis.readInt();
			House house = new House(name, rid);
			house.setWords(words);
			house.setColor(ccolor);
			int inItS = dis.readInt();
			for (int j = 1; j <= inItS; j++) {
				house.inIt.add(dis.readUTF());
			}
			inItS = dis.readInt();
			for (int j = 1; j <= inItS; j++) {
				String cworld = dis.readUTF();
				int x = dis.readInt();
				int z = dis.readInt();
				house.ownedChunks.add(new ChunkLocation(cworld, x, z));
			}
			house.hstats.load(dis);
			house.treasury.load(dis);
			houses.put(name, house);
		}
		dis.close();
	}

	public static void save(File dataFolder) throws IOException {
		File saveFile = new File(dataFolder, "houses.dat");
		if (saveFile.exists()) {
			saveFile.delete();
		}
		saveFile.createNewFile();
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(saveFile));
		dos.writeInt(Feudalism.CURRENT_VERSION);
		dos.writeInt(houses.size());
		for (House house : houses.values()) {
			dos.writeUTF(house.name);
			dos.writeUTF(house.words);
			dos.writeByte(house.color.getData());
			dos.writeInt(house.religion.getId());
			dos.writeInt(house.inIt.size());
			for (String inn : house.inIt) {
				dos.writeUTF(inn);
			}
			dos.writeInt(house.ownedChunks.size());
			for (ChunkLocation cll : house.ownedChunks) {
				dos.writeUTF(cll.getWorld());
				dos.writeInt(cll.getX());
				dos.writeInt(cll.getZ());
			}
			house.hstats.save(dos);
			house.treasury.save(dos);
		}
		dos.flush();
		dos.close();
	}

	public static void createHouse(House house) {
		houses.put(house.getName(), house);
	}

	public static House getHouse(String name) {
		return houses.get(name);
	}

	public static Map<String, House> getAllHouses() {
		return Collections.unmodifiableMap(houses);
	}

	public static House getHouse(ChunkLocation which) {
		for (House house : houses.values()) {
			if (house.ownsChunk(which)) {
				return house;
			}
		}
		return null;
	}

	void broadcastMessage(String string) {
		for (String plr : inIt) {
			Player player = Bukkit.getPlayer(plr);
			if (player == null) {
				continue;
			}
			if (!(player.isOnline())) {
				continue;
			}
			player.sendMessage(string);
		}
	}

	public int getSize() {
		return inIt.size();
	}

	public List<String> getMembers(HouseRank houseRank) {
		List<String> toRet = new ArrayList<String>();
		for (String smember : inIt) {
			FeudalPlayer member = FeudalPlayer.getPlayer(smember, true);
			if (member.getRank() == houseRank) {
				toRet.add(member.getName());
			}
		}
		return toRet;
	}

	public String getName() {
		return name;
	}

	public Treasury getTreasury() {
		return treasury;
	}

	public Set<ChunkLocation> getAllOwnedChunks() {
		return Collections.unmodifiableSet(ownedChunks);
	}

	public DyeColor getColor() {
		return color;
	}

	public void rename(String newName) {
		houses.remove(getName());
		name = newName;
		houses.put(name, this);
		for (String mm : inIt) {
			FeudalPlayer fplr = FeudalPlayer.getPlayer(mm, false);
			if (fplr != null) {
				fplr.setHouse(this, fplr.getRank());
			} else {
				fplr = FeudalPlayer.getPlayer(mm, true);
				fplr.setHouse(this, fplr.getRank());
				fplr.savePlayer(Feudalism.getInstance().getPlayerDataFolder(), true);
			}
		}
	}

	public void disband() {
		for (String mm : inIt) {
			FeudalPlayer fplr = FeudalPlayer.getPlayer(mm, false);
			if (fplr != null) {
				fplr.setHouse(null, null);
			}
		}
		House.houses.remove(getName());
	}

	public void setColor(DyeColor ncolor) {
		color = ncolor;
		for (String plr : inIt) {
			FeudalPlayer ffp = FeudalPlayer.getPlayer(plr, false);
			if (ffp != null) {
				ffp.setDisplayName();
			}
		}
	}

	public boolean isMember(String name) {
		return inIt.contains(name);
	}

	public void addMember(FeudalPlayer fplr) {
		addMember(fplr, HouseRank.PEASANT);
	}

	public void addMember(FeudalPlayer fplr, HouseRank rank) {
		fplr.setHouse(this, rank);
		inIt.add(fplr.getName());
		hstats.increase();
	}

	public void removeMember(FeudalPlayer fplr) {
		inIt.remove(fplr.getName());
		fplr.setHouse(null, null);
		hstats.decrease();
	}

	public void addChunk(ChunkLocation toCheck) {
		ownedChunks.add(toCheck);
	}

	public void removeChunk(ChunkLocation toCheck) {
		ownedChunks.remove(toCheck);
	}

	public boolean ownsChunk(ChunkLocation toCheck) {
		return ownedChunks.contains(toCheck);
	}

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
	}

	public HouseStats getHouseStats() {
		return hstats;
	}

	public int canClaim(ChunkLocation cl) {
		for (House house : houses.values()) {
			if (house.ownsChunk(cl)) {
				return 1;
			}
		}
		if (hstats.getTotal(true) < ownedChunks.size()) {
			return 2;
		}
		return 3;
	}

	public String getDebugStats() {
		return hstats.getTotal(false) + " : " + ownedChunks.size();
	}

	public boolean canSet(ChunkLocation cl, ChunkType type) {
		int max = hstats.get(type);
		int got = 0;
		for (ChunkLocation cll : ownedChunks) {
			if (ChunkInfo.getChunkInfo(cll).getType() != null && ChunkInfo.getChunkInfo(cll).getType().equals(type)) {
				got++;
			}
		}
		return max > got;
	}

	public Religion getReligion() {
		return religion;
	}

	public class HouseStats {

		private int total, tBonus;
		private Map<ChunkType, Integer> allowed = new HashMap<ChunkType, Integer>();

		public HouseStats() {
			total = 20;
			tBonus = 0;
			for (ChunkType call : ChunkType.getAll()) {
				allowed.put(call, call.getInitial());
			}
			/*defense = 10;
			 merchant = 10;
			 pasture = 4;
			 farm = 5;
			 throne = 1;*/
		}
		
		public void setBonus(int nBonus) {
			tBonus = nBonus;
		}

		public int getTotal(boolean countBonus) { 
			if(countBonus) {
				return total+tBonus;
			}
			return total;
		}

		public int get(ChunkType type) {
			return allowed.get(type);
		}

		public void increase() {
			/*total += 5;
			 pasture += 2;
			 farm += 2;
			 defense += 1;*/
			total += 5;
			for (ChunkType call : ChunkType.getAll()) {
				allowed.put(call, allowed.get(call) + call.getIncrease());
			}
		}

		public void decrease() {
			total -= 5;
			for (ChunkType call : ChunkType.getAll()) {
				allowed.put(call, allowed.get(call) - call.getIncrease());
			}
		}

		private void save(DataOutputStream dos) throws IOException {
			dos.writeInt(total);
			dos.writeInt(tBonus);
			dos.writeInt(allowed.size());
			for (ChunkType ttype : allowed.keySet()) {
				try {
					dos.writeUTF(ttype.getName());
					dos.writeInt(allowed.get(ttype));
				} catch (NullPointerException ex) {
					System.out.println("Wat: " + ttype);
					ex.printStackTrace();
				}
			}
		}

		private void load(DataInputStream dis) throws IOException {
			total = dis.readInt();
			tBonus = dis.readInt();
			int allowes = dis.readInt();
			for (int i = 1; i <= allowes; i++) {
				String sutf = dis.readUTF();
				int all = dis.readInt();
				allowed.put(ChunkType.getByName(sutf), all);
			}
		}
	}
}
