package uk.rayware.nitrolib.lunarclient.net.packets.client;

import lombok.Getter;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandlerClient;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandler;
import uk.rayware.nitrolib.lunarclient.net.util.ByteBufferWrapper;
import uk.rayware.nitrolib.lunarclient.net.util.LCPacket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class LCPacketHologram extends LCPacket {
	@Getter
	private UUID uuid;
	@Getter
	private double x;
	@Getter
	private double y;
	@Getter
	private double z;
	@Getter
	private List<String> lines;
	
	public LCPacketHologram() {
	}
	
	public LCPacketHologram(UUID uuid, double x, double y, double z, List<String> lines) {
		this.uuid = uuid;
		this.x = x;
		this.y = y;
		this.z = z;
		this.lines = lines;
	}
	
	
	@Override
	public void write(ByteBufferWrapper buf) throws IOException {
		buf.writeUUID(uuid);
		buf.buf().writeDouble(x);
		buf.buf().writeDouble(y);
		buf.buf().writeDouble(z);
		buf.writeVarInt(lines.size());
		
		for (String line : lines) {
			buf.writeString(line);
		}
	}
	
	@Override
	public void read(ByteBufferWrapper buf) throws IOException {
		this.uuid = buf.readUUID();
		this.x = buf.buf().readDouble();
		this.y = buf.buf().readDouble();
		this.z = buf.buf().readDouble();
		int linesSize = buf.readVarInt();
		this.lines = new ArrayList<>(linesSize);
		
		for (int i = 0; i < linesSize; i++) {
			this.lines.add(buf.readString());
		}
	}
	
	@Override
	public void process(LCNetHandler handler) {
		((LCNetHandlerClient) handler).handleAddHologram(this);
	}
}
