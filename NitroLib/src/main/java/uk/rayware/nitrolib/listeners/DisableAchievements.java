package uk.rayware.nitrolib.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;

public class DisableAchievements implements Listener {

	@EventHandler
	public void achievement(PlayerAchievementAwardedEvent event) {
		event.setCancelled(true);
	}

}
