package uk.rayware.arena.scoreboard;

import org.bukkit.entity.Player;

import java.util.List;

public interface ScoreboardScoresGetter {

	List<String> getScores(Player player);

}
