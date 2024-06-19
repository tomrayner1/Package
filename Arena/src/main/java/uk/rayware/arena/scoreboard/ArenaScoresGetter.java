package uk.rayware.arena.scoreboard;

import org.bukkit.entity.Player;
import uk.rayware.arena.cooldown.impl.CombatTag;
import uk.rayware.arena.stats.ArenaStats;

import java.util.ArrayList;
import java.util.List;

public class ArenaScoresGetter implements ScoreboardScoresGetter {

	@Override
	public List<String> getScores(Player player) {
		List<String> scores = new ArrayList<>();

		scores.add("&e&lKills:&f " + ArenaStats.getKills(player.getUniqueId()));
		scores.add("&e&lDeaths:&f " + ArenaStats.getDeaths(player.getUniqueId()));
		scores.add("&a&lBalance:&2 $&a500");

		if (CombatTag.get(player.getUniqueId()).active()) {
			scores.add("&4&lCombat:&f " + (CombatTag.get(player.getUniqueId()).remaining() / 100) / 10D + "s");
		}

		return scores;
	}

}
