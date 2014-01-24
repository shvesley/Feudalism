package com.valarmorghulismc.feudalism;

import com.valarmorghulismc.feudalism.admin.AdminCommands;
import com.valarmorghulismc.feudalism.chunk.ChunkCommands;
import com.valarmorghulismc.feudalism.chunk.ChunkInfo;
import com.valarmorghulismc.feudalism.chunk.RentTaxRunnable;
import com.valarmorghulismc.feudalism.chunk.types.ChunkTickRunnable;
import com.valarmorghulismc.feudalism.chunk.types.ChunkType;
import com.valarmorghulismc.feudalism.chunk.types.ChunkTypeListener;
import com.valarmorghulismc.feudalism.house.ClaimedLandTicker;
import com.valarmorghulismc.feudalism.house.FeudalHouseListener;
import com.valarmorghulismc.feudalism.house.House;
import com.valarmorghulismc.feudalism.house.HouseCommands;
import com.valarmorghulismc.feudalism.player.FeudalPlayer;
import com.valarmorghulismc.feudalism.player.FeudalPlayerListener;
import com.valarmorghulismc.feudalism.religion.Religion;
import com.valarmorghulismc.feudalism.religion.ReligionCommands;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ZNickq
 */
public class Feudalism extends JavaPlugin {

    public static final int CURRENT_VERSION = 2;
    private FeudalHouseListener hlistener = new FeudalHouseListener();
    private FeudalPlayerListener plistener = new FeudalPlayerListener(this);
    private ChunkTypeListener tlistener = new ChunkTypeListener(this);
    private static Economy economy;
    private static Permission permission;
    private static Feudalism instance;
    private static Configuration mconfig;
    private File pdf;

    @Override
    public void onDisable() {
        try {
            ChunkInfo.save(getDataFolder());
            FeudalPlayer.save(getDataFolder());
            House.save(getDataFolder());
            Religion.save(getConfig());
        } catch (Exception ex) {
            Logger.getLogger(Feudalism.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        pdf = new File(getDataFolder(), "players");
        try {
            ChunkInfo.load(getDataFolder());
            FeudalPlayer.init();
            House.load(getDataFolder());
            Religion.load(getConfig());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Feudalism.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Feudalism.class.getName()).log(Level.SEVERE, null, ex);
        }

        mconfig = new Configuration(this);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(hlistener, this);
        pm.registerEvents(plistener, this);
        pm.registerEvents(tlistener, this);
        for (ChunkType type : ChunkType.getAll()) {
            if (!(type.getListener() instanceof ChunkTypeListener))
                pm.registerEvents(type.getListener(), this); //Maybe pass events through ChunkListener and filter unneeded calls
        }
        getCommand("h").setExecutor(new HouseCommands());
        getCommand("c").setExecutor(new ChunkCommands());
        getCommand("rel").setExecutor(new ReligionCommands());
        getCommand("fa").setExecutor(new AdminCommands());

        setupEconomy();
        setupPermissions();
        setupTasks();
    }

    private void setupTasks() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new RentTaxRunnable(), 20L * 60 * mconfig.getTaxPeriod(), 20L * 60 * mconfig.getTaxPeriod());
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new ChunkTickRunnable(), 20L * 60, 20L * 60);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new ClaimedLandTicker(), 20L, 20L);
    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    public static Economy getEconomy() {
        return economy;
    }

    public static Permission getPermission() {
        return permission;
    }

    public static Configuration getConfiguration() {
        return mconfig;
    }

    public static Feudalism getInstance() {
        return instance;
    }

    public File getPlayerDataFolder() {
        return pdf;
    }
}
