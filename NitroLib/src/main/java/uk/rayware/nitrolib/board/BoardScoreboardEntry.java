package uk.rayware.nitrolib.board;

import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import lombok.Setter;

public class BoardScoreboardEntry {

    private final BoardScoreboard board;

    private Team team;
    @Setter
    private String text, identifier;

    /**
     * Assemble Board Entry
     *
     * @param board    that entry belongs to.
     * @param text     of entry.
     * @param position of entry.
     */
    public BoardScoreboardEntry(BoardScoreboard board, String text, int position) {
        this.board = board;
        this.text = text;
        this.identifier = this.board.getUniqueIdentifier(position);

        this.setup();
    }

    /**
     * Setup Board Entry.
     */
    public void setup() {
        final Scoreboard scoreboard = this.board.getScoreboard();

        if (scoreboard == null) {
            return;
        }

        String teamName = this.identifier;

        // This shouldn't happen, but just in case.
        if (teamName.length() > 16) {
            teamName = teamName.substring(0, 16);
        }

        Team team = scoreboard.getTeam(teamName);

        // Register the team if it does not exist.
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
        }

        // Add the entry to the team.
        if (!team.getEntries().contains(this.identifier)) {
            team.addEntry(this.identifier);
        }

        // Add the entry if it does not exist.
        if (!this.board.getEntries().contains(this)) {
            this.board.getEntries().add(this);
        }

        this.team = team;
    }

    /**
     * Send Board Entry Update.
     *
     * @param position of entry.
     */
    public void send(int position) {
        // Set Prefix & Suffix.
        String[] split = BoardUtils.splitTeamText(text);
        this.team.setPrefix(split[0]);
        this.team.setSuffix(split[1]);

        // Set the score
        this.board.getObjective().getScore(this.identifier).setScore(position);
    }

    /**
     * Remove Board Entry from Board.
     */
    public void remove() {
        this.board.getIdentifiers().remove(this.identifier);
        this.board.getScoreboard().resetScores(this.identifier);
    }

}
