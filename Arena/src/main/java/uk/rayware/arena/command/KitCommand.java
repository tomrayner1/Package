package uk.rayware.arena.command;

import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Optional;
import uk.rayware.arena.Arena;
import uk.rayware.arena.ArenaLocale;
import uk.rayware.arena.kit.Kit;
import uk.rayware.arena.kit.menu.KitsMenu;
import uk.rayware.arena.player.PlayerState;
import uk.rayware.nitrolib.NitroLib;

public class KitCommand {

	@Command("kits")
	public void kits(Player sender) {
		PlayerState state = Arena.getInstance().getPlayerStateHandler().get(sender);

		if (state == PlayerState.FIGHTING || state == PlayerState.SPECTATING) {
			sender.sendMessage(ArenaLocale.NEED_TO_BE_IN_LOBBY);
			return;
		}

		NitroLib.getInstance().getMenuHandler().openMenuForPlayer(sender, new KitsMenu());
	}

	@Command("kit")
	public void kit(Player sender, @Optional String name) {
		PlayerState state = Arena.getInstance().getPlayerStateHandler().get(sender);

		if (state == PlayerState.FIGHTING || state == PlayerState.SPECTATING) {
			sender.sendMessage(ArenaLocale.NEED_TO_BE_IN_LOBBY);
			return;
		}

		Kit kit = Arena.getInstance().getKitHandler().getByName(name);

		if (kit == null) {
			sender.sendMessage(String.format(ArenaLocale.CANT_FIND_KIT, name));
		} else {
			kit.apply(sender);
			sender.sendMessage(String.format(ArenaLocale.APPLIED_KIT, kit.getName()));
		}
	}

}
