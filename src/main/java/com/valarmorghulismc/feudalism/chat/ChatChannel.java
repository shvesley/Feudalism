/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valarmorghulismc.feudalism.chat;

import com.valarmorghulismc.feudalism.player.FeudalPlayer;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author ZNickq
 */
public abstract class ChatChannel {
	private static Map<String, ChatChannel> loadedChannels = new HashMap<String, ChatChannel>();
	
	public boolean canSwitch(Player player, FeudalPlayer fplayer) {
		return true;
	}
	
	public boolean canReceive(FeudalPlayer receiver, FeudalPlayer transmitter) {
		return true;
	}
	
	public abstract String getAlias();

	public Integer getRange() {
		return 0;
	}
	
	public static void load(File dataFolder) {
		loadedChannels.put("global", new GlobalChannel());
		loadedChannels.put("house", new HouseChannel());
		loadedChannels.put("local", new LocalChannel());
		loadedChannels.put("religion", new ReligionChannel());
		File channels = new File(dataFolder, "channels.yml");
		if(!channels.exists()) {
			try {
				channels.createNewFile();
			} catch (IOException ex) {
				Logger.getLogger(ChatChannel.class.getName()).log(Level.SEVERE, null, ex);
			}
			YamlConfiguration yc = YamlConfiguration.loadConfiguration(channels);
			addDefaults(yc);
		}
	}
	
	private static void addDefaults(YamlConfiguration yc) {
	}
}
