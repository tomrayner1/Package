package uk.rayware.hub.command;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.rayware.hub.HubPlugin;
import uk.rayware.nitrolib.NitroLib;

import java.util.Arrays;

public class RemoveNpcCommand extends Command {

	public RemoveNpcCommand() {
		super("removenpc");
		setPermission("hub.command.removenpc");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!sender.hasPermission("hub.command.removenpc")) {
			sender.sendMessage(NitroLib.permissionMessage);
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.GREEN + "/removenpc " + ChatColor.WHITE + "<serverDisplayName>");
			return true;
		}

		String target = Arrays.toString(args).replace('[', ' ').replace(']', ' ').replaceAll("[,]", "").trim();

		if (HubPlugin.getInstance().getOwningNPCs().containsKey(target)) {
			NPC npc = CitizensAPI.getNPCRegistry().getById(HubPlugin.getInstance().getOwningNPCs().get(target));

			npc.despawn();
			npc.destroy();

			HubPlugin.getInstance().getOwningNPCs().remove(target);
			HubPlugin.getInstance().saveNPCs();

			sender.sendMessage(ChatColor.GREEN + "Successfully removed " + ChatColor.DARK_GREEN + target + ChatColor.GREEN + ".");
		} else {
			sender.sendMessage(ChatColor.RED + "Could not find Hub NPC for '" + ChatColor.YELLOW + target + ChatColor.RED + "'.");
		}
		return true;
	}
}
