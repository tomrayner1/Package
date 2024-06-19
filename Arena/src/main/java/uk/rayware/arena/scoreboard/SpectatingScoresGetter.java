package uk.rayware.arena.scoreboard;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpectatingScoresGetter implements ScoreboardScoresGetter {

	@Override
	public List<String> getScores(Player player) {
		List<String> scores = new ArrayList<>();

		scores.add("You are spectating");

		return scores;
	}

}
