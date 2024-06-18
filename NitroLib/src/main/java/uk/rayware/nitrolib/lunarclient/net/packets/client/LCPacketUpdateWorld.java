package uk.rayware.nitrolib.lunarclient.net.packets.client;

import lombok.Getter;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandlerClient;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandler;
import uk.rayware.nitrolib.lunarclient.net.util.ByteBufferWrapper;
import uk.rayware.nitrolib.lunarclient.net.util.LCPacket;

import java.io.IOException;

public final class LCPacketUpdateWorld extends LCPacket {
	
	@Getter
	private String world;
	
	public LCPacketUpdateWorld() {}
	
	public LCPacketUpdateWorld(String world) {
		this.world = world;
	}
	
	@Override
	public void write(ByteBufferWrapper buf) throws IOException {
		buf.writeString(world);
	}
	
	@Override
	public void read(ByteBufferWrapper buf) throws IOException {
		this.world = buf.readString();
	}
	
	@Override
	public void process(LCNetHandler handler) {
		((LCNetHandlerClient) handler).handleUpdateWorld(this);
	}
	
}
