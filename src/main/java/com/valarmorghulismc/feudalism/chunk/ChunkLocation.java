package com.valarmorghulismc.feudalism.chunk;

import com.valarmorghulismc.feudalism.house.House;
import com.valarmorghulismc.feudalism.house.House.HouseRank;
import com.valarmorghulismc.feudalism.player.FeudalPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

/**
 *
 * @author ZNickq
 */
public class ChunkLocation {
	private int x,z;
	private String world;
	
	public ChunkLocation(String world, int x, int z) {
		this.world = world;
		this.x = x;
		this.z = z;
	}
	
	public ChunkLocation(Location loc) {
		x = (int) (loc.getX() / 10);
		z = (int) (loc.getZ() / 10);
		world = loc.getWorld().getName();
	}

	public House getHouse() {
		return House.getHouse(this);
	}
	
	public boolean isOwner(FeudalPlayer fplr) {
		ChunkInfo cinfo = ChunkInfo.getChunkInfo(this);
		if(fplr.getHouse() != null) {
			if(fplr.getHouse().ownsChunk(this) && fplr.getRank().hasPower(HouseRank.CONSTABLE)) {
				return true;
			}
		}
		if(cinfo.getOwner() != null) {
			return cinfo.getOwner().equals(fplr.getName());
		}
		return false;
	}
	
	public int getX() {
		return x;
	}
	
	public int getZ() {
		return z;
	}
	
	public String getWorld() {
		return world;
	}

	@Override
	public boolean equals(Object obj) {
		ChunkLocation other = (ChunkLocation) obj;
		return other.x == x && other.z == z && other.world.equals(world);
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 13 * hash + this.x;
		hash = 13 * hash + this.z;
		hash = 13 * hash + this.world.hashCode();
		return hash;
	}

	public Chunk getChunk() {
		return Bukkit.getWorld(world).getBlockAt(x*10, 64, z*10).getChunk();
	}
	
	
	
}
