package uk.rayware.nitrolib.lunarclient.net.packets.client;

import lombok.Getter;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandler;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandlerServer;
import uk.rayware.nitrolib.lunarclient.net.util.ByteBufferWrapper;
import uk.rayware.nitrolib.lunarclient.net.util.LCPacket;

import java.io.IOException;
import java.util.UUID;

public final class LCPacketVoiceChannelSwitch extends LCPacket {
	@Getter
	private UUID switchingTo;
	
	public LCPacketVoiceChannelSwitch() {
	}
	
	public LCPacketVoiceChannelSwitch(UUID switchingTo) {
		this.switchingTo = switchingTo;
	}
	
	
	@Override
	public void write(ByteBufferWrapper buf) throws IOException {
		buf.writeUUID(switchingTo);
	}
	
	@Override
	public void read(ByteBufferWrapper buf) throws IOException {
		this.switchingTo = buf.readUUID();
	}
	
	@Override
	public void process(LCNetHandler handler) {
		((LCNetHandlerServer) handler).handleVoiceChannelSwitch(this);
	}
}
