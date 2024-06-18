package uk.rayware.hub.command;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.rayware.hub.HubPlugin;
import uk.rayware.hub.npc.ServerNPC;
import uk.rayware.nitrogen.NitrogenAPI;
import uk.rayware.nitrolib.NitroLib;

import java.util.HashMap;

public class AddNpcCommand extends Command {

	public AddNpcCommand() {
		super("addnpc");
		setPermission("hub.command.addnpc");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!sender.hasPermission("hub.command.addnpc")) {
			sender.sendMessage(NitroLib.permissionMessage);
			return true;
		}

		if (!(sender instanceof Player player)) {
			return true;
		}

		if (!(args.length == 1 || args.length == 2)) {
			player.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.GREEN + "/addnpc " + ChatColor.WHITE + "<server> [skin]");
			return true;
		}

		HashMap<String, String> data = NitrogenAPI.getNitrogenServerData(args[0]);

		if (data.isEmpty() || data.get("display") == null) {
			player.sendMessage(ChatColor.RED + "Could not find specified server.");
			return true;
		}

		if (HubPlugin.getInstance().getOwningNPCs().containsKey(data.get("display"))) {
			player.sendMessage(ChatColor.RED + "NPC already exists for this server.");
			return true;
		}

		String skinName = player.getName();

		if (args.length == 2) {
			skinName = args[1];
		}

		Location location = player.getLocation();

		ServerNPC npc = new ServerNPC(data.get("name"), data.get("display"), location, skinName);
		HubPlugin.getInstance().getOwningNPCs().put(data.get("display"), npc.getNpc().getId());

		player.sendMessage(ChatColor.GREEN + "Created " + ChatColor.DARK_GREEN + data.get("display") + ChatColor.GREEN + " NPC (at " + location.toVector() + ", skin: " + skinName + ").");
		return true;
	}
}
