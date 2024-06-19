package uk.rayware.arena.player;

import lombok.Getter;
import org.bukkit.Bukkit;
import uk.rayware.arena.Arena;
import uk.rayware.arena.ArenaLocale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class PlayerStateStatsCache {

	private HashMap<PlayerState, Integer> totalPerState = new HashMap<>();
	private int online = 0;

	public PlayerStateStatsCache() {
		Bukkit.getScheduler().runTaskTimer(Arena.getInstance(), () -> {
			int lobby = 0;
			int arena = 0;
			int fights = 0;

			for (PlayerState state : Arena.getInstance().getPlayerStateHandler().getPlayerState().values()) {
				switch (state) {
					case IN_LOBBY -> lobby++;
					case IN_ARENA -> arena++;
					case FIGHTING -> fights++;
				}
			}

			HashMap<PlayerState, Integer> results = new HashMap<>();

			results.put(PlayerState.IN_LOBBY, lobby);
			results.put(PlayerState.IN_ARENA, arena);
			results.put(PlayerState.FIGHTING, fights);

			totalPerState = results;
			online = Bukkit.getOnlinePlayers().size();
		}, 0, 2);
	}

	public List<String> getScoreboardStats() {
		List<String> scores = new ArrayList<>();

		scores.add(String.format(ArenaLocale.SCOREBOARD_ONLINE, online));
		scores.add(String.format(ArenaLocale.SCOREBOARD_IN_ARENA, totalPerState.get(PlayerState.IN_ARENA)));
		scores.add(String.format(ArenaLocale.SCOREBOARD_IN_FIGHTS, totalPerState.get(PlayerState.FIGHTING)));

		return scores;
	}

}
