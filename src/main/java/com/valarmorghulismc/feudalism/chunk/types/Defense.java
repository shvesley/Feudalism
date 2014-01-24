/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valarmorghulismc.feudalism.chunk.types;

import com.valarmorghulismc.feudalism.chunk.types.listeners.DefenseListener;
import org.bukkit.event.Listener;

/**
 *
 * @author ZNickq
 */
public class Defense extends ChunkType{

	@Override
	public String getName() {
		return "Defense";
	}

	@Override
	public int getInitial() {
		return 12;
	}

	@Override
	public int getIncrease() {
		return 1;
	}

    @Override
    public Listener getListener(){
        return new DefenseListener(); //Should only be called onEnable()
    }
}
