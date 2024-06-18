package uk.rayware.nitrolib;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import uk.rayware.nitrolib.commands.*;
import uk.rayware.nitrolib.economy.EconomyHandler;
import uk.rayware.nitrolib.listeners.DisableAchievements;
import uk.rayware.nitrolib.listeners.DisableDefaultCommands;
import uk.rayware.nitrolib.listeners.DisableJoinQuitMessages;
import uk.rayware.nitrolib.listeners.MCBrandChannel;
import uk.rayware.nitrolib.lunarclient.LunarClientAPI;
import uk.rayware.nitrolib.menu.MenuHandler;
import uk.rayware.nitrolib.tasks.AutoMessageTask;

import java.lang.reflect.Field;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class NitroLib extends JavaPlugin {

	@Getter
	private static NitroLib instance;
	private CommandMap nitroCommandMap;
	private MenuHandler menuHandler;
	private Location spawn;
	private LunarClientAPI lunarClientAPI;
	//private BackupMain backupMain; - doesnt work ATM
	private AutoMessageTask messageTask;
	@Setter
	private List<String> tips;
	private EconomyHandler economyHandler;
	
	public static final String bar = ChatColor.BOLD + "❘" + ChatColor.RESET;
	public static final String slimbar = "❘" + ChatColor.RESET;
	public static final String permissionMessage = ChatColor.RED + "You do not have permission to perform this action.";

	public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm zzz EEE, dd LLL")
			.withZone(ZoneId.systemDefault());
	public static final DateTimeFormatter longDateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss zzz EEE dd MMM yyyy")
			.withZone(ZoneId.systemDefault());

	@Override
	public void onEnable() {
		instance = this;
		lunarClientAPI = new LunarClientAPI();
		//backupMain = new BackupMain(Bukkit.getServerName());
		
		log("Getting command map (potential reflection error)");
		try {
			Field cmdMapField = getServer().getClass().getDeclaredField("commandMap");
			cmdMapField.setAccessible(true);

			nitroCommandMap = (CommandMap) cmdMapField.get(getServer());
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}

		menuHandler = new MenuHandler();

		log("Handling config");
		saveDefaultConfig();

		if (getConfig().contains("spawn.world")) {
			 spawn = new Location(Bukkit.getWorld(getConfig().getString("spawn.world")),
					 getConfig().getDouble("spawn.x"),
					 getConfig().getDouble("spawn.y"),
					 getConfig().getDouble("spawn.z"));
		}

		log("Starting aids tip messages.");
		setTips(getConfig().getStringList("tips"));

		if (tips.size() > 0)
		{
			messageTask = new AutoMessageTask();
			getServer().getScheduler().runTaskTimer(this, messageTask, 10, getConfig().getInt("settings.tipTimer") * 20L);
		}


		if (getConfig().getBoolean("settings.economy"))
		{
			log("Starting Economy");
			economyHandler = new EconomyHandler( this );
		}

		log("Implementing listeners");
		log("  Disabling join & quit messages");
		getServer().getPluginManager().registerEvents(new DisableJoinQuitMessages(), this);
		log("  Removing /minecraft:tell & /minecraft:me permissions");
		getServer().getPluginManager().registerEvents(new DisableDefaultCommands(), this);

		if (getConfig().getBoolean("settings.disableAchievements")) {
			log("  Disabling achievements");
			getServer().getPluginManager().registerEvents(new DisableAchievements(), this);
		}

		log("Implementing commands");
		log("  Build toggle command (nitrolib.command.build)");
		getNitroCommandMap().register("nitrolib", new BuildCommand(this));
		log("  Set spawn command (nitrolib.command.setspawn)");
		getNitroCommandMap().register("nitrolib", new SetSpawnCommand(this));
		log("  Waypoint command (nitrolib.command.waypoint)");
		getNitroCommandMap().register("nitrolib", new WaypointSuperCommand(lunarClientAPI));
		/*
		* Currently broken, might not ever be fixed, comes down to "Access is denied" errors, almost completely unpreventable
		* log("  Backup command (nitrolib.command.backup)");
		* getNitroCommandMap().register("nitrolib", new BackupSuperCommand(backupMain));
		*/
		log("  Max slots command (nitrolib.command.maxslots)");
		getNitroCommandMap().register("nitrolib", new MaxSlotsCommand());

		// DOESN'T WORK AS OF 1.13
		//if (Bukkit.getVersion().contains("1.8"))
		{
			log("  Registering brand channel listener ");
			Messenger messenger = Bukkit.getMessenger();
			messenger.registerIncomingPluginChannel(this, "mc:brand", new MCBrandChannel()); // MC|Brand
		}
		
		log("Finished");
	}

	public void setSpawn(Location location) {
		spawn = location;

		getConfig().set("spawn.x", location.getBlockX() + 0.5);
		getConfig().set("spawn.y", location.getY());
		getConfig().set("spawn.z", location.getBlockZ() + 0.5);
		getConfig().set("spawn.world", location.getWorld().getName());

		saveConfig();
	}

	public void log(String message) {
		Bukkit.getLogger().info("[NitroLib] " + message);
	}

	@Override
	public void onDisable() {
		if (economyHandler.isEnabled())
		{
			economyHandler.save();
		}
	}

}
