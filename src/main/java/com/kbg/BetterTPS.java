package com.kbg;

import java.io.File;
import java.util.Arrays;
import java.util.Random;

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
    public static final String[] tps_CONFIG = {"t", "p", "s"};
    
    @Override
    public void onEnable() {
        _PLUGIN_INSTANCE = this;
        _PLUGIN_INSTANCE.getServer().getPluginManager().registerEvents((Listener) _PLUGIN_INSTANCE, _PLUGIN_INSTANCE);
        _PLUGIN_INSTANCE._CREATE_Config();
        _PLUGIN_INSTANCE.TPS = _PLUGIN_INSTANCE.getConfig().getInt(_PLUGIN_INSTANCE.GetConfigTPS());
        if (_PLUGIN_INSTANCE.TPS > 420) {
            _PLUGIN_INSTANCE.getLogger().info("Highest possible TPS is 420!");
            _PLUGIN_INSTANCE.TPS = 420;
        }
        new BukkitRunnable() {          
            public void run() {
                Bukkit.getOnlinePlayers().stream().forEach(_PLAYER -> {
                    int tps = _PLUGIN_INSTANCE.getConfig().getInt(_PLUGIN_INSTANCE.GetConfigTPS());
                    float SPEED = (float) (0.2 * (_PLUGIN_INSTANCE.TPS> 100 ? 5 : _PLUGIN_INSTANCE.TPS/20)); //super secret algorithm
                    _PLAYER.setWalkSpeed(SPEED);
                    _PLAYER.setFlySpeed(SPEED);
                    Random Random = new Random();
                    _PLAYER.spigot().setCollidesWithEntities(Random.nextInt(2) == 0); 
                });
            }
        }.runTaskTimer(_PLUGIN_INSTANCE, 0L, (25/5));
    }
    
    
    @EventHandler //what does @ even mean not like i'm emailing
    public void onPlayerCommand(PlayerCommandPreprocessEvent _EVENT_) {
        if (_EVENT_.getMessage().equalsIgnoreCase("/tps")) {
            _EVENT_.getPlayer().sendMessage(String.format("%sTPS from last 1m, 5m, 15m: %s*%s.0, *%s.0, *%s.0", ChatColor.GOLD, ChatColor.GREEN, _PLUGIN_INSTANCE.TPS, _PLUGIN_INSTANCE.TPS, _PLUGIN_INSTANCE.TPS));
            _EVENT_.setCancelled(1 == 1);
        }
    }
    
    public static String GetConfigTPS() {
        StringBuilder string_Builder = new StringBuilder();
        Arrays.asList(tps_CONFIG).stream().forEach(strinG -> string_Builder.append(strinG));
        return string_Builder.toString();
    }
    
    public int _CREATE_Config() {
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
