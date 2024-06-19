package uk.rayware.arena.player;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import uk.rayware.arena.Arena;
import uk.rayware.arena.debug.DebugPlayerStateListener;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class PlayerStateHandler implements Listener {

	private final HashMap<UUID, PlayerState> playerState = new HashMap<>();
	private final PlayerStateStatsCache stateStatsCache = new PlayerStateStatsCache();

	public PlayerStateHandler() {
		Bukkit.getServer().getPluginManager().registerEvents(this, Arena.getInstance());
		if (Arena.getInstance().isDEBUG()) {
			Bukkit.getServer().getPluginManager().registerEvents(new DebugPlayerStateListener(), Arena.getInstance());
		}
	}

	public PlayerState get(Player player) {
		return playerState.get(player.getUniqueId());
	}

	public PlayerState get(UUID uuid) {
		return playerState.get(uuid);
	}

	public void set(Player player, PlayerState state) {
		UUID uuid = player.getUniqueId();
		PlayerStateChangeEvent event;

		if (playerState.containsKey(uuid)) {
			event = new PlayerStateChangeEvent(player, playerState.get(uuid), state);
			playerState.replace(uuid, state);
		} else {
			event = new PlayerStateChangeEvent(player, null, state);
			playerState.put(uuid, state);
		}

		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	@EventHandler
	public void join(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		CuboidSelection spawn = Arena.getInstance().getLobbyHandler().getSpawn();

		if (spawn == null) {
			set(player, PlayerState.IN_LOBBY);
			return;
		}

		if (spawn.contains(player.getLocation())) {
			set(player, PlayerState.IN_LOBBY);
		} else {
			set(player, PlayerState.IN_ARENA);
		}
	}

	@EventHandler
	public void quit(PlayerQuitEvent event) {
		playerState.remove(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void move(PlayerMoveEvent event) {
		CuboidSelection spawn = Arena.getInstance().getLobbyHandler().getSpawn();

		if (spawn == null || event.isCancelled()) {
			return;
		}

		PlayerState state = Arena.getInstance().getPlayerStateHandler().get(event.getPlayer());

		if (state == PlayerState.SPECTATING) {
			return;
		}

		boolean nowIn = spawn.contains(event.getTo());
		boolean wasIn = spawn.contains(event.getFrom());

		if (!nowIn && wasIn) {
			set(event.getPlayer(), PlayerState.IN_ARENA);
		} else if (nowIn && !wasIn) {
			set(event.getPlayer(), PlayerState.IN_LOBBY);
		}
	}

}
