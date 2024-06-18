package uk.rayware.nitrolib.minigamefwk;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import uk.rayware.nitrolib.util.Color;

import java.util.HashMap;

/*
 * This is the class to extend when making a minigame
 * You will need to make listeners for
 * - PlayerJoinEvent
 * - PlayerQuitEvent
 * - PlayerDeathEvent
 *
 */
public abstract class MinigameFramework {
	@Getter
	private final World map;
	@Getter
	private final World lobby;
	@Getter
	private final String minigameName;
	@Getter
	private EMinigames.MinigameStatus minigameStatus;
	@Getter
	private boolean playersCanRespawn;
	@Getter
	private boolean playersCanJoin;
	private HashMap<Player, String> players = new HashMap<>();
	
	
	public MinigameFramework(String minigameName) {
		this.minigameName = minigameName;
		WorldCreator WC = new WorldCreator(minigameName + "-map");
		WC.environment(World.Environment.NORMAL);
		WC.type(WorldType.NORMAL);
		
		this.map = WC.createWorld();
		
		WorldCreator WC2 = new WorldCreator(minigameName + "-lobby");
		WC2.environment(World.Environment.NORMAL);
		WC2.type(WorldType.FLAT);
		
		this.lobby = WC2.createWorld();
		
		this.minigameStatus = EMinigames.MinigameStatus.LOBBY;
	}
	
	public MinigameFramework(String minigameName, World world, World lobby) {
		this.minigameName = minigameName;
		this.map = world;
		this.lobby = lobby;
	}
	
	public void sendPlayerToLobby(Player player) {
		for (Player p : players.keySet()) {
			if (players.get(p).equals(minigameName + "-lobby")) {
				p.teleport(lobby.getSpawnLocation());
			}
		}
	}
	
	public void sendPlayerToMap(Player player) {
		for (Player p : players.keySet()) {
			if (players.get(p).equals(minigameName + "-map")) {
				p.teleport(map.getSpawnLocation());
			}
		}
	}
	
	private void addPlayer(Player player) {
		players.put(player, minigameName + "-lobby");
	}
	
	private void removePlayer(Player player) {
		players.remove(player);
	}
	
	public void playerJoin(Player player, EMinigames.World to) {
		if (!playersCanJoin) {
			player.sendMessage(Color.RED("You cannot join this minigame at this time"));
			return;
		}
		addPlayer(player);
		if (to.equals(EMinigames.World.LOBBY)) {
			sendPlayerToLobby(player);
		} else if (to.equals(EMinigames.World.MAP)) {
			sendPlayerToMap(player);
		}
	}
	
	public void playerLeave(Player player, Location returnTo) {
		removePlayer(player);
		player.teleport(returnTo);
	}
	
	public void playerDeath(Player player) {
		if (!playersCanRespawn) {
		
		}
	}
	
	public void setWorldSpawn(Integer x, Integer y, Integer z, String worldName) {
		if (worldName.equals(minigameName + "-map")) {
			map.setSpawnLocation(x, y, z);
		} else if (worldName.equals(minigameName + "-lobby")) {
			lobby.setSpawnLocation(x, y, z);
		}
	}
	
	public void sendPlayerToSpawn(Player player, String worldName) {
	
	}
	
	public void sendPlayerToCoordinate(Player player, Integer x, Integer y, Integer z) {
		player.teleport(this.map.getSpawnLocation().add(x, y, z));
	}
	
}
