/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valarmorghulismc.feudalism.chunk.types;

import com.valarmorghulismc.feudalism.chunk.ChunkInfo;
import com.valarmorghulismc.feudalism.chunk.ChunkLocation;
import com.valarmorghulismc.feudalism.house.House;

/**
 *
 * @author ZNickq
 */
public class ChunkTickRunnable implements Runnable {
	private int timeFarmNotTicked = 0;
	
	public ChunkTickRunnable() {
		/*for(ChunkType cs : ChunkType.getAll()) {
			int tick = cs.getTickIntervalInMins();
			if(tick != -1) {
				shouldTick.put(cs, cs.getTickIntervalInMins());
			}
		}*/
		timeFarmNotTicked = ChunkType.PASTURE.getTickIntervalInMins();
	}

	public void run() {
		/* - old system, only need farms right now!
		Set<ChunkType> cs = new HashSet<ChunkType>();
		for(ChunkType ctt : shouldTick.keySet()) {
			int howMuch = shouldTick.get(ctt);
			howMuch--;
			//System.out.println(ctt.getName()+": "+howMuch);
			if(howMuch == 0) {
				cs.add(ctt);
				shouldTick.put(ctt, ctt.getTickIntervalInMins());
			} else {
				shouldTick.put(ctt, howMuch);
			}
		}*/
		//System.out.println("Ticking that shit! "+cs.size());
		timeFarmNotTicked--;
		if(timeFarmNotTicked != 0) {
			return;
		}
		timeFarmNotTicked = ChunkType.PASTURE.getTickIntervalInMins();
		for (String nname : House.getAllHouses().keySet()) {
			House house = House.getAllHouses().get(nname);
			for (ChunkLocation cll : house.getAllOwnedChunks()) {
				ChunkType ttype = ChunkInfo.getChunkInfo(cll).getType();
				if(ttype == null) {
					continue;
				}
				if (ttype == ChunkType.PASTURE) {
					ttype.onTick(cll);
				}
			}
		}
	}
}
