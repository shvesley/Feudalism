/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valarmorghulismc.feudalism.chat;

import com.valarmorghulismc.feudalism.player.FeudalPlayer;
import org.bukkit.entity.Player;

/**
 *
 * @author ZNickq
 */
public class ConfigChannel extends ChatChannel{
	
	private String alias, perm;
	public ConfigChannel(String alias, String perm) {
		this.perm = perm;
		this.alias = alias;
	}
	
	@Override
	public String getAlias() {
		return alias;
	}

	@Override
	public boolean canSwitch(Player player, FeudalPlayer fplayer) {
		return player.hasPermission(perm);
	}

}
