package uk.rayware.nitrolib.lunarclient.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class LCPlayerRegisterEvent extends PlayerEvent {
	
	@Getter
	private final static HandlerList handlerList = new HandlerList();
	
	/**
	 * Called whenever a player registers to the LunarClient plugin channel.
	 *
	 * @param player The player registering as a LunarClient player.
	 * */
	public LCPlayerRegisterEvent(Player player) {
		super(player);
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}
}
