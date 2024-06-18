package uk.rayware.nitrolib.lunarclient.net.packets.client;

import lombok.Getter;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandlerClient;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandler;
import uk.rayware.nitrolib.lunarclient.net.util.ByteBufferWrapper;
import uk.rayware.nitrolib.lunarclient.net.util.LCPacket;

import java.io.IOException;

public final class LCPacketStaffModState extends LCPacket {
	@Getter
	private String mod;
	@Getter
	private boolean state;
	
	public LCPacketStaffModState() {
	}
	
	public LCPacketStaffModState(String mod, boolean state) {
		this.mod = mod;
		this.state = state;
	}
	
	@Override
	public void write(ByteBufferWrapper buf) throws IOException {
		buf.writeString(mod);
		buf.buf().writeBoolean(state);
	}
	
	@Override
	public void read(ByteBufferWrapper buf) throws IOException {
		this.mod = buf.readString();
		this.state = buf.buf().readBoolean();
	}
	
	@Override
	public void process(LCNetHandler handler) {
		((LCNetHandlerClient) handler).handleStaffModState(this);
	}
}
