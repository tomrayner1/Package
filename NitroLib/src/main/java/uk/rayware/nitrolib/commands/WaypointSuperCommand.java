package uk.rayware.nitrolib.commands;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import uk.rayware.nitrolib.lunarclient.LunarClientAPI;
import uk.rayware.nitrolib.lunarclient.object.LCWaypoint;
import uk.rayware.nitrolib.lunarclient.waypoint.EWaypointMGR;

import java.util.HashMap;

import static uk.rayware.nitrolib.util.Color.*;

/**
 * A command that allows waypoint manipulation.
 * Let's see
 * <p>
 * so
 * <p>
 * first subcommands are:
 * create {name} {color} {forced} {visible}
 * remove {name}
 * list
 * replace {name} {x} {y} {z} {world} {color} by standard the forced=false and visible=true
 * set {name}
 * > forced {false/true}
 * > visible {true/false}
 */
public class WaypointSuperCommand extends Command {
	private final LunarClientAPI main;
	
	public WaypointSuperCommand(LunarClientAPI main) {
		super("waypoint");
		this.main = main;
	}
	
	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!sender.hasPermission("nitrolib.command.waypoint")) {
			sender.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this command.");
			return false;
		}
		
		if (args.length == 0) {
			sender.sendMessage("Usage: /waypoint <create|remove|list|replace|set>");
			sender.sendMessage("Usage: /waypoint create <name> <color [0xFFF]> <forced [true/false]> <visible [true/false]>");
			sender.sendMessage("Usage: /waypoint remove <name>");
			sender.sendMessage("Usage: /waypoint list");
			sender.sendMessage("Usage: /waypoint set <name> <(false)forced|visible(true)> <true|false>");
			
			return false;
		}
		
		switch (args[0].toLowerCase()) {
			case "create" -> {
				createSubcommand(args, sender);
			}
			case "remove" -> {
				removeSubcommand(args, sender);
			}
			case "list" -> {
				listSubcommand(args, sender);
			}
			case "set" -> {
				setSubcommand(args, sender);
			}
			case "broadcast" -> {
				broadcastSubcommand(args, sender);
			}
			default -> {
				sender.sendMessage(ChatColor.RED + "Error, " + args[0] + " is not a valid subcommand [create|remove|list|replace|set]");
			}
		}
		
		return false;
		
	}
	
	public void createSubcommand(String[] args, CommandSender sender) {
		String[] arguments = new String[args.length - 1];
		System.arraycopy(args, 1, arguments, 0, args.length - 1);
		if (arguments.length < 4) {
			sender.sendMessage(ChatColor.RED + "(100) Error, not enough arguments.");
			sender.sendMessage(ChatColor.RED + "Usage: /waypoint create <name> <color [0xFFF]> <forced [true/false][1/0]> <visible [true/false][1/0]>");
			return;
		}
		
		String name = arguments[0];
		int color;
		try {
			color = Integer.decode(arguments[1]);
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "Error, color must be a valid hex color. e.g 0xFFF. You supplied " + arguments[1]);
			sender.sendMessage(ChatColor.DARK_RED + e.getMessage());
			return;
		}
		boolean forced = Boolean.parseBoolean(arguments[2]);
		boolean visible = Boolean.parseBoolean(arguments[3]);
		
		if (sender instanceof Player player) {
			Location location = player.getLocation();
			main.getWaypointMGR().addWaypoint(name, color, location);
			sender.sendMessage(ChatColor.GREEN + "Waypoint " + name + " created. At " + location.toString());
		} else if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage(ChatColor.RED + "Error, you must be a player to use this command.");
		}
	}
	
	public void removeSubcommand(String[] args, CommandSender sender) {
		String[] arguments = new String[args.length - 1];
		// Should exclude the first argument as that is the subcommand name
		System.arraycopy(args, 1, arguments, 0, args.length - 1);
		// only if the waypoint was actually removed.
		// includes broadcasting the removal packet within WaypointMGR;
		EWaypointMGR eval = main.getWaypointMGR().removeWaypoint(arguments[0]);
		if (eval == EWaypointMGR.REMOVED) {
			sender.sendMessage(ChatColor.GREEN + "Removed waypoint " + arguments[0]);
		} else if (eval == EWaypointMGR.NOT_FOUND) {
			sender.sendMessage(ChatColor.DARK_RED + "Error, waypoint does not exist");
		}
	}
	
	public void listSubcommand(String[] args, CommandSender sender) {
		sender.sendMessage(ChatColor.GREEN + "Listing waypoints!");
		HashMap<String, LCWaypoint> waypoints = main.getWaypointMGR().getWaypoints();
		if (waypoints == null) {
			sender.sendMessage(ChatColor.DARK_RED + "Waypoints array is null, waypoints will not work, please update your plugin!");
			return;
		}
		for (String key : waypoints.keySet()) {
			LCWaypoint wp = waypoints.get(key);
			sender.sendMessage(wp.toColoredString());
		}
	}
	
	public void setSubcommand(String[] args, CommandSender sender) {
		String[] arguments = new String[args.length - 1];
		// Should exclude the first argument as that is the subcommand name
		System.arraycopy(args, 1, arguments, 0, args.length - 1);
		if (arguments.length < 3) {
			sender.sendMessage(ChatColor.RED + "(100) Error, not enough arguments.");
			sender.sendMessage(ChatColor.RED + "Usage: /waypoint set <(false)forced|visible(true)> <true|false> <name>");
			return;
		}
		LCWaypoint wp = main.getWaypointMGR().getWaypoint(arguments[2]);
		if (wp == null) {
			sender.sendMessage(DARK_RED("Error, waypoint does not exist"));
			return;
		}
		switch (arguments[0].toLowerCase()) {
			case "forced" -> {
				wp.setForced(Boolean.parseBoolean(arguments[1]));
				sender.sendMessage(ChatColor.GREEN + "Set waypoint " + wp.getName() + " forced to " + wp.isForced());
			}
			case "visible" -> {
				wp.setVisible(Boolean.parseBoolean(arguments[1]));
				sender.sendMessage(GREEN("Set waypoint " + wp.getName() + " visible to " + wp.isVisible()));
			}
			default -> {
				sender.sendMessage(RED("Error, " + arguments[0] + " is not a valid subcommand [forced|visible]"));
			}
		}
	}
	
	public void broadcastSubcommand(String[] args, CommandSender sender) {
		String[] arguments = new String[args.length - 1];
		// Should exclude the first argument as that is the subcommand name
		System.arraycopy(args, 1, arguments, 0, args.length - 1);
		if (arguments.length < 1) {
			sender.sendMessage(RED("Please specify a world name."));
			return;
		}
		Object exists = main.getWaypointMGR().getWaypoint(arguments[0]);
		if (exists == null) {
			sender.sendMessage(RED("Waypoint does not exist."));
		} else main.getWaypointMGR().broadcastWaypoint(arguments[0]);
	}
}