package uk.rayware.guard.player;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import uk.rayware.guard.Guard;
import uk.rayware.guard.check.CheckHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class PlayerDataHandler implements Listener {

	private final Set<UUID> alerts = new HashSet<>();
	private final HashMap<UUID, PlayerData> dataMap = new HashMap<>();

	public PlayerDataHandler(Guard plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);

		Bukkit.getOnlinePlayers().forEach(player -> dataMap.putIfAbsent(player.getUniqueId(), new PlayerData(player.getUniqueId())));
	}

	@EventHandler
	public void onPreJoin(AsyncPlayerPreLoginEvent event) {
		PlayerData data = new PlayerData(event.getUniqueId());

		dataMap.putIfAbsent(event.getUniqueId(), data);

		CheckHandler.loadChecks(data);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		dataMap.remove(event.getPlayer().getUniqueId());
		alerts.remove(event.getPlayer().getUniqueId());
	}

}
