package uk.rayware.nitrolib.lunarclient.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import uk.rayware.nitrolib.lunarclient.net.util.LCPacket;

public class LCPacketReceivedEvent extends PlayerEvent {
	
	@Getter
	private final static HandlerList handlerList = new HandlerList();
	@Getter
	private final LCPacket packet;
	
	public LCPacketReceivedEvent(Player player, LCPacket packet) {
		super(player);
		this.packet = packet;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}
}
