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
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (p.hasPermission(getConfig().getString("Elite.Permission"))) e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Elite.JoinMessage")).replace("<player>", p.getName()));
		if (p.hasPermission(getConfig().getString("Mythical.Permission"))) e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Mythical.JoinMessage")).replace("<player>", p.getName()));
		if (p.hasPermission(getConfig().getString("Ninja.Permission"))) e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Ninja.JoinMessage")).replace("<player>", p.getName()));
		if (p.hasPermission(getConfig().getString("Moderator.Permission"))) e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Moderator.JoinMessage")).replace("<player>", p.getName()));
		if (p.hasPermission(getConfig().getString("Admin.Permission"))) e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Admin.JoinMessage")).replace("<player>", p.getName()));
		if (p.hasPermission(getConfig().getString("YouTuber.Permission"))) e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Youtuber.JoinMessage")).replace("<player>", p.getName()));
		if (p.hasPermission(getConfig().getString("Builder.Permission"))) e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Builder.JoinMessage")).replace("<player>", p.getName()));
		if (p.hasPermission(getConfig().getString("Developer.Permission"))) e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Developer.JoinMessage")).replace("<player>", p.getName()));
		if (p.hasPermission(getConfig().getString("Owner.Permission"))) {
			e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Owner.JoinMessage")).replace("<player>", p.getName()));
			for (Player all : Bukkit.getOnlinePlayers()) {
				if (getConfig().getBoolean("Owner.Playsound")) all.playSound(all.getLocation(), Sound.ENDERDRAGON_DEATH, 2, 2);
				if (getConfig().getBoolean("Owner.PlayTitle")) sendTitle(all, 125, 185, 125, getConfig().getString("Owner.Title"), getConfig().getString("Owner.Subtitle"));
			}
		} else {
			if (getConfig().getBoolean("Default.UseThis")) {
				e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Default.JoinMessage")).replace("<player>", p.getName()));
			} else {
				e.setJoinMessage(null);
			}
		}
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (p.hasPermission(getConfig().getString("Elite.Permission"))) e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Elite.QuitMessage")).replace("<player>", p.getName()));
		else if (p.hasPermission(getConfig().getString("Mythical.Permission"))) e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Mythical.QuitMessage")).replace("<player>", p.getName()));
		else if (p.hasPermission(getConfig().getString("Ninja.Permission"))) e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Ninja.QuitMessage")).replace("<player>", p.getName()));
		else if (p.hasPermission(getConfig().getString("Moderator.Permission"))) e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Moderator.QuitMessage")).replace("<player>", p.getName()));
		else if (p.hasPermission(getConfig().getString("Admin.Permission"))) e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Admin.QuitMessage")).replace("<player>", p.getName()));
		else if (p.hasPermission(getConfig().getString("YouTuber.Permission"))) e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Youtuber.QuitMessage")).replace("<player>", p.getName()));
		if (p.hasPermission(getConfig().getString("Builder.Permission"))) e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Builder.QuitMessage")).replace("<player>", p.getName()));
		if (p.hasPermission(getConfig().getString("Developer.Permission"))) e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Developer.QuitMessage")).replace("<player>", p.getName()));
		if (p.hasPermission(getConfig().getString("Owner.Permission"))) {
			e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Owner.QuitMessage")).replace("<player>", p.getName()));
		} else {
			if (getConfig().getBoolean("Default.UseThis")) {
				e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Default.JoinMessage")).replace("<player>", p.getName()));
			} else {
				e.setQuitMessage(null);
			}
		}
	}
	
	private static void sendPacket(Player p, Object packet) {
		try {
			Object handle = p.getClass().getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
		    Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
		    playerConnection.getClass().getMethod("sendPacket", new Class[] { getNMSClass("Packet") }).invoke(playerConnection, new Object[] { packet });  
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	private static Class<?> getNMSClass(String n) {
		String version = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		try {
			return Class.forName("net.minecraft.server." + version + "." + n);
		} catch (ClassNotFoundException e) {
			e.printStackTrace(); 	
		}
		return null;
	}
	
	private static void sendTitle(Player p, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
		try {
			if (title != null) {
				title = ChatColor.translateAlternateColorCodes('&', title);
				title = title.replaceAll("<player>", p.getName());
				Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
				Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\":\"" + title + "\"}" });
	            Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(new Class[] { getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE });
	            Object titlePacket = titleConstructor.newInstance(new Object[] { enumTitle, chatTitle, fadeIn, stay, fadeOut });
	            sendPacket(p, titlePacket);
			}     
			if (subtitle != null) {
	            subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
	            subtitle = subtitle.replaceAll("<player>", p.getName());
	            Object enumSubtitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
	            Object chatSubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\":\"" + subtitle + "\"}" });
	            Constructor<?> subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(new Class[] { getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE });
	            Object subtitlePacket = subtitleConstructor.newInstance(new Object[] { enumSubtitle, chatSubtitle, fadeIn, stay, fadeOut });
	            sendPacket(p, subtitlePacket); 
			}
		} catch (Exception e) {
			e.printStackTrace();      
		}
		
	}
}
