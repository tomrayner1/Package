package uk.rayware.arena.stats;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import uk.rayware.arena.Arena;
import uk.rayware.arena.player.PlayerState;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ArenaStatsHandler implements Listener {

	private File file;
	@Getter
	private FileConfiguration config;

	public ArenaStatsHandler() {
		Bukkit.getServer().getPluginManager().registerEvents(this, Arena.getInstance());
		setupFile();
		setupRunnable();
	}

	@EventHandler
	public void death(PlayerDeathEvent event) {
		UUID uuid = event.getEntity().getUniqueId();
		if (Arena.getInstance().getPlayerStateHandler().get(uuid) != PlayerState.IN_ARENA) {
			return;
		}
		ArenaStats.addDeath(uuid);
		if (event.getEntity().getKiller() != null) {
			ArenaStats.addKill(event.getEntity().getKiller().getUniqueId());
		}
	}

	@EventHandler
	public void join(PlayerJoinEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		boolean save = false;

		if (config.isSet(uuid + ".kills")) {
			ArenaStats.updateKills(uuid, config.getInt(uuid + ".kills"));
		} else {
			ArenaStats.updateKills(uuid, 0);
			config.set(uuid + ".kills", 0);
			save = true;
		}

		if (config.isSet(uuid + ".deaths")) {
			ArenaStats.updateDeaths(uuid, config.getInt(uuid + ".deaths"));
		} else {
			ArenaStats.updateDeaths(uuid, 0);
			config.set(uuid + ".deaths", 0);
			save = true;
		}

		if (save) {
			saveConfig();
		}
	}

	public void setupFile() {
		file = new File(Arena.getInstance().getDataFolder(), "stats.yml");
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			Arena.getInstance().saveResource("stats.yml", false);
		}
		config = new YamlConfiguration();
		try {
			config.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void setupRunnable() {
		Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(Arena.getInstance(), this::saveConfig, 1, 300);
	}

	public void saveConfig() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
