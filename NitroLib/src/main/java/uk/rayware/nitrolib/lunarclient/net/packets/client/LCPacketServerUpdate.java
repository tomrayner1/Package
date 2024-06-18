package uk.rayware.nitrolib.lunarclient.net.packets.client;

import lombok.Getter;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandlerClient;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandler;
import uk.rayware.nitrolib.lunarclient.net.util.ByteBufferWrapper;
import uk.rayware.nitrolib.lunarclient.net.util.LCPacket;

import java.io.IOException;

public final class LCPacketServerUpdate extends LCPacket {
	@Getter
	private String server;
	
	public LCPacketServerUpdate() {
	}
	
	public LCPacketServerUpdate(String server) {
		this.server = server;
	}
	
	@Override
	public void write(ByteBufferWrapper buf) throws IOException {
		buf.writeString(server);
	}
	
	@Override
	public void read(ByteBufferWrapper buf) throws IOException {
		this.server = buf.readString();
	}
	
	@Override
	public void process(LCNetHandler handler) {
		((LCNetHandlerClient) handler).handleServerUpdate(this);
	}
}
