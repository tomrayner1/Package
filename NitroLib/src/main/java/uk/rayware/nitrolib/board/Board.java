package uk.rayware.nitrolib.board;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import uk.rayware.nitrolib.board.events.BoardCreateEvent;

@Getter @Setter
public class Board {

    private final JavaPlugin plugin;

    private BoardAdapter adapter;
    private BoardThread thread;
    private BoardListener listeners;
    private BoardStyle boardStyle = BoardStyle.DEFAULT;

    private Map<UUID, BoardScoreboard> boards;

    private long ticks = 2;
    private boolean hook = false, debugMode = true, callEvents = true;

    private final ChatColor[] chatColorCache = ChatColor.values();

    /**
     * Assemble.
     *
     * @param plugin instance.
     * @param adapter that is being provided.
     */
    public Board(JavaPlugin plugin, BoardAdapter adapter) {
        if (plugin == null) {
            throw new RuntimeException("Assemble can not be instantiated without a plugin instance!");
        }

        this.plugin = plugin;
        this.adapter = adapter;
        this.boards = new ConcurrentHashMap<>();

        this.setup();
    }

    /**
     * Setup Assemble.
     */
    public void setup() {
        // Register Events.
        this.listeners = new BoardListener(this);
        this.plugin.getServer().getPluginManager().registerEvents(listeners, this.plugin);

        // Ensure that the thread has stopped running.
        if (this.thread != null) {
            this.thread.stop();
            this.thread = null;
        }

        // Register new boards for existing online players.
        for (Player player : this.getPlugin().getServer().getOnlinePlayers()) {

            // Call Events if enabled.
            if (this.isCallEvents()) {
                BoardCreateEvent createEvent = new BoardCreateEvent(player);

                Bukkit.getPluginManager().callEvent(createEvent);
                if (createEvent.isCancelled()) {
                    continue;
                }
            }

            getBoards().putIfAbsent(player.getUniqueId(), new BoardScoreboard(player, this));
        }

        // Start Thread.
        this.thread = new BoardThread(this);
    }

    /**
     * Cleanup Assemble.
     */
    public void cleanup() {
        // Stop thread.
        if (this.thread != null) {
            this.thread.stop();
            this.thread = null;
        }

        // Unregister listeners.
        if (listeners != null) {
            HandlerList.unregisterAll(listeners);
            listeners = null;
        }

        // Destroy player scoreboards.
        for (UUID uuid : getBoards().keySet()) {
            Player player = Bukkit.getPlayer(uuid);

            if (player == null || !player.isOnline()) {
                continue;
            }

            getBoards().remove(uuid);
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
    }

}
