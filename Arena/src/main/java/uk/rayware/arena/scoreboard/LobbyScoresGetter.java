package uk.rayware.arena.scoreboard;

import org.bukkit.entity.Player;
import uk.rayware.arena.Arena;

import java.util.ArrayList;
import java.util.List;

public class LobbyScoresGetter implements ScoreboardScoresGetter {

	@Override
	public List<String> getScores(Player player) {
		List<String> scores = new ArrayList<>(Arena.getInstance().getPlayerStateHandler().getStateStatsCache().getScoreboardStats());

		scores.add("&a&lBalance:&2 $&a500");

		return scores;
	}

}
