package uk.rayware.nitrolib.lunarclient.net.packets.client;

import lombok.Getter;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandler;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandlerServer;
import uk.rayware.nitrolib.lunarclient.net.util.ByteBufferWrapper;
import uk.rayware.nitrolib.lunarclient.net.util.LCPacket;

import java.io.IOException;
import java.util.UUID;

public final class LCPacketVoiceMute extends LCPacket {
	
	@Getter
	private UUID muting;
	
	@Getter
	private int volume;
	
	public LCPacketVoiceMute() {
	}
	
	public LCPacketVoiceMute(UUID muting) {
		this.muting = muting;
	}
	
	public LCPacketVoiceMute(UUID muting, int volume) {
		this.muting = muting;
		this.volume = volume;
	}
	
	@Override
	public void write(ByteBufferWrapper buf) throws IOException {
		buf.writeUUID(muting);
		buf.writeVarInt(volume);
	}
	
	@Override
	public void read(ByteBufferWrapper buf) throws IOException {
		this.muting = buf.readUUID();
		this.volume = buf.readVarInt();
	}
	
	@Override
	public void process(LCNetHandler handler) {
		((LCNetHandlerServer) handler).handleVoiceMute(this);
	}
}
