package uk.rayware.nitrolib.lunarclient.net.packets.client;

import lombok.Getter;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandlerClient;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandler;
import uk.rayware.nitrolib.lunarclient.net.util.ByteBufferWrapper;
import uk.rayware.nitrolib.lunarclient.net.util.LCPacket;

import java.io.IOException;
import java.util.*;

public final class LCPacketNametagsUpdate extends LCPacket {
	@Getter
	private Map<UUID, List<String>> playersMap;
	
	public LCPacketNametagsUpdate() {
	}
	
	public LCPacketNametagsUpdate(Map<UUID, List<String>> playersMap) {
		this.playersMap = playersMap;
	}
	
	@Override
	public void write(ByteBufferWrapper buf) throws IOException {
		buf.writeVarInt(playersMap == null ? -1 : playersMap.size());
		
		if (playersMap != null) {
			playersMap.forEach((uuid, tags) -> {
				buf.writeUUID(uuid);
				buf.writeVarInt(tags.size());
				for (String tag : tags) {
					buf.writeString(tag);
				}
			});
		}
	}
	
	@Override
	public void read(ByteBufferWrapper buf) throws IOException {
		int playersMapSize = buf.readVarInt();
		
		if (playersMapSize != -1) {
			this.playersMap = new HashMap<>();
			
			for (int i = 0; i < playersMapSize; i++) {
				UUID uuid = buf.readUUID();
				int tagsSize = buf.readVarInt();
				List<String> tags = new ArrayList<>(tagsSize);
				
				for (int j = 0; j < tagsSize; j++) {
					tags.add(buf.readString());
				}
				
				this.playersMap.put(uuid, tags);
			}
		}
	}
	
	@Override
	public void process(LCNetHandler handler) {
		((LCNetHandlerClient) handler).handleNametagsUpdate(this);
	}
}
