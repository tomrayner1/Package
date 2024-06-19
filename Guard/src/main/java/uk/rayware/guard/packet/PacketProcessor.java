package uk.rayware.guard.packet;

import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import uk.rayware.guard.check.Check;
import uk.rayware.guard.player.PlayerData;

public class PacketProcessor {

	public static void handleIncoming(PacketPlayReceiveEvent event, PlayerData data) {
		//if (PacketType.Play.Client.Util.isInstanceOfFlying(event.getPacketId())) {
		//	WrappedPacketInFlying flying = new WrappedPacketInFlying(event.getNMSPacket());
		//}

		for (Check check : data.getChecks()) {
			if (check.isEnabled()) {
				check.handle(event);
			}
		}
	}

	public static void handleOutgoing(PacketPlaySendEvent event, PlayerData data) {
		for (Check check : data.getChecks()) {
			if (check.isEnabled()) {
				check.handleOutgoing(event);
			}
		}
	}

}
