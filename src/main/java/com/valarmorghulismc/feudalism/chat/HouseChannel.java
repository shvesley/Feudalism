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
public class HouseChannel extends ChatChannel{

	@Override
	public String getAlias() {
		return "h";
	}

	@Override
	public boolean canSwitch(Player player, FeudalPlayer fplayer) {
		return fplayer.getHouse() != null;
	}

	@Override
	public boolean canReceive(FeudalPlayer receiver, FeudalPlayer transmitter) {
		if(receiver.getHouse() == null && transmitter.getHouse() != null) {
			return false;
		}
		if(receiver.getHouse() != null && transmitter.getHouse() == null) {
			return false;
		}
		if(receiver.getHouse() == null && transmitter.getHouse() == null) {
			return false;
		}
		return receiver.getHouse().getName().equals(transmitter.getHouse().getName());
	}
	
}
