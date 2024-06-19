package uk.rayware.guard.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import uk.rayware.guard.Guard;
import uk.rayware.guard.event.CheckFailEvent;
import uk.rayware.guard.util.TeleportMessage;

import java.util.UUID;

public class PlayerListener implements Listener {

	@EventHandler
	public void fail(CheckFailEvent event) {
		if (!Guard.getGuard().getDataHandler().getAlerts().isEmpty()) {
			String alert = ChatColor.RED + "[GBO] " + ChatColor.LIGHT_PURPLE + event.getPlayerData().getPlayer().getName() +
					ChatColor.YELLOW + " failed " + ChatColor.LIGHT_PURPLE + event.getCheck().getName() +
					ChatColor.YELLOW + ". ";

			if (event.getData() != null) {
				alert += "(" + event.getData() + ") ";
			}

			for (UUID uuid : Guard.getGuard().getDataHandler().getAlerts()) {
				TeleportMessage.sendTPMessage(Bukkit.getPlayer(uuid), event.getPlayerData().getPlayer(), alert);
			}
		}
	}

}
