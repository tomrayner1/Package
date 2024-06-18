package uk.rayware.nitrolib.lunarclient.net.packets.client;

import lombok.Getter;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandlerClient;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandler;
import uk.rayware.nitrolib.lunarclient.net.util.ByteBufferWrapper;
import uk.rayware.nitrolib.lunarclient.net.util.LCPacket;

import java.io.IOException;
import java.util.UUID;

public final class LCPacketHologramRemove extends LCPacket {
	@Getter
	private UUID uuid;
	
	public LCPacketHologramRemove() {
	}
	
	public LCPacketHologramRemove(UUID uuid) {
		this.uuid = uuid;
	}
	
	
	@Override
	public void write(ByteBufferWrapper buf) throws IOException {
		buf.writeUUID(uuid);
	}
	
	@Override
	public void read(ByteBufferWrapper buf) throws IOException {
		this.uuid = buf.readUUID();
	}
	
	@Override
	public void process(LCNetHandler handler) {
		((LCNetHandlerClient) handler).handleRemoveHologram(this);
	}
}
