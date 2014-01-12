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
public class ReligionChannel extends ChatChannel{

	@Override
	public String getAlias() {
		return "h";
	}

	@Override
	public boolean canSwitch(Player player, FeudalPlayer fplayer) {
		return fplayer.getReligion() != null;
	}

	@Override
	public boolean canReceive(FeudalPlayer receiver, FeudalPlayer transmitter) {
		if(receiver.getReligion() == null && transmitter.getReligion() != null) {
			return false;
		}
		if(receiver.getReligion() != null && transmitter.getReligion() == null) {
			return false;
		}
		if(receiver.getReligion() == null && transmitter.getReligion() == null) {
			return false;
		}
		return receiver.getReligion().getName().equals(transmitter.getReligion().getName());
	}
	
}
