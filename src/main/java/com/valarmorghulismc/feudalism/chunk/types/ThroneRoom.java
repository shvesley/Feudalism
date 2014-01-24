/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valarmorghulismc.feudalism.chunk.types;

import com.valarmorghulismc.feudalism.chunk.types.listeners.ThroneRoomListener;
import org.bukkit.event.Listener;

/**
 *
 * @author ZNickq
 */
public class ThroneRoom extends ChunkType{

	@Override
	public String getName() {
		return "ThroneRoom";
	}

	@Override
	public int getInitial() {
		return 1;
	}

	@Override
	public int getIncrease() {
		return 0;
	}

    @Override
    public Listener getListener(){
        return new ThroneRoomListener(); //Should only be called onEnable()
    }
}
