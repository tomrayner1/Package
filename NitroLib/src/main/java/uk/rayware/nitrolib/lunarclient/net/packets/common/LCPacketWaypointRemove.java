package uk.rayware.nitrolib.lunarclient.net.packets.common;

import lombok.Getter;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandler;
import uk.rayware.nitrolib.lunarclient.net.util.ByteBufferWrapper;
import uk.rayware.nitrolib.lunarclient.net.util.LCPacket;

import java.io.IOException;

public class LCPacketWaypointRemove extends LCPacket {
	@Getter
	private String name;
	@Getter
	private String world;

	public LCPacketWaypointRemove() {}

	public LCPacketWaypointRemove(String name, String world) {
		this.name = name;
		this.world = world;
	}
	
	
	@Override
	public void write(ByteBufferWrapper buf) throws IOException {
		buf.writeString(name);
		buf.writeString(world);
	}
	
	@Override
	public void read(ByteBufferWrapper buf) throws IOException {
		this.name = buf.readString();
		this.world = buf.readString();
	}
	
	@Override
	public void process(LCNetHandler handler) {
		handler.handleRemoveWaypoint(this);
	}
}
