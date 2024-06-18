package uk.rayware.nitrolib.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.rayware.nitrolib.NitroLib;

public class SetSpawnCommand extends Command {

	private final NitroLib nitroLib;

	public SetSpawnCommand(NitroLib nitroLib) {
		super("setspawn");
		this.nitroLib = nitroLib;

		setPermission("nitrolib.command.setspawn");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!(sender instanceof Player player)) {
			return true;
		}

		if (!player.hasPermission("nitrolib.command.setspawn")) {
			player.sendMessage(NitroLib.permissionMessage);
			return true;
		}

		nitroLib.setSpawn(player.getLocation());
		sender.sendMessage(ChatColor.GREEN + "You have updated the spawn point.");

		return true;
	}
}
