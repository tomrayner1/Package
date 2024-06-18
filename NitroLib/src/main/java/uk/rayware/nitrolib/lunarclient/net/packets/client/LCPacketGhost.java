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

public final class LCPacketGhost extends LCPacket {
	@Getter
	private List<UUID> addGhostLimit;
	@Getter
	private List<UUID> removeGhostLimit;
	
	public LCPacketGhost() {
	}
	
	public LCPacketGhost(List<UUID> addGhostLimit, List<UUID> removeGhostLimit) {
		this.addGhostLimit = addGhostLimit;
		this.removeGhostLimit = removeGhostLimit;
	}
	
	
	@Override
	public void write(ByteBufferWrapper buf) throws IOException {
		buf.writeVarInt(addGhostLimit.size());
		
		for (UUID uuid : addGhostLimit) {
			buf.writeUUID(uuid);
		}
		
		buf.writeVarInt(removeGhostLimit.size());
		
		for (UUID uuid : removeGhostLimit) {
			buf.writeUUID(uuid);
		}
	}
	
	@Override
	public void read(ByteBufferWrapper buf) throws IOException {
		int addListSize = buf.readVarInt();
		this.addGhostLimit = new ArrayList<>(addListSize);
		
		for (int i = 0; i < addListSize; i++) {
			this.addGhostLimit.add(buf.readUUID());
		}
		
		int removeListSize = buf.readVarInt();
		this.removeGhostLimit = new ArrayList<>(removeListSize);
		
		for (int i = 0; i < removeListSize; i++) {
			this.removeGhostLimit.add(buf.readUUID());
		}
	}
	
	@Override
	public void process(LCNetHandler handler) {
		((LCNetHandlerClient) handler).handleGhost(this);
	}
}
