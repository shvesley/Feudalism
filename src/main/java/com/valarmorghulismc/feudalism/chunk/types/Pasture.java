/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valarmorghulismc.feudalism.chunk.types;

import com.valarmorghulismc.feudalism.Feudalism;
import com.valarmorghulismc.feudalism.chunk.ChunkLocation;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

/**
 *
 * @author ZNickq
 */
public class Pasture extends ChunkType {

	private EntityType[] possibleToSpawn = new EntityType[4];
	private Random rand = new Random();

	protected Pasture() {
		possibleToSpawn[0] = EntityType.PIG;
		possibleToSpawn[1] = EntityType.COW;
		possibleToSpawn[2] = EntityType.CHICKEN;
		possibleToSpawn[3] = EntityType.SHEEP;
	}

	@Override
	public void onTick(ChunkLocation cl) {
		int ni = rand.nextInt(4);
		EntityType toSpawn = possibleToSpawn[ni];
		Entity[] ents = cl.getChunk().getEntities();
		if(ents.length > Feudalism.getConfiguration().getPastureEntityLimit()) {
			return;
		}
		for (Entity enty : ents) {
			System.out.println(enty.getType());
			if (enty instanceof Animals) {
				Location locy = enty.getLocation();
				for (int i = 1; i <= Feudalism.getConfiguration().getPastureSpawningAnimals(); i++) {
					locy.getWorld().spawnEntity(locy, toSpawn);
				}
				return;
			}
		}
	}

	@Override
	public String getName() {
		return "Pasture";
	}

	@Override
	public int getInitial() {
		return 4;
	}

	@Override
	public int getIncrease() {
		return 2;
	}

	@Override
	public int getTickIntervalInMins() {
		return Feudalism.getConfiguration().getPastureTickInterval();
	}
}
