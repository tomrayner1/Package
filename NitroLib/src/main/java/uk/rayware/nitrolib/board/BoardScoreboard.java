package uk.rayware.nitrolib.board;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import uk.rayware.nitrolib.board.events.BoardCreatedEvent;

@Getter
public class BoardScoreboard {

    private final Board board;

    private final List<BoardScoreboardEntry> entries = new ArrayList<>();
    private final List<String> identifiers = new ArrayList<>();

    private final UUID uuid;

    /**
     * Assemble Board.
     *
     * @param player that the board belongs to.
     * @param board instance.
     */
    public BoardScoreboard(Player player, Board board) {
        this.uuid = player.getUniqueId();
        this.board = board;
        this.setup(player);
    }

    /**
     * Get's a player's bukkit scoreboard.
     *
     * @return either existing scoreboard or new scoreboard.
     */
    public Scoreboard getScoreboard() {
        Player player = Bukkit.getPlayer(getUuid());
        if (getBoard().isHook() || player.getScoreboard() != Bukkit.getScoreboardManager().getMainScoreboard()) {
            return player.getScoreboard();
        } else {
            return Bukkit.getScoreboardManager().getNewScoreboard();
        }
    }

    /**
     * Get's the player's scoreboard objective.
     *
     * @return either existing objecting or new objective.
     */
    public Objective getObjective() {
        Scoreboard scoreboard = getScoreboard();
        if (scoreboard.getObjective("Assemble") == null) {
            Objective objective = scoreboard.registerNewObjective("Assemble", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(getBoard().getAdapter().getTitle(Bukkit.getPlayer(getUuid())));
            return objective;
        } else {
            return scoreboard.getObjective("Assemble");
        }
    }

    /**
     * Setup the board.
     *
     * @param player who's board to setup.
     */
    private void setup(Player player) {
        Scoreboard scoreboard = getScoreboard();
        player.setScoreboard(scoreboard);
        getObjective();

        // Call Events if enabled.
        if (board.isCallEvents()) {
            BoardCreatedEvent createdEvent = new BoardCreatedEvent(this);
            Bukkit.getPluginManager().callEvent(createdEvent);
        }
    }

    /**
     * Get the board entry at a specific position.
     *
     * @param pos to find entry.
     * @return entry if it isn't out of range.
     */
    public BoardScoreboardEntry getEntryAtPosition(int pos) {
        return pos >= this.entries.size() ? null : this.entries.get(pos);
    }

    /**
     * Get the unique identifier for position in scoreboard.
     *
     * @param position for identifier.
     * @return unique identifier.
     */
    public String getUniqueIdentifier(int position) {
        String identifier = getRandomChatColor(position) + ChatColor.WHITE;

        while (this.identifiers.contains(identifier)) {
            identifier = identifier + getRandomChatColor(position) + ChatColor.WHITE;
        }

        // This is rare, but just in case, make the method recursive
        if (identifier.length() > 16) {
            return this.getUniqueIdentifier(position);
        }

        // Add our identifier to the list so there are no duplicates
        this.identifiers.add(identifier);

        return identifier;
    }

    /**
     * Gets a ChatColor based off the position in the collection.
     *
     * @param position of entry.
     * @return ChatColor adjacent to position.
     */
    private String getRandomChatColor(int position) {
        return board.getChatColorCache()[position].toString();
    }

}
