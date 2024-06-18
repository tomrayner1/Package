package uk.rayware.nitrolib.economy.cmds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.rayware.nitrolib.NitroLib;

import java.util.Arrays;

public class BalCommand extends Command {

	public BalCommand() {
		super("balance");
		setAliases(Arrays.asList("bal", "networth"));
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		boolean spy = sender.hasPermission("nitrolib.bal.other") || !(sender instanceof Player);

		String targetDisplay = "";
		double targetWorth = 0;

		if (args.length > 0 && spy)
		{
			OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);

			targetDisplay = player.getName();
			targetWorth = NitroLib.getInstance().getEconomyHandler().getNitroEconomy().getBalance(player);
		}
		else
		{
			if (!(sender instanceof Player))
			{
				sender.sendMessage((ChatColor.translateAlternateColorCodes('&', "&eUsage: &a/balance&f <player>")));
				return true;
			}

			Player player = (Player) sender;

			targetDisplay = player.getDisplayName();
			targetWorth = NitroLib.getInstance().getEconomyHandler().getNitroEconomy().getBalance(player);
		}

		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6" + targetDisplay + "&6: &2&l$&a" + targetWorth));

		return true;
	}
}
