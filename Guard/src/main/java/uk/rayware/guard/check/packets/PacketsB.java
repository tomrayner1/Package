package uk.rayware.guard.check.packets;

import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import uk.rayware.guard.check.Check;
import uk.rayware.guard.player.PlayerData;

public class PacketsB extends Check {

	private int streak;

	public PacketsB(PlayerData data) {
		super(data, "Packets Check B", 10);
	}

	@Override
	public void handle(PacketPlayReceiveEvent event) {
		if (event.getPacketId() == PacketType.Play.Client.POSITION || event.getPacketId() == PacketType.Play.Client.POSITION_LOOK) {
			streak = 0;
		} else if (event.getPacketId() == PacketType.Play.Client.FLYING) {
			if (++streak > 20) {
				vl++;
				fail("S: " + streak);
			}
		}
	}
}
