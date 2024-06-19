package uk.rayware.guard.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import uk.rayware.guard.check.Check;
import uk.rayware.guard.player.PlayerData;

@AllArgsConstructor
@Getter
public class CheckFailEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private final Check check;
	private final PlayerData playerData;
	private final String data;
	private final long timestamp = System.currentTimeMillis();

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
