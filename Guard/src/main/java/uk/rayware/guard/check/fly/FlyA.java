package uk.rayware.guard.check.fly;

import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import org.bukkit.GameMode;
import uk.rayware.guard.check.Check;
import uk.rayware.guard.player.PlayerData;

public class FlyA extends Check {

	private int airTicks;

	public FlyA(PlayerData data) {
		super(data, "Fly Check A", 10);
	}

	@Override
	public void handle(PacketPlayReceiveEvent event) {

	}
}
