package com.valarmorghulismc.feudalism.player;

import com.valarmorghulismc.feudalism.Feudalism;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author ZNickq
 */
public class FeudalPlayerListener implements Listener {

	private Feudalism plugin;

	public FeudalPlayerListener(Feudalism plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event) {
		final FeudalPlayer fplr = FeudalPlayer.getPlayer(event.getPlayer().getName(), true);
		if (fplr != null) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					fplr.setDisplayName();
				}
			}, 20L);
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		FeudalPlayer fplr = FeudalPlayer.getPlayer(event.getPlayer().getName(), false);
		if (fplr != null) {
			fplr.savePlayer(new File(plugin.getDataFolder(), "players"), true);
		}
	}
}
