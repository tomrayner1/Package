package uk.rayware.nitrolib.lunarclient.util;

import uk.rayware.nitrolib.lunarclient.LunarClientAPI;
import uk.rayware.nitrolib.lunarclient.net.util.LCPacket;
import org.bukkit.entity.Player;

public interface LCPacketWrapper<T extends LCPacket> {
	/**
	 * The packet sent to the player
	 *
	 * @return LCPacket to send to LunarClient player
	 */
	T getPacket();
	
	/**
	 * Send wrapped packet to a LunarClient player
	 *
	 * @param player The online LunarClient player to receive the packet
	 */
	default void send(Player player) {
		send(player, getPacket());
	}
	
	/**
	 * Send any class which extends LCPacket to a LunarClient player
	 * <p>
	 * Intended only to send wrapped packets, can send other types of packet althogh it's better to use {@link uk.rayware.nitrolib.lunarclient.LunarClientAPI} to do this.
	 *
	 * @param player An online LunarClient player to receive the packet
	 * @param packet The packet to send to the user
	 */
	default void send(Player player, LCPacket packet) {
		//LunarClientAPI.getInstance().sendPacket(player, packet);
	}
}
