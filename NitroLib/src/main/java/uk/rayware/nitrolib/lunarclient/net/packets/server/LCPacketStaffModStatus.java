package uk.rayware.nitrolib.lunarclient.net.packets.server;

import lombok.Getter;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandler;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandlerServer;
import uk.rayware.nitrolib.lunarclient.net.util.ByteBufferWrapper;
import uk.rayware.nitrolib.lunarclient.net.util.LCPacket;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class LCPacketStaffModStatus extends LCPacket {
	@Getter
	private final Set<String> enabled;
	
	public LCPacketStaffModStatus() {
		this.enabled = new HashSet<>();
	}
	
	public LCPacketStaffModStatus(Set<String> enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public void write(ByteBufferWrapper buf) throws IOException {
		buf.writeVarInt(enabled.size());
		
		for (String mod : enabled) {
			buf.writeString(mod);
		}
	}
	
	@Override
	public void read(ByteBufferWrapper buf) throws IOException {
		int size = buf.readVarInt();
		
		for (int i = 0; i < size; i++) {
			enabled.add(buf.readString());
		}
	}
	
	@Override
	public void process(LCNetHandler handler) {
		((LCNetHandlerServer) handler).handleStaffModStatus(this);
	}
}
