/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valarmorghulismc.feudalism.chunk.types;

import com.valarmorghulismc.feudalism.chunk.ChunkLocation;
import com.valarmorghulismc.feudalism.chunk.types.listeners.FarmListener;
import org.bukkit.event.Listener;

/**
 *
 * @author ZNickq
 */
public class Farm extends ChunkType{

    public enum Season{
        FALLOW,
        SPRING,
        FALL;

    }

	@Override
	public String getName() {
		return "Farm";
	}

	@Override
	public int getInitial() {
		return 5;
	}

	@Override
	public int getIncrease() {
		return 2;
	}

    @Override
    public void onTick(ChunkLocation chunkLoc){


    }

    @Override
    public Listener getListener(){
        return new FarmListener();
    }
}
