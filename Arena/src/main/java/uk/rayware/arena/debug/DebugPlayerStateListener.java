package uk.rayware.arena.debug;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import uk.rayware.arena.Arena;
import uk.rayware.arena.lobby.LobbyItems;
import uk.rayware.arena.player.PlayerStateChangeEvent;

public class DebugPlayerStateListener implements Listener {

	@EventHandler
	public void state(PlayerStateChangeEvent event) {
		if (event.getFrom() == null) {
			event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&eDebug&8]&f From none to " + event.getTo().name()));
			return;
		}
		event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&eDebug&8]&f From " + event.getFrom().name() + " to " + event.getTo().name()));
	}

}
