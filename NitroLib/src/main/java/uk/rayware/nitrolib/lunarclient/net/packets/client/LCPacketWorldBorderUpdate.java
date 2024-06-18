package uk.rayware.nitrolib.lunarclient.net.packets.client;

import lombok.Getter;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandlerClient;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandler;
import uk.rayware.nitrolib.lunarclient.net.util.ByteBufferWrapper;
import uk.rayware.nitrolib.lunarclient.net.util.LCPacket;

import java.io.IOException;

public final class LCPacketWorldBorderUpdate extends LCPacket {
	
	@Getter
	private String id;
	@Getter
	private double minX;
	@Getter
	private double minZ;
	@Getter
	private double maxX;
	@Getter
	private double maxZ;
	@Getter
	private int durationTicks;
	
	public LCPacketWorldBorderUpdate() {
	}
	
	public LCPacketWorldBorderUpdate(String id, double minX, double minZ, double maxX, double maxZ, int durationTicks) {
		this.id = id;
		this.minX = minX;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxZ = maxZ;
		this.durationTicks = durationTicks;
	}
	
	@Override
	public void write(ByteBufferWrapper buf) throws IOException {
		buf.writeString(id);
		buf.buf().writeDouble(minX);
		buf.buf().writeDouble(minZ);
		buf.buf().writeDouble(maxX);
		buf.buf().writeDouble(maxZ);
		buf.buf().writeInt(durationTicks);
	}
	
	@Override
	public void read(ByteBufferWrapper buf) throws IOException {
		this.id = buf.readString();
		this.minX = buf.buf().readDouble();
		this.minZ = buf.buf().readDouble();
		this.maxX = buf.buf().readDouble();
		this.maxZ = buf.buf().readDouble();
		this.durationTicks = buf.buf().readInt();
	}
	
	@Override
	public void process(LCNetHandler handler) {
		((LCNetHandlerClient) handler).handleWorldBorderUpdate(this);
	}
	
}
