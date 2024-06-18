package uk.rayware.nitrolib.lunarclient.net.packets.client;

import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandlerClient;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandler;
import uk.rayware.nitrolib.lunarclient.net.util.ByteBufferWrapper;
import uk.rayware.nitrolib.lunarclient.net.util.LCPacket;
import uk.rayware.nitrolib.lunarclient.net.util.ModSettings;

import java.io.IOException;

public class LCPacketModSettings extends LCPacket  {
	
	private ModSettings settings;
	
	public LCPacketModSettings() {}
	
	public LCPacketModSettings(ModSettings settings) {
		this.settings = settings;
	}
	
	@Override
	public void write(ByteBufferWrapper buf) throws IOException {
		buf.writeString(ModSettings.GSON.toJson(this.settings));
	}
	
	@Override
	public void read(ByteBufferWrapper buf) throws IOException {
		this.settings = ModSettings.GSON.fromJson(buf.readString(), ModSettings.class);
	}
	
	@Override
	public void process(LCNetHandler handler) {
		((LCNetHandlerClient) handler).handleModSettings(this);
	}
	
	public ModSettings getSettings() {
		return settings;
	}
}
