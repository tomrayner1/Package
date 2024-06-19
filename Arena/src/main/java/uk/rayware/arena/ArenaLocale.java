package uk.rayware.arena;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

@UtilityClass
public final class ArenaLocale {

	private final String LEFT_WRAPPER = "&9» &r";
	private final String RIGHT_WRAPPER = "&9 «&r";

	public final String CANT_DO_THIS = compile("&cYou cannot do this!");
	public final String ERROR = compile("&cAn error has occurred whilst performing this action, contact an admin!");
	public final String NO_SELECTION = compile("&cPlease make a selection before doing this!");
	public final String CANT_FIND_KIT = compile("&cCannot find kit '&e%s&c'.");
	public final String NEED_TO_BE_IN_LOBBY = compile("&cYou need to be in the lobby to do this!");

	public final String NOW_SPECTATING = compile("&aYou are now spectating.");
	public final String NOW_SPECTATING_TARGET = compile("&aYou are now spectating&r %s&a.");
	public final String NO_LONGER_SPECTATING = compile("&cYou are no longer spectating.");

	public final String APPLIED_KIT = compile("&aApplied kit &e%s&a.");
	public final String CLAIMED_SPAWN = compile("&aYou have updated the spawn region.");

	public final String SCOREBOARD_ONLINE = "&6Online:&f %s";
	public final String SCOREBOARD_IN_ARENA = "&6In Arena:&f %s";
	public final String SCOREBOARD_IN_FIGHTS = "&6In Fights:&f %s";

	public String wrap(String s) {
		return compile(LEFT_WRAPPER + s + RIGHT_WRAPPER);
	}

	private String compile(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

}
