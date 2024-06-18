package uk.rayware.nitrolib.board.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import uk.rayware.nitrolib.board.BoardScoreboard;

@Getter @Setter
public class BoardCreatedEvent extends Event {

    @Getter public static HandlerList handlerList = new HandlerList();

    private boolean cancelled = false;
    private final BoardScoreboard board;

    /**
     * Assemble Board Created Event.
     *
     * @param board of player.
     */
    public BoardCreatedEvent(BoardScoreboard board) {
        this.board = board;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
