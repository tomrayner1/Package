package uk.rayware.nitrolib.lunarclient.net.packets.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandlerClient;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandler;
import uk.rayware.nitrolib.lunarclient.net.util.ByteBufferWrapper;
import uk.rayware.nitrolib.lunarclient.net.util.LCPacket;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public final class LCPacketVoiceChannelUpdate extends LCPacket {
	
	/**
	 * 0 for adding a player
	 * 1 for removing a player
	 * 2 for marking a player as listening
	 * 3 for marking a player as deafened
	 */
	@Getter public int status;
	
	@Getter private UUID channelUuid;
	
	@Getter private UUID uuid;
	
	@Getter private String name;
	
	@Override
	public void write(ByteBufferWrapper b) {
		b.writeVarInt(status);
		b.writeUUID(channelUuid);
		b.writeUUID(uuid);
		b.writeString(name);
	}
	
	@Override
	public void read(ByteBufferWrapper b) {
		this.status = b.readVarInt();
		this.channelUuid = b.readUUID();
		this.uuid = b.readUUID();
		this.name = b.readString();
	}
	
	@Override
	public void process(LCNetHandler handler) {
		((LCNetHandlerClient) handler).handleVoiceChannelUpdate(this);
	}
}
