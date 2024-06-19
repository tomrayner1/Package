package uk.rayware.arena.command;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Optional;
import uk.rayware.arena.Arena;
import uk.rayware.arena.ArenaLocale;
import uk.rayware.arena.lobby.LobbyItems;
import uk.rayware.arena.player.PlayerState;
import uk.rayware.nitrolib.NitroLib;

public class SpectateCommand {

	@Command({"spectate", "spec"})
	public void spec(Player sender, @Optional Player target) {
		PlayerState state = Arena.getInstance().getPlayerStateHandler().get(sender);
		PlayerState targetState = null;

		if (target != null) {
			targetState = Arena.getInstance().getPlayerStateHandler().get(target);
		}

		switch (state) {
			case IN_LOBBY -> {
				Arena.getInstance().getSpectateHandler().startSpectating(sender);
			}
			case SPECTATING -> {
				if (target == null) {
					Arena.getInstance().getSpectateHandler().stopSpectating(sender);
				} else {
					if (targetState == null) {
						sender.sendMessage(ArenaLocale.ERROR);
					} else if (targetState == PlayerState.IN_ARENA) {
						sender.teleport(target.getLocation());
					} else if (targetState == PlayerState.FIGHTING) {
						sender.sendMessage(ArenaLocale.ERROR);
					}
				}
			}
			default -> {
				sender.sendMessage(ArenaLocale.NEED_TO_BE_IN_LOBBY);
			}
		}
	}

}
