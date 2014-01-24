/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valarmorghulismc.feudalism.chunk.types;

import com.valarmorghulismc.feudalism.chunk.ChunkLocation;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author ZNickq
 */
public abstract class ChunkType {
	
	public static final ChunkType PASTURE = new Pasture();
	public static final ChunkType FARM = new Farm();
	public static final ChunkType MERCHANT = new Merchant();
	public static final ChunkType THRONE_ROOM = new ThroneRoom();
	public static final ChunkType DEFENSE = new Defense();
	private static Set<ChunkType> all = new HashSet<ChunkType>();
	
	public static ChunkType getByName(String name) {
		for(ChunkType ct : all) {
			if(ct.getName().equalsIgnoreCase(name)) {
				return ct;
			}
		}
		return null;
	}
	
	static {
		goGoStatic();
	}

	private static void goGoStatic() {
		all.add(PASTURE);
		all.add(FARM);
		all.add(MERCHANT);
		all.add(THRONE_ROOM);
	}
	
	public static Set<ChunkType> getAll() {
		return Collections.unmodifiableSet(all);
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash+=getName().hashCode();
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ChunkType)) {
			return false;
		}
		return getName().equals(((ChunkType)obj).getName());
	}
	
	public void onTick(ChunkLocation cl) {
		System.out.println("Chunk was ticked!");
	}
	
	public abstract String getName();
	
	public abstract int getInitial();
	
	public abstract int getIncrease();

    public abstract Listener getListener();
	
	public int getTickIntervalInMins() {
		return -1;
	}
}
