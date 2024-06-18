package uk.rayware.nitrolib.lunarclient.net.packets.client;

import lombok.Getter;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandlerClient;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandler;
import uk.rayware.nitrolib.lunarclient.net.util.ByteBufferWrapper;
import uk.rayware.nitrolib.lunarclient.net.util.LCPacket;

import java.io.IOException;

public final class LCPacketNotification extends LCPacket {
	@Getter
	private String message;
	@Getter
	private long durationMs;
	@Getter
	private String level;
	
	public LCPacketNotification() {
	}
	
	public LCPacketNotification(String message, long durationMs, String level) {
		this.message = message;
		this.durationMs = durationMs;
		this.level = level;
	}
	
	@Override
	public void write(ByteBufferWrapper buf) throws IOException {
		buf.writeString(message);
		buf.buf().writeLong(durationMs);
		buf.writeString(level);
	}
	
	@Override
	public void read(ByteBufferWrapper buf) throws IOException {
		this.message = buf.readString();
		this.durationMs = buf.buf().readLong();
		this.level = buf.readString();
	}
	
	@Override
	public void process(LCNetHandler handler) {
		((LCNetHandlerClient) handler).handleNotification(this);
	}
}
