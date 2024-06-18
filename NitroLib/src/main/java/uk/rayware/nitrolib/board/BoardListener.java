package uk.rayware.nitrolib.board;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import uk.rayware.nitrolib.board.events.BoardCreateEvent;
import uk.rayware.nitrolib.board.events.BoardDestroyEvent;

@Getter
public class BoardListener implements Listener {

    private final Board board;

    /**
     * Assemble Listener.
     *
     * @param board instance.
     */
    public BoardListener(Board board) {
        this.board = board;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Call Events if enabled.
        if (board.isCallEvents()) {
            BoardCreateEvent createEvent = new BoardCreateEvent(event.getPlayer());

            Bukkit.getPluginManager().callEvent(createEvent);
            if (createEvent.isCancelled()) {
                return;
            }
        }

        getBoard().getBoards().put(event.getPlayer().getUniqueId(), new BoardScoreboard(event.getPlayer(), getBoard()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Call Events if enabled.
        if (board.isCallEvents()) {
            BoardDestroyEvent destroyEvent = new BoardDestroyEvent(event.getPlayer());

            Bukkit.getPluginManager().callEvent(destroyEvent);
            if (destroyEvent.isCancelled()) {
                return;
            }
        }

        getBoard().getBoards().remove(event.getPlayer().getUniqueId());
        event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

}
