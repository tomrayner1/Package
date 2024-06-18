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

public final class LCPacketNametagsOverride extends LCPacket {
	@Getter
	private UUID player;
	@Getter
	private List<String> tags;
	
	public LCPacketNametagsOverride() {
	}
	
	public LCPacketNametagsOverride(UUID player, List<String> tags) {
		this.player = player;
		this.tags = tags;
	}
	
	@Override
	public void write(ByteBufferWrapper buf) throws IOException {
		buf.writeUUID(player);
		buf.buf().writeBoolean(this.tags != null);
		
		if (this.tags != null) {
			buf.writeVarInt(tags.size());
			
			for (String tag : tags) {
				buf.writeString(tag);
			}
		}
	}
	
	@Override
	public void read(ByteBufferWrapper buf) throws IOException {
		this.player = buf.readUUID();
		
		if (buf.buf().readBoolean()) {
			int tagsSize = buf.readVarInt();
			this.tags = new ArrayList<>(tagsSize);
			
			for (int i = 0; i < tagsSize; i++) {
				this.tags.add(buf.readString());
			}
		}
	}
	
	@Override
	public void process(LCNetHandler handler) {
		((LCNetHandlerClient) handler).handleOverrideNametags(this);
	}
}
