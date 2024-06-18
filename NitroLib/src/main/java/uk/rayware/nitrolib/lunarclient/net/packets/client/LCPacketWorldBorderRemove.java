package uk.rayware.nitrolib.lunarclient.net.packets.client;

import lombok.Getter;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandlerClient;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandler;
import uk.rayware.nitrolib.lunarclient.net.util.ByteBufferWrapper;
import uk.rayware.nitrolib.lunarclient.net.util.LCPacket;

import java.io.IOException;

public final class LCPacketWorldBorderRemove extends LCPacket {
	
	@Getter
	private String id;
	
	public LCPacketWorldBorderRemove() {}
	
	public LCPacketWorldBorderRemove(String id) {
		this.id = id;
	}
	
	@Override
	public void write(ByteBufferWrapper buf) throws IOException {
		buf.writeString(id);
	}
	
	@Override
	public void read(ByteBufferWrapper buf) throws IOException {
		this.id = buf.readString();
	}
	
	@Override
	public void process(LCNetHandler handler) {
		((LCNetHandlerClient) handler).handleWorldBorderRemove(this);
	}
	
}
