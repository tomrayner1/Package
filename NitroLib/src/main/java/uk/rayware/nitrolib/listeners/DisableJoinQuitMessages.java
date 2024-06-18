package uk.rayware.nitrolib.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DisableJoinQuitMessages implements Listener {

	@EventHandler
	public void join(PlayerJoinEvent event) {
		event.setJoinMessage(null);
	}

	@EventHandler
	public void quit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
	}

}
