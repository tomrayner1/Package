package uk.rayware.nitrolib.lunarclient.net.packets.common;

import lombok.Getter;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandler;
import uk.rayware.nitrolib.lunarclient.net.util.ByteBufferWrapper;
import uk.rayware.nitrolib.lunarclient.net.util.LCPacket;
import uk.rayware.nitrolib.lunarclient.object.LCWaypoint;

import java.io.IOException;

public class LCPacketWaypointAdd extends LCPacket {
	@Getter
	private String name;
	@Getter
	private String world;
	@Getter
	private int color;
	@Getter
	private int x;
	@Getter
	private int y;
	@Getter
	private int z;
	@Getter
	private boolean forced;
	@Getter
	private boolean visible;
	
	public LCPacketWaypointAdd() {
	}
	
	public LCPacketWaypointAdd(LCWaypoint waypoint) {
		this.color = waypoint.getColor();
		this.forced = waypoint.isForced();
		this.name = waypoint.getName();
		this.visible = waypoint.isVisible();
		this.world = waypoint.getWorld();
		this.x = waypoint.getX();
		this.y = waypoint.getY();
		this.z = waypoint.getZ();
	}
	
	public LCPacketWaypointAdd(String name, String world, int color, int x, int y, int z, boolean forced, boolean visible) {
		this.name = name;
		this.world = world;
		this.color = color;
		this.x = x;
		this.y = y;
		this.z = z;
		this.forced = forced;
		this.visible = visible;
	}
	
	
	@Override
	public void write(ByteBufferWrapper buf) throws IOException {
		buf.writeString(name);
		buf.writeString(world);
		buf.buf().writeInt(color);
		buf.buf().writeInt(x);
		buf.buf().writeInt(y);
		buf.buf().writeInt(z);
		buf.buf().writeBoolean(forced);
		buf.buf().writeBoolean(visible);
	}
	
	@Override
	public void read(ByteBufferWrapper buf) throws IOException {
		this.name = buf.readString();
		this.world = buf.readString();
		this.color = buf.buf().readInt();
		this.x = buf.buf().readInt();
		this.y = buf.buf().readInt();
		this.z = buf.buf().readInt();
		this.forced = buf.buf().readBoolean();
		this.visible = buf.buf().readBoolean();
	}
	
	@Override
	public void process(LCNetHandler handler) {
		handler.handleAddWaypoint(this);
	}
}
