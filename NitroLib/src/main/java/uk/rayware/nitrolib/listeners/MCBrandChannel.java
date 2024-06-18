package uk.rayware.nitrolib.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import uk.rayware.nitrolib.NitroLib;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

public class MCBrandChannel implements PluginMessageListener {
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		try {
			NitroLib.getInstance().log(player.getName() + " is using " + new String(message, "UTF-8").substring(1));
		} catch (UnsupportedEncodingException uee) {
			NitroLib.getInstance().log("Error decoding message from " + player.getName() + "!");
		}
	}
	
	@EventHandler
	public static void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
		addChannel(event.getPlayer(), "MC|Brand");
	}
	
	private static void addChannel(Player p, String channel) {
		try{
			p.getClass().getMethod("addChannel", String.class).invoke(p, channel);
			
		} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
