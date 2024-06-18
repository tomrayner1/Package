package uk.rayware.nitrolib.lunarclient.net.packets.common;

import lombok.Getter;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandler;
import uk.rayware.nitrolib.lunarclient.net.util.ByteBufferWrapper;
import uk.rayware.nitrolib.lunarclient.net.util.LCPacket;

import java.io.IOException;
import java.util.UUID;

public final class LCPacketEmoteBroadcast extends LCPacket {
	
	@Getter
	private UUID uuid;
	
	@Getter
	private int emoteID;
	
	public LCPacketEmoteBroadcast() {}
	
	public LCPacketEmoteBroadcast(UUID uuid, int emoteID) {
		this.uuid = uuid;
		this.emoteID = emoteID;
	}
	
	@Override
	public void write(ByteBufferWrapper buf) throws IOException {
		buf.writeUUID(uuid);
		buf.buf().writeInt(emoteID);
	}
	
	@Override
	public void read(ByteBufferWrapper buf) throws IOException {
		this.uuid = buf.readUUID();
		this.emoteID = buf.buf().readInt();
	}
	
	@Override
	public void process(LCNetHandler handler) {
		handler.handleEmote(this);
	}
}
