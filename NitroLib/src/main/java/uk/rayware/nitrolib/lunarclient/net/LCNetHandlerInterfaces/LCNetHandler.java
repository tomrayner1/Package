package uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces;

import uk.rayware.nitrolib.lunarclient.net.packets.common.LCPacketEmoteBroadcast;
import uk.rayware.nitrolib.lunarclient.net.packets.common.LCPacketWaypointAdd;
import uk.rayware.nitrolib.lunarclient.net.packets.common.LCPacketWaypointRemove;

public interface LCNetHandler {
	void handleAddWaypoint(LCPacketWaypointAdd packet);
	void handleRemoveWaypoint(LCPacketWaypointRemove packet);
	void handleEmote(LCPacketEmoteBroadcast packet);
}
