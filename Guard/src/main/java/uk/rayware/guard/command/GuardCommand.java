package uk.rayware.guard.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import uk.rayware.guard.Guard;

@Command({"guard", "gbo"})
@CommandPermission("guard.command.help")
public class GuardCommand {

	@Subcommand("help")
	public void help(Player player) {
		player.sendMessage("&f impl later");
	}

	@Subcommand("alerts")
	@CommandPermission("guard.command.alerts")
	public void alerts(Player player) {
		if (!Guard.getGuard().getDataHandler().getAlerts().remove(player.getUniqueId())) {
			Guard.getGuard().getDataHandler().getAlerts().add(player.getUniqueId());
			player.sendMessage(ChatColor.GREEN + "You are now subscribed to guard alerts.");
			return;
		}
		player.sendMessage(ChatColor.RED + "You are no longer subscribed to guard alerts.");
	}

}
