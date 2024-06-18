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
public final class LCPacketVoiceChannelRemove extends LCPacket {
	
	@Getter private UUID uuid;
	
	@Override
	public void write(ByteBufferWrapper b) {
		b.writeUUID(uuid);
	}
	
	@Override
	public void read(ByteBufferWrapper b) {
		this.uuid = b.readUUID();
	}
	
	@Override
	public void process(LCNetHandler handler) {
		((LCNetHandlerClient) handler).handleVoiceChannelDelete(this);
	}
}
