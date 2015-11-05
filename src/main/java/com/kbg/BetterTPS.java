package com.kbg;

import java.io.File;
import java.util.Arrays;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BetterTPS extends JavaPlugin implements Listener {
    
    public static volatile Integer TPS;
    public static volatile BetterTPS _PLUGIN_INSTANCE;
    public static final String[] tpsCONFIG = {"t", "p", "s"};
    
    @Override
    public void onEnable() {
        _PLUGIN_INSTANCE = this;
        _PLUGIN_INSTANCE.getServer().getPluginManager().registerEvents((Listener) _PLUGIN_INSTANCE, _PLUGIN_INSTANCE);
        _PLUGIN_INSTANCE.createConfig();
        _PLUGIN_INSTANCE.TPS = _PLUGIN_INSTANCE.getConfig().getInt(_PLUGIN_INSTANCE.getConfigTps());
        if (_PLUGIN_INSTANCE.TPS > 420) {
            _PLUGIN_INSTANCE.getLogger().info("Highest possible TPS is 420!");
            _PLUGIN_INSTANCE.TPS = 420;
        }
        new BukkitRunnable() {          
            public void run() {
                Bukkit.getOnlinePlayers().stream().forEach(player -> {
                    int tps = _PLUGIN_INSTANCE.getConfig().getInt(_PLUGIN_INSTANCE.getConfigTps());
                    float SPEED = (float) (0.2 * (_PLUGIN_INSTANCE.TPS> 100 ? 5 : _PLUGIN_INSTANCE.TPS/20));
                    player.setWalkSpeed(SPEED);
                    player.setFlySpeed(SPEED);
                });
            }
        }.runTaskTimer(_PLUGIN_INSTANCE, 0L, (25/5));
    }
    
    
    @EventHandler //what does @ even mean not like i'm emailing
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().equalsIgnoreCase("/tps")) {
            event.getPlayer().sendMessage(String.format("%sTPS from last 1m, 5m, 15m: %s*%s.0, *%s.0, *%s.0", ChatColor.GOLD, ChatColor.GREEN, _PLUGIN_INSTANCE.TPS, _PLUGIN_INSTANCE.TPS, _PLUGIN_INSTANCE.TPS));
            event.setCancelled(1 == 1);
        }
    }
    
    public static String getConfigTps() {
        StringBuilder string = new StringBuilder();
        Arrays.asList(tpsCONFIG).stream().forEach(str -> string.append(str));
        return string.toString();
    }
    
    public int createConfig() {
        try {
            if (!_PLUGIN_INSTANCE.getDataFolder().exists()) {
                _PLUGIN_INSTANCE.getDataFolder().mkdirs();
            }
            File file = new File(_PLUGIN_INSTANCE.getDataFolder(), "config.yml");
            
            if (!file.exists()) {
                _PLUGIN_INSTANCE.getLogger().info("No config.yml found, creating default");
                _PLUGIN_INSTANCE.saveDefaultConfig();
            } else {
                _PLUGIN_INSTANCE.getLogger().info("config.yml found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

}
