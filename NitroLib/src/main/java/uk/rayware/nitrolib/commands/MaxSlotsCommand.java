package uk.rayware.nitrolib.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.rayware.nitrolib.NitroLib;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static uk.rayware.nitrolib.util.Color.*;

public class MaxSlotsCommand extends Command {
	
	private Field mpField;
	
	public MaxSlotsCommand() {
		super("maxslots");
	}
	
	private static void editProperties(CommandSender sender) {
		Properties properties = new Properties();
		File propFile = new File("server.properties");
		
		try {
			try (InputStream is = new FileInputStream(propFile)) {
				properties.load(is);
			}
			
			String maxPlayers = Integer.toString(Bukkit.getServer().getMaxPlayers());
			
			if (properties.getProperty("max-players").equals(maxPlayers)) {
				return;
			}
			
			sender.sendMessage(GOLD("Saving max players to server.properties..."));
			properties.setProperty("max-players", maxPlayers);
			
			try (OutputStream os = new FileOutputStream(propFile)) {
				properties.store(os, "Minecraft server properties");
			}
			sender.sendMessage(GREEN("Max players saved to server.properties"));
		} catch (IOException e) {
			NitroLib.getInstance().log("An error occurred while updating the server properties");
			NitroLib.getInstance().log(e.getMessage());
		}
	}
	
	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!sender.hasPermission(NitroLib.permissionMessage)) {
			sender.sendMessage(NitroLib.permissionMessage);
			return true;
		}

		List<String> arguments = new ArrayList<>(Arrays.stream(args).toList());

		boolean temp = arguments.remove("-t");

		if (arguments.size() == 0) {
			sender.sendMessage(GREEN("Current max slots: " + sender.getServer().getMaxPlayers()));
			return true;
		} else if (arguments.size() == 1) {
			try {
				int newMaxSlots = Integer.parseInt(arguments.get(0));
				changeSlots(newMaxSlots, sender);
				if (!temp)
					editProperties(sender);
			} catch (NumberFormatException e) {
				sender.sendMessage(RED("Invalid number: " + arguments.get(0)));
				return true;
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}
			
		} else {
			sender.sendMessage(RED("Usage: /maxslots [new max slots] (-t)"));
			return false;
		}
		return true;
	}
	
	private Field getMaxPlayersField(Object playerList) throws ReflectiveOperationException {
		Class<?> playerListClass = playerList.getClass().getSuperclass();
		try {
			Field field = playerListClass.getDeclaredField("maxPlayers");
			field.setAccessible(true);
			return field;
		} catch (NoSuchFieldException e) {
			for (Field field : playerListClass.getDeclaredFields()) {
				if (field.getType() != int.class) {
					continue;
				}
				field.setAccessible(true);
				if (field.getInt(playerList) == Bukkit.getServer().getMaxPlayers()) {
					return field;
				}
			}
			
			throw new NoSuchFieldException("Unable to find maxPlayers field in " + playerListClass.getName());
		}
	}
	
	private void changeSlots(int slots, CommandSender sender) throws ReflectiveOperationException {
		Method serverGetHandle = Bukkit.getServer().getClass().getDeclaredMethod("getHandle");
		Object playerList = serverGetHandle.invoke(Bukkit.getServer());
		
		if (this.mpField == null) {
			this.mpField = getMaxPlayersField(playerList);
		}
		
		this.mpField.setInt(playerList, slots);
		sender.sendMessage(GREEN("Max slots changed to " + slots));
	}
}
