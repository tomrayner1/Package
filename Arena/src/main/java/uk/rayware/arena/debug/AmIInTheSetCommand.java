package uk.rayware.arena.debug;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import uk.rayware.arena.Arena;
import uk.rayware.arena.ArenaLocale;
import uk.rayware.arena.lobby.LobbyItems;

public class AmIInTheSetCommand {

	@Command("amiintheset")
	public void check(Player sender) {
		if (Arena.getInstance().isDEBUG()) {
			sender.sendMessage("Lobby Items: " + (LobbyItems.hasLobbyItems.contains(sender.getUniqueId()) ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"));
			sender.sendMessage("Spectating: " + (Arena.getInstance().getSpectateHandler().getSpectators().contains(sender.getUniqueId()) ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"));
		} else {
			sender.sendMessage(ArenaLocale.CANT_DO_THIS);
		}
	}

}
