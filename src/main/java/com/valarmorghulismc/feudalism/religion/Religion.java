package com.valarmorghulismc.feudalism.religion;

import com.valarmorghulismc.feudalism.Feudalism;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author ZNickq
 */
public class Religion {

	public static void load(FileConfiguration config) {
		for (Religion rel : allReligions.values()) {
			String ll = config.getString("Religions." + rel.name + ".Leader", "Unknown");
			Double lmoney = config.getDouble("Religions." + rel.name + ".Money", 0);
			String prefix = config.getString("Religions."+rel.name+".Prefix", "Archbishop");
			if (ll.equals("Unknown")) {
				ll = config.getString("Religions.Leaders." + rel.name, "Unknown");
				config.set("Religions.Leaders." + rel.name, null);
			}
			rel.leader = ll;
			rel.money = lmoney;
			rel.prefix = prefix;
		}
	}

	public static void save(FileConfiguration config) {
		for (Religion rl : allReligions.values()) {
			config.set("Religions." + rl.name + ".Leader", rl.leader);
			config.set("Religions." + rl.name + ".Money", rl.money);
			config.set("Religions." + rl.name + ".Prefix", rl.prefix);
		}
		Feudalism.getInstance().saveConfig();
	}

	public enum ReligionRank {

		ARCHBISHOP("Archbishop"), PRIOR("Prior"), FRIAR("Friar"), NOVICE("Novice"), FOLLOWER("Follower");
		private String nname;

		private ReligionRank(String nname) {
			this.nname = nname;
		}

		public String getPrintName() {
			return nname;
		}
	};
	private static Map<Integer, Religion> allReligions = new HashMap<Integer, Religion>();
	public static Religion RHLLOR = new Religion("R'hllor", 0);
	public static Religion THESEVEN = new Religion("The Seven", 1);
	public static Religion THEOLDGODS = new Religion("The Old Gods", 2);
	public static Religion SKEPTICISM = new Religion("Skepticism", 3);
	public static Religion THEDROWNEDGOD = new Religion("The Drowned God", 3);
	private String name;
	private String leader;
	private String prefix;
	private int id;
	private double money;

	private Religion(String name, int id) {
		this.name = name;
		this.id = id;
		this.leader = "Unknown";
		this.prefix = "Baws";
		this.money = 0;
		allReligions.put(id, this);

	}

	public void setMoney(double nMoney) {
		money = nMoney;
	}

	public double getMoney() {
		return money;
	}

	public void setLeader(String nl) {
		this.leader = nl;
	}

	public String getLeader() {
		return leader;
	}

	public String getName() {
		return name;
	}

	public String getPrefix() {
		return prefix;
	}

	public Integer getId() {
		return id;
	}

	public static Religion getById(Integer id) {
		if (id == -1) {
			return null;
		}
		return allReligions.get(id);
	}

	public static int getIdFromName(String name) {
		for (Religion rel : allReligions.values()) {
			if (rel.getName().equalsIgnoreCase(name)) {
				return rel.getId();
			}
		}
		return -1;
	}

	public static Collection<Religion> getAll() {
		return allReligions.values();
	}
}