package uk.rayware.arena.stats;

import uk.rayware.arena.Arena;

import java.util.HashMap;
import java.util.UUID;

public class ArenaStats {

	private static final HashMap<UUID, Integer> kills = new HashMap<>();
	private static final HashMap<UUID, Integer> deaths = new HashMap<>();

	public static int getKills(UUID uuid) {
		if (kills.containsKey(uuid)) {
			return kills.get(uuid);
		}
		return 0;
	}

	public static int getDeaths(UUID uuid) {
		if (deaths.containsKey(uuid)) {
			return deaths.get(uuid);
		}
		return 0;
	}

	public static void updateKills(UUID uuid, int newKills) {
		if (kills.containsKey(uuid)) {
			kills.replace(uuid, newKills);
		} else {
			kills.put(uuid, newKills);
		}
		Arena.getInstance().getArenaStatsHandler().getConfig().set(uuid + ".kills", newKills);
	}

	public static void updateDeaths(UUID uuid, int newDeaths) {
		if (deaths.containsKey(uuid)) {
			deaths.replace(uuid, newDeaths);
		} else {
			deaths.put(uuid, newDeaths);
		}
		Arena.getInstance().getArenaStatsHandler().getConfig().set(uuid + ".deaths", newDeaths);
	}

	public static void addKill(UUID uuid) {
		int killz = getKills(uuid);
		updateKills(uuid, ++killz);
	}

	public static void addDeath(UUID uuid) {
		int deathz = getDeaths(uuid);
		updateDeaths(uuid, ++deathz);
	}

}
