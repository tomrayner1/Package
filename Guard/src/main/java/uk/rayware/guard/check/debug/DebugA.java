package uk.rayware.guard.check.debug;

import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import uk.rayware.guard.check.Check;
import uk.rayware.guard.player.PlayerData;

public class DebugA extends Check {

	public DebugA(PlayerData data) {
		super(data, "Debug Check A", 4);
	}

	@Override
	public void handle(PacketPlayReceiveEvent event) {
		if (event.getPacketId() == PacketType.Play.Client.ARM_ANIMATION) {
			fail();
			vl++;
		}
	}
}
