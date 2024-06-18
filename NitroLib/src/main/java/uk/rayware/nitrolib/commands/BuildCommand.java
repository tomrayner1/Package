package uk.rayware.nitrolib.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import uk.rayware.nitrolib.NitroLib;

public class BuildCommand extends Command {

	private final NitroLib plugin;

	public BuildCommand(NitroLib plugin) {
		super("build");
		this.plugin = plugin;

		setPermission("nitrolib.command.build");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!(sender instanceof Player player)) {
			return true;
		}

		if (!player.hasPermission("nitrolib.command.build")) {
			player.sendMessage(NitroLib.permissionMessage);
			return true;
		}

		if (player.hasMetadata("build")) {
			player.removeMetadata("build", plugin);
		} else {
			player.setMetadata("build", new FixedMetadataValue(plugin, true));
		}

		sender.sendMessage(ChatColor.GOLD + "Build: " +
				(player.hasMetadata("build") ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
		return true;
	}
}
