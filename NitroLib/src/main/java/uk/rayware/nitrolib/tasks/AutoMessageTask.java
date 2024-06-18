package uk.rayware.nitrolib.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import uk.rayware.nitrolib.NitroLib;

public class AutoMessageTask implements Runnable {

	private int current = 0;

	@Override
	public void run() {
		if (NitroLib.getInstance().getTips().size() <= current) {
			current = 0;
		}

		Bukkit.getOnlinePlayers().forEach(player -> {
			if (!player.hasMetadata("hidetips")) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6TIP&7]&r " + NitroLib.getInstance().getTips().get(current)));
			}
		});
		current++;
	}

}
