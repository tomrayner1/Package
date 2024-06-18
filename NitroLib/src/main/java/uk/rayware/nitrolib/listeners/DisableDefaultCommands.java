package uk.rayware.nitrolib.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.PermissionAttachment;
import uk.rayware.nitrolib.NitroLib;

public class DisableDefaultCommands implements Listener {

	@EventHandler
	public void join(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		PermissionAttachment attachment = player.addAttachment(NitroLib.getInstance());

		attachment.setPermission("minecraft.command.me", false);
		attachment.setPermission("minecraft.command.msg", false);
		attachment.setPermission("minecraft.command.tell", false);

		player.recalculatePermissions();
	}

}
