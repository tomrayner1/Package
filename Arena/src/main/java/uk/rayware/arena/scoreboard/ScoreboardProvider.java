package uk.rayware.arena.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Team;
import uk.rayware.arena.Arena;
import uk.rayware.arena.player.PlayerState;
import uk.rayware.nitrolib.NitroLib;
import uk.rayware.nitrolib.board.BoardAdapter;
import uk.rayware.nitrolib.board.events.BoardCreatedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreboardProvider implements BoardAdapter, Listener {

	private final Map<PlayerState, ScoreboardScoresGetter> scoreGetters = new HashMap<>();

	public ScoreboardProvider() {
		Bukkit.getServer().getPluginManager().registerEvents(this, Arena.getInstance());

		scoreGetters.put(PlayerState.IN_ARENA, new ArenaScoresGetter());
		scoreGetters.put(PlayerState.FIGHTING, new FightingScoresGetter());
		scoreGetters.put(PlayerState.IN_LOBBY, new LobbyScoresGetter());
		scoreGetters.put(PlayerState.SPECTATING, new SpectatingScoresGetter());
	}

	@Override
	public String getTitle(Player player) {
		return "&6&lMethMC &7" + NitroLib.slimbar + "&f Arena";
	}

	@Override
	public List<String> getScores(Player player) {
		List<String> scores = new ArrayList<>();
		scores.add("&7&m--------------------");

		scores.addAll(scoreGetters.get(Arena.getInstance().getPlayerStateHandler().get(player)).getScores(player));

		if (scores.size() == 1) {
			return null;
		}

		scores.add("&7&m--------------------");
		return scores;
	}

	@EventHandler
	public void create(BoardCreatedEvent event) {
		Team ally = event.getBoard().getScoreboard().getTeam("ally");

		if (ally == null) {
			ally = event.getBoard().getScoreboard().registerNewTeam("ally");
		}

		ally.setPrefix(ChatColor.GREEN + "");

		Team enemy = event.getBoard().getScoreboard().getTeam("enemy");

		if (enemy == null) {
			enemy = event.getBoard().getScoreboard().registerNewTeam("enemy");
		}

		enemy.setPrefix(ChatColor.RED + "");

		Team spectator = event.getBoard().getScoreboard().getTeam("spectator");

		if (spectator == null) {
			spectator = event.getBoard().getScoreboard().registerNewTeam("spectator");
		}

		spectator.setPrefix(ChatColor.GRAY + "");
	}

	@EventHandler
	public void join(PlayerJoinEvent event) {
		Bukkit.getScheduler().runTaskLater(Arena.getInstance(), () -> {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.getScoreboard().getTeam("enemy").addEntry(event.getPlayer().getName());
				event.getPlayer().getScoreboard().getTeam("enemy").addEntry(player.getName());
			}

			event.getPlayer().getScoreboard().getTeam("enemy").removeEntry(event.getPlayer().getName());
			event.getPlayer().getScoreboard().getTeam("ally").addEntry(event.getPlayer().getName());
		}, 20);
	}
}
