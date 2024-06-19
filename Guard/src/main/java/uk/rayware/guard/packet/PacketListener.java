package uk.rayware.guard.packet;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import uk.rayware.guard.Guard;
import uk.rayware.guard.player.PlayerData;

public class PacketListener extends PacketListenerAbstract {

	private final Guard plugin;

	public PacketListener(Guard plugin) {
		super(PacketListenerPriority.MONITOR);
		this.plugin = plugin;
	}

	@Override
	public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
		PlayerData data = plugin.getDataHandler().getDataMap().get(event.getPlayer().getUniqueId());

		PacketProcessor.handleIncoming(event, data);
	}

	@Override
	public void onPacketPlaySend(PacketPlaySendEvent event) {
		PlayerData data = plugin.getDataHandler().getDataMap().get( event.getPlayer().getUniqueId());

		PacketProcessor.handleOutgoing(event, data);
	}
}
