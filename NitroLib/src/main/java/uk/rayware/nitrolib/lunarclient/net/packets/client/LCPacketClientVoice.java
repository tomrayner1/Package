package uk.rayware.nitrolib.lunarclient.net.packets.client;

import lombok.Getter;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandler;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandlerServer;
import uk.rayware.nitrolib.lunarclient.net.util.ByteBufferWrapper;
import uk.rayware.nitrolib.lunarclient.net.util.LCPacket;

import java.io.IOException;

public final class LCPacketClientVoice extends LCPacket {
	@Getter
	private byte[] data;
	
	public LCPacketClientVoice() {
	}
	
	public LCPacketClientVoice(byte[] data) {
		this.data = data;
	}
	
	@Override
	public void write(ByteBufferWrapper buf) throws IOException {
		writeBlob(buf, this.data);
	}
	
	@Override
	public void read(ByteBufferWrapper buf) throws IOException {
		this.data = readBlob(buf);
	}
	
	@Override
	public void process(LCNetHandler handler) {
		((LCNetHandlerServer) handler).handleVoice(this);
	}
}
