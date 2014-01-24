package com.valarmorghulismc.feudalism.chunk.types.listeners;

import com.valarmorghulismc.feudalism.chunk.ChunkInfo;
import com.valarmorghulismc.feudalism.chunk.ChunkLocation;
import com.valarmorghulismc.feudalism.chunk.types.ChunkType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * @author bobert456
 */
public class FarmListener implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTryFarm(PlayerInteractEvent event) {
        //System.out.println("Attempting farm!");
        if (!event.hasItem() || event.isCancelled() || event.getClickedBlock() == null || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            //System.out.println("Returning!");
            return;
        }
        if (isForFarming(event.getItem().getType()) && event.getClickedBlock().getType() == Material.SOIL) {
            //System.out.println("Is for farming, soil!");
            ChunkLocation curIn = new ChunkLocation(event.getClickedBlock().getLocation());
            ChunkInfo curInfo = ChunkInfo.getChunkInfo(curIn);
            if (curInfo.getType() == null || curInfo.getType() != ChunkType.FARM) {
                //System.out.println("Going on");
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.GOLD + "You can only plant crops on farms!");
            }
        }
    }

    private boolean isForFarming(Material type) {
        return type == Material.SEEDS || type == Material.MELON_SEEDS || type == Material.PUMPKIN_SEEDS || type == Material.CARROT_ITEM || type == Material.SUGAR_CANE || type == Material.POTATO_ITEM;
    }

    private boolean isCrop(Material type) {
        return type == Material.SUGAR_CANE_BLOCK || type == Material.POTATO || type == Material.CARROT || type == Material.WHEAT || type == Material.MELON_BLOCK || type == Material.PUMPKIN;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCropBreak(BlockBreakEvent event) {
        if (!isCrop(event.getBlock().getType()) || event.isCancelled()) //Check if its a crop
            return;
        ChunkLocation chunkLoc = new ChunkLocation(event.getBlock().getLocation());
        ChunkInfo info = ChunkInfo.getChunkInfo(chunkLoc);
        if (info.getType() == ChunkType.FARM) { //Its a farm, so give bonuses
            //We could probably skip this check because plants can only be planted on farms anyway
            //Keep it in case of natural gen plants such as Sugar Cane
            Location location = event.getPlayer().getLocation();
            ItemStack drop = null;
            for (ItemStack is : event.getBlock().getDrops()) { //Cant find a better way to do it, feel free to change it
                drop = is;
                break;
            }
            if (drop == null) {
                return;
            }
            for (int i = 0; i < new Random().nextInt(10) + 5; i++) { //Drops a random amount from 5 - 15
                location.getWorld().dropItemNaturally(location, drop);
            }


        }
    }
}
