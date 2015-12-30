package me.boomboompower;

import java.lang.reflect.Constructor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Hub extends JavaPlugin implements Listener {
	
	@Override
	public void onEnable() {
        saveDefaultConfig();
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerJoinEvent e) {
        // Define the player.
		Player p = e.getPlayer();
        // Loop through all the join messages.
        for (String key: getConfig().getConfigurationSection("join-messages").getKeys(false)) {
            // If the player has the particular permission for the message...
            if (p.hasPermission("hubjoinmessages.join." + key)) {
                // ... set the join message to the configured one.
                e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("join-messages." + key))); 
                // Return to leave the loop.
                return;
            }
            // Otherwise, set the join message to the default.
            e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("join-messages.default"))); 
        }
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent e) {
        // Define the player.
		Player p = e.getPlayer();
        // Loop through all the leave messages.
		for (String key: getConfig().getConfigurationSection("leave-messages").getKeys(false)) {
            // If the player has the particular permission for the message...
            if (p.hasPermission("hubjoinmessages.leave." + key)) {
                // ... set the leave message to the configured one.
                e.setLeaveMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("leave-messages." + key))); 
                // Return to leave the loop.
                return;
            }
            // Otherwise, set the leave message to the default.
            e.setLeaveMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("leave-messages.default"))); 
        }
	}
    
}