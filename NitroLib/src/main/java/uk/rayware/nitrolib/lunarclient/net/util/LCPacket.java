package uk.rayware.nitrolib.lunarclient.net.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import uk.rayware.nitrolib.lunarclient.LunarClientAPI;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandler;
import uk.rayware.nitrolib.lunarclient.net.packets.client.*;
import uk.rayware.nitrolib.lunarclient.net.packets.server.*;
import uk.rayware.nitrolib.lunarclient.net.packets.common.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class LCPacket {
	@SuppressWarnings("rawtypes")
	private static final Map<Class, Integer> classToID = new HashMap<>();
	@SuppressWarnings("rawtypes")
	private static final Map<Integer, Class> idToClass = new HashMap<>();
	
	static {
		// server
		addPacket(0, LCPacketClientVoice.class);
		addPacket(16, LCPacketVoice.class);
		addPacket(1, LCPacketVoiceChannelSwitch.class);
		addPacket(2, LCPacketVoiceMute.class);
		
		addPacket(17, LCPacketVoiceChannel.class);
		addPacket(18, LCPacketVoiceChannelRemove.class);
		addPacket(19, LCPacketVoiceChannelUpdate.class);
		
		// client
		addPacket(3, LCPacketCooldown.class);
		addPacket(4, LCPacketHologram.class);
		addPacket(6, LCPacketHologramRemove.class);
		addPacket(5, LCPacketHologramUpdate.class);
		addPacket(7, LCPacketNametagsOverride.class);
		addPacket(8, LCPacketNametagsUpdate.class);
		addPacket(9, LCPacketNotification.class);
		addPacket(10, LCPacketServerRule.class);
		addPacket(11, LCPacketServerUpdate.class);
		addPacket(12, LCPacketStaffModState.class);
		addPacket(13, LCPacketTeammates.class);
		addPacket(14, LCPacketTitle.class);
		addPacket(15, LCPacketUpdateWorld.class);
		addPacket(20, LCPacketWorldBorder.class);
		addPacket(21, LCPacketWorldBorderRemove.class);
		addPacket(22, LCPacketWorldBorderUpdate.class);
		addPacket(25, LCPacketGhost.class);
		addPacket(28, LCPacketBossBar.class);
		addPacket(29, LCPacketWorldBorderCreateNew.class);
		addPacket(30, LCPacketWorldBorderUpdateNew.class);
		addPacket(31, LCPacketModSettings.class);
		
		// shared
		addPacket(26, LCPacketEmoteBroadcast.class);
		addPacket(23, LCPacketWaypointAdd.class);
		addPacket(24, LCPacketWaypointRemove.class);
	}
	
	
	private Object attachment;
	
	public static LCPacket handle(byte[] data) {
		return handle(data, null);
	}
	
	public static LCPacket handle(byte[] data, Object attachment) {
		ByteBufferWrapper buf = new ByteBufferWrapper(Unpooled.wrappedBuffer(data));
		
		int packetID = buf.readVarInt();
		@SuppressWarnings("rawtypes")
		Class packetClass = idToClass.get(packetID);
		
		if (packetClass != null) {
			try {
				@SuppressWarnings("deprecation")
				LCPacket packet = (LCPacket) packetClass.newInstance();
				
				packet.attach(attachment);
				packet.read(buf);
				
				return packet;
			} catch (IOException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static byte[] getPacketData(LCPacket packet) {
		return getPacketBuf(packet).array();
	}
	
	public static ByteBuf getPacketBuf(LCPacket packet) {
		ByteBufferWrapper buf = new ByteBufferWrapper(Unpooled.buffer());
		buf.writeVarInt(classToID.get(packet.getClass()));
		
		try {
			packet.write(buf);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buf.buf();
	}
	
	@SuppressWarnings("rawtypes")
	private static void addPacket(int id, Class clazz) {
		if (classToID.containsKey(clazz)) {
			throw new IllegalArgumentException("Duplicate packet class (" + clazz.getSimpleName() + "), already used by " + classToID.get(clazz));
		} else if (idToClass.containsKey(id)) {
			throw new IllegalArgumentException("Duplicate packet ID (" + id + "), already used by " + idToClass.get(id).getSimpleName());
		}
		
		classToID.put(clazz, id);
		idToClass.put(id, clazz);
	}
	
	
	
	public abstract void write(ByteBufferWrapper buf) throws IOException;
	
	public abstract void read(ByteBufferWrapper buf) throws IOException;
	
	public abstract void process(LCNetHandler handler);
	
	public <T> void attach(T object) {
		this.attachment = object;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getAttacment() {
		return (T) attachment;
	}
	
	protected void writeBlob(ByteBufferWrapper buf, byte[] bytes) {
		buf.buf().writeShort(bytes.length);
		buf.buf().writeBytes(bytes);
	}
	
	protected byte[] readBlob(ByteBufferWrapper buf) {
		short key = buf.buf().readShort();
		if (key < 0) {
			LunarClientAPI.getInstance().log("Blob key is negative!");
		} else {
			byte[] blob = new byte[key];
			buf.buf().readBytes(blob);
			return blob;
		}
		
		return null;
	}
	
}
