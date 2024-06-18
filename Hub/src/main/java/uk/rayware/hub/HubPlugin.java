package uk.rayware.hub;

import lombok.Getter;
import lombok.Setter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import uk.rayware.hub.scoreboard.ScoreboardProvider;
import uk.rayware.hub.command.AddNpcCommand;
import uk.rayware.hub.command.RemoveNpcCommand;
import uk.rayware.hub.listeners.HubListeners;
import uk.rayware.hub.listeners.JoinMessage;
import uk.rayware.hub.tasks.GlobalPlayerCountTask;
import uk.rayware.hub.util.NPCUtil;
import uk.rayware.nitrolib.NitroLib;
import uk.rayware.nitrolib.board.Board;

import java.time.Instant;
import java.util.*;

@Getter
public class HubPlugin extends JavaPlugin {

	@Getter
	private static HubPlugin instance;

	@Setter
	private int globalCount = 0;

	private final List<String> welcomeMessage = new ArrayList<>();
	private final HashMap<String, Integer> owningNPCs = new HashMap<>();
	private String boardDisplay;
	private int boardCount = 0;
	private boolean usingCitizens;
	private boolean promptRightClick;

	@Override
	public void onEnable() {
		instance = this;

		usingCitizens = getServer().getPluginManager().isPluginEnabled("Citizens");

		saveDefaultConfig();

		promptRightClick = getConfig().getBoolean("prompt-right-click");
		List<String> motd = getConfig().getStringList("join-message");

		motd.forEach(line -> {
			line = line.replaceAll("%bar%", NitroLib.bar);
			line = line.replaceAll("%slimbar%", NitroLib.slimbar);

			welcomeMessage.add(line);
		});

		Arrays.asList(
				new JoinMessage(),
				new HubListeners()
		).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

		new Board(this, new ScoreboardProvider());

		getServer().getScheduler().runTaskTimerAsynchronously(this, new GlobalPlayerCountTask(), 0, 20 * 3);
		getServer().getScheduler().runTaskTimer(this, () -> {
			if (++boardCount >= getConfig().getStringList("board-footers").size()) {
				boardCount = 0;
			}

			String entry = getConfig().getStringList("board-footers").get(boardCount).replace("%time%", NitroLib.timeFormatter.format(Instant.now()));

			boardDisplay = ChatColor.translateAlternateColorCodes('&', entry);
		}, 0, 20 * 10);

		if (usingCitizens)
		{
			NitroLib.getInstance().getNitroCommandMap().register("hub", new RemoveNpcCommand());
			NitroLib.getInstance().getNitroCommandMap().register("hub", new AddNpcCommand());

			Bukkit.getScheduler().runTaskLater(this, this::postLoad, 20 * 2);

			getServer().getScheduler().runTaskTimerAsynchronously(this, () -> owningNPCs.forEach((name, id) ->
					NPCUtil.updateHologram(CitizensAPI.getNPCRegistry().getById(id))), 20 * 5, 20 * 3);
		}
	}

	@Override
	public void onDisable() {
		getServer().getScheduler().cancelTasks(this);
	}

	public void postLoad() {
		List<Integer> update = new ArrayList<>();

		getConfig().getIntegerList("owning-npcs").forEach(entry -> {
			NPC npc = CitizensAPI.getNPCRegistry().getById(entry);

			if (npc != null) {
				owningNPCs.put(npc.getName(), entry);
				update.add(entry);
				getLogger().info("Loaded Hub NPC: " + npc.getName() + ".");
			}
		});

		if (update != getConfig().getIntegerList("owning-npcs")) {
			getConfig().set("owning-npcs", update);
		}
	}

	public void saveNPCs() {
		getConfig().set("owning-npcs", new ArrayList<>(owningNPCs.values()));
		saveConfig();
	}

}
