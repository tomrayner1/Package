package uk.rayware.nitrolib.backup;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import java.util.HashMap;

// setup here
// https://help.displayr.com/hc/en-us/articles/360004116315-How-to-Create-an-Access-Token-for-Dropbox

public class BackupMain {
	@Setter
	@Getter
	private static String ACCESS_TOKEN = "sl.BXCMi0iT5dHcyZVGJrsk8rOCUjgwz7waq1IO-UNq9_8QLXwczcZUKcBmxFb1fk8L7SynEzfCqW1vzdvKtHCYVj74ycnpumJWSdNY5E7J9_N_vbRrSV09b7_57WC7OHUpi9V2CXQ";
	@Setter
	private static String SERVER_NAME;
	private static DbxClientV2 client;
	private static Codes AuthenticationStatus = Codes.NOT_AUTHENTICATED;
	@Getter
	private static BackupMain INSTANCE;
	
	HashMap<Player, Location> originalPlayerLocations = new HashMap<>();
	/*
	* NOTE: This can set the world to the initial lobby, if one is not set, it will set to a custom, superflat world named "world_backup"
	* */
	@Setter
	@Getter
	World backupWorld = null;
	
	
	public static void log(String message) {
		Bukkit.getLogger().info("[NitroLib/BackupMain] " + message);
	}
	
	public BackupMain(String serverName) {
		INSTANCE = this;
		SERVER_NAME = serverName;
		if (ACCESS_TOKEN == null) {
			log("No access token found, please use /Dropboxauth to authenticate.");
			return;
		}
		try {
			this.Authenticate();
		} catch (DbxException e) {
			log("Error authenticating with dropbox, please check your token");
		}
		
		if (this.getBackupWorld() == null) {
			WorldCreator wc = new WorldCreator("world_backup");
			wc.generateStructures(false);
			wc.type(WorldType.FLAT);
			wc.generatorSettings("3;minecraft:air;127;decoration");
			wc.environment(World.Environment.NORMAL);
			wc.generateStructures(false);
			wc.createWorld();
			this.setBackupWorld(wc.createWorld());
		}
	}
	
	public Codes Authenticate() throws DbxException {
		DbxRequestConfig config = DbxRequestConfig.newBuilder("NitroLibBackups").build();
		client = new DbxClientV2(config, ACCESS_TOKEN);
		try {
			client.users().getCurrentAccount();
			AuthenticationStatus = Codes.AUTHENTICATED;
		} catch (DbxApiException ae) {
			AuthenticationStatus = Codes.ERROR;
		} catch (DbxException e) {
			AuthenticationStatus = Codes.NOT_AUTHENTICATED;
		}
		
		return AuthenticationStatus;
	}
	
	public boolean isAuthenticated() {
		return AuthenticationStatus == Codes.AUTHENTICATED;
	}
	
	public Codes makeFolder(String folderName) throws DbxException {
		if (this.isAuthenticated()) {
			client.files().createFolderV2("/" + folderName);
			return Codes.SUCCESS;
		} else {
			return Codes.NOT_AUTHENTICATED;
		}
	}
	
	/*
	* Backs up the worlds by unloading them, moving all players to a lobby world, zipping the folders and moving players back after the backup is complete.
	*/
	public static void MP_Backup(String[] worlds) {
		String zipfilename = Compressor.Compress();
		// Backup("zipfilename");
		// let's move all players to the backup world and save their previous locations to the hashmap
		for (Player p : Bukkit.getOnlinePlayers()) {
			INSTANCE.originalPlayerLocations.put(p, p.getLocation());
			p.teleport(INSTANCE.getBackupWorld().getSpawnLocation());
		}
		
		// unload all worlds specified in argument which is a string array
		
		for (String world : worlds) {
			World w = Bukkit.getWorld(world);
			if (w != null) {
				Bukkit.unloadWorld(w, false);
			}
		}
		
		// for now we will just zip them.
		Compressor.Compress();
		
		for (String world: worlds) {
			World w = Bukkit.getWorld(world);
			if (w != null) {
				// Looks wrong, but it's not. This is how you reload a world. If it doesn't exist, it will create it and if it does, it will reload it.
				Bukkit.createWorld(new WorldCreator(world));
			}
		}
		
		// return players to their original locations
		for (Player p: INSTANCE.originalPlayerLocations.keySet()) {
			p.teleport(INSTANCE.originalPlayerLocations.get(p));
		}
		
	}
	
}
