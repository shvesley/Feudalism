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
public class LocalChannel extends ChatChannel{

	@Override
	public boolean canSwitch(Player player, FeudalPlayer fplayer) {
		return true;
	}

	@Override
	public String getAlias() {
		return "l";
	}

	@Override
	public Integer getRange() {
		return 20;
	}
	
}
