package uk.rayware.guard.check.packets;

import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import uk.rayware.guard.check.Check;
import uk.rayware.guard.player.PlayerData;

public class PacketsA extends Check {

	public PacketsA(PlayerData data) {
		super(data, "Packets Check A", 3);
	}

	@Override
	public void handle(PacketPlayReceiveEvent event) {
		if (event.getPacketId() == PacketType.Play.Client.FLYING) {
			WrappedPacketInFlying packet = new WrappedPacketInFlying(event.getNMSPacket());

			if (packet.getPitch() > 90 || packet.getPitch() < -90) {
				vl++;
				fail();
			}
		}
	}
}
