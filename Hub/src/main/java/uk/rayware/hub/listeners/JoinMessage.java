package uk.rayware.hub.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import uk.rayware.hub.HubPlugin;
import uk.rayware.nitrogen.NitrogenAPI;

public class JoinMessage implements Listener {

	@EventHandler
	public void join(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		//player.sendMessage(ChatColor.GREEN + "You are connected to " + ChatColor.BOLD + NitrogenAPI.getNitrogenServer().getDisplayName());

		HubPlugin.getInstance().getWelcomeMessage().forEach(line -> {
			line = line.replaceAll("%displayName%", player.getDisplayName());
			line = line.replaceAll("%name%", player.getName());
			line = line.replaceAll("%bullet%", "•");

			player.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
		});

		if (HubPlugin.getInstance().isPromptRightClick())
		{
			HubPlugin.getInstance().getServer().getScheduler().runTaskLater(HubPlugin.getInstance(), () -> {
				player.sendMessage(ChatColor.GREEN + " ");
				player.sendMessage(ChatColor.GREEN + "Right click the " + ChatColor.GOLD + ChatColor.BOLD + "Game Selector"
						+ ChatColor.GREEN + " to get started.");
				player.sendMessage(ChatColor.GREEN + " ");;
			}, 20);
		}
	}

}
