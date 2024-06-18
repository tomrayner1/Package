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

public final class LCPacketHologramUpdate extends LCPacket {
	@Getter
	private UUID uuid;
	@Getter
	private List<String> lines;
	
	public LCPacketHologramUpdate() {
	}
	
	public LCPacketHologramUpdate(UUID uuid, List<String> lines) {
		this.uuid = uuid;
		this.lines = lines;
	}
	
	
	@Override
	public void write(ByteBufferWrapper buf) throws IOException {
		buf.writeUUID(uuid);
		buf.writeVarInt(lines.size());
		
		for (String line : lines) {
			buf.writeString(line);
		}
	}
	
	@Override
	public void read(ByteBufferWrapper buf) throws IOException {
		this.uuid = buf.readUUID();
		int linesSize = buf.readVarInt();
		this.lines = new ArrayList<>(linesSize);
		
		for (int i = 0; i < linesSize; i++) {
			this.lines.add(buf.readString());
		}
	}
	
	@Override
	public void process(LCNetHandler handler) {
		((LCNetHandlerClient) handler).handleUpdateHologram(this);
	}
}
