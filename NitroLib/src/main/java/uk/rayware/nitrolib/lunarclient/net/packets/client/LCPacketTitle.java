package uk.rayware.nitrolib.lunarclient.net.packets.client;

import lombok.Getter;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandlerClient;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandler;
import uk.rayware.nitrolib.lunarclient.net.util.ByteBufferWrapper;
import uk.rayware.nitrolib.lunarclient.net.util.LCPacket;

import java.io.IOException;

public final class LCPacketTitle extends LCPacket {
	
	@Getter
	private String type;
	@Getter
	private String message;
	@Getter
	private float scale;
	@Getter
	private long displayTimeMs;
	@Getter
	private long fadeInTimeMs;
	@Getter
	private long fadeOutTimeMs;
	
	public LCPacketTitle() {
	}
	
	public LCPacketTitle(String type, String message, long displayTimeMs, long fadeInTimeMs, long fadeOutTimeMs) {
		this(type, message, 1F, displayTimeMs, fadeInTimeMs, fadeOutTimeMs);
	}
	
	public LCPacketTitle(String type, String message, float scale, long displayTimeMs, long fadeInTimeMs, long fadeOutTimeMs) {
		this.type = type;
		this.message = message;
		this.scale = scale;
		this.displayTimeMs = displayTimeMs;
		this.fadeInTimeMs = fadeInTimeMs;
		this.fadeOutTimeMs = fadeOutTimeMs;
	}
	
	@Override
	public void write(ByteBufferWrapper buf) throws IOException {
		buf.writeString(type);
		buf.writeString(message);
		buf.buf().writeFloat(scale);
		buf.buf().writeLong(displayTimeMs);
		buf.buf().writeLong(fadeInTimeMs);
		buf.buf().writeLong(fadeOutTimeMs);
	}
	
	@Override
	public void read(ByteBufferWrapper buf) throws IOException {
		this.type = buf.readString();
		this.message = buf.readString();
		this.scale = buf.buf().readFloat();
		this.displayTimeMs = buf.buf().readLong();
		this.fadeInTimeMs = buf.buf().readLong();
		this.fadeOutTimeMs = buf.buf().readLong();
	}
	
	@Override
	public void process(LCNetHandler handler) {
		((LCNetHandlerClient) handler).handleTitle(this);
	}
	
}
