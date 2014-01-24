/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valarmorghulismc.feudalism.chunk.types;

import com.valarmorghulismc.feudalism.chunk.types.listeners.MerchantListener;
import org.bukkit.event.Listener;

/**
 *
 * @author ZNickq
 */
public class Merchant extends ChunkType{

	@Override
	public String getName() {
		return "Merchant";
	}

	@Override
	public int getInitial() {
		return 10;
	}

	@Override
	public int getIncrease() {
		return 0;
	}

    @Override
    public Listener getListener(){
        return new MerchantListener(); //Should only be called onEnable()
    }
}
