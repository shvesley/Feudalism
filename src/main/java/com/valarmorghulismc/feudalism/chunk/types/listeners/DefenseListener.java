package com.valarmorghulismc.feudalism.chunk.types.listeners;

import com.valarmorghulismc.feudalism.util.CountMap;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * @author bobert
 */
public class DefenseListener implements Listener {

    CountMap<Location> blockCounter = new CountMap<Location>();
    @EventHandler
    public void onBreak(BlockBreakEvent e){
    //TODO count the amount of times a block has been broken?
        //blockCounter.increcment(e.getLocation());
        //If greater than 10 break it
    }
}
