package uk.rayware.arena;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;
import uk.rayware.arena.command.KitCommand;
import uk.rayware.arena.command.SpectateCommand;
import uk.rayware.arena.command.UpdateSpawnCommand;
import uk.rayware.arena.debug.AmIInTheSetCommand;
import uk.rayware.arena.kit.KitHandler;
import uk.rayware.arena.lobby.LobbyHandler;
import uk.rayware.arena.player.PlayerStateHandler;
import uk.rayware.arena.scoreboard.ScoreboardProvider;
import uk.rayware.arena.spectate.SpectateHandler;
import uk.rayware.arena.stats.ArenaStatsHandler;
import uk.rayware.nitrolib.board.Board;

@Getter
public class Arena extends JavaPlugin {

	@Getter
	private static Arena instance;

	private boolean DEBUG;

	private Board board;
	private BukkitCommandHandler bukkitCommandHandler;
	private LobbyHandler lobbyHandler;
	private PlayerStateHandler playerStateHandler;
	private ArenaStatsHandler arenaStatsHandler;
	private KitHandler kitHandler;
	private SpectateHandler spectateHandler;

	@Override
	public void onEnable() {
		instance = this;

		for (World world : Bukkit.getWorlds()) {
			world.setGameRuleValue("doDaylightCycle", "false");
			world.setGameRuleValue("doMobSpawning", "false");
			world.setGameRuleValue("doweathercycle", "false");
			world.setTime(6_000L);
		}

		saveDefaultConfig();
		DEBUG = getConfig().getBoolean("settings.debug");

		bukkitCommandHandler = BukkitCommandHandler.create(this);
		bukkitCommandHandler.register(
				new UpdateSpawnCommand(),
				new AmIInTheSetCommand(),
				new KitCommand(),
				new SpectateCommand());

		board = new Board(this, new ScoreboardProvider());
		lobbyHandler = new LobbyHandler();
		playerStateHandler = new PlayerStateHandler();
		arenaStatsHandler = new ArenaStatsHandler();
		kitHandler = new KitHandler();
		spectateHandler = new SpectateHandler();
	}

	@Override
	public void onDisable() {
		//bukkitCommandHandler.unregisterAllCommands();
		arenaStatsHandler.saveConfig();
		Bukkit.getScheduler().cancelTasks(this);
	}

}
