package uk.rayware.nitrolib.lunarclient.net.packets.client;

import lombok.Getter;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandlerClient;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandler;
import uk.rayware.nitrolib.lunarclient.net.util.ByteBufferWrapper;
import uk.rayware.nitrolib.lunarclient.net.util.LCPacket;

import java.io.IOException;

public final class LCPacketBossBar extends LCPacket {
	@Getter
	private int action;
	@Getter
	private String text;
	@Getter
	private float health;
	
	public LCPacketBossBar() {
	}
	
	public LCPacketBossBar(int action, String text, float health) {
		this.action = action;
		this.text = text;
		this.health = health;
	}
	
	
	@Override
	public void write(ByteBufferWrapper buf) throws IOException {
		buf.writeVarInt(action);
		
		if (action == 0) {
			buf.writeString(text);
			buf.buf().writeFloat(health);
		}
	}
	
	@Override
	public void read(ByteBufferWrapper buf) throws IOException {
		this.action = buf.readVarInt();
		
		if (action == 0) {
			this.text = buf.readString();
			this.health = buf.buf().readFloat();
		}
	}
	
	@Override
	public void process(LCNetHandler handler) {
		((LCNetHandlerClient) handler).handleBossBar(this);
	}
}
