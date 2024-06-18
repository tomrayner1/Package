package uk.rayware.nitrolib.lunarclient.net.packets.client;

import lombok.Getter;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandlerClient;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandler;
import uk.rayware.nitrolib.lunarclient.net.util.ByteBufferWrapper;
import uk.rayware.nitrolib.lunarclient.net.util.LCPacket;

import java.io.IOException;

public final class LCPacketCooldown extends LCPacket {
	@Getter
	private String message;
	@Getter
	private long durationMs;
	@Getter
	private int iconID;
	
	public LCPacketCooldown() {
	}
	
	public LCPacketCooldown(String message, long durationMs, int iconID) {
		this.message = message;
		this.durationMs = durationMs;
		this.iconID = iconID;
	}
	
	@Override
	public void write(ByteBufferWrapper buf) throws IOException {
		buf.writeString(message);
		buf.buf().writeLong(durationMs);
		buf.buf().writeInt(iconID);
	}
	
	@Override
	public void read(ByteBufferWrapper buf) throws IOException {
		this.message = buf.readString();
		this.durationMs = buf.buf().readLong();
		this.iconID = buf.buf().readInt();
	}
	
	@Override
	public void process(LCNetHandler handler) {
		((LCNetHandlerClient) handler).handleCooldown(this);
	}
}
