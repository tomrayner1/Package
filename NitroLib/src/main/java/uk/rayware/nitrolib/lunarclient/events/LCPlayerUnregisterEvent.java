package uk.rayware.nitrolib.lunarclient.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class LCPlayerUnregisterEvent extends PlayerEvent {
	
	@Getter
	private final static HandlerList handlerList = new HandlerList();
	
	public LCPlayerUnregisterEvent(Player player) {
		super(player);
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}
}
