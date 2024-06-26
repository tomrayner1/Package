package uk.rayware.nitrolib.lunarclient.net.packets.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandlerClient;
import uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces.LCNetHandler;
import uk.rayware.nitrolib.lunarclient.net.util.ByteBufferWrapper;
import uk.rayware.nitrolib.lunarclient.net.util.LCPacket;

import java.io.IOException;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LCPacketWorldBorderCreateNew extends LCPacket {
	
	private String id;
	
	private String world;
	
	private boolean cancelsEntry, cancelsExit;
	
	private boolean canShrinkExpand;
	
	private int color = 0xFF3333FF;
	
	private double minX, minZ, maxX, maxZ;
	
	@Override
	public void write(ByteBufferWrapper b) throws IOException {
		b.buf().writeBoolean(id != null);
		if (id != null) b.writeString(id);
		b.writeString(world);
		b.buf().writeBoolean(cancelsEntry);
		b.buf().writeBoolean(cancelsExit);
		b.buf().writeBoolean(canShrinkExpand);
		b.buf().writeInt(color);
		b.buf().writeDouble(minX);
		b.buf().writeDouble(minZ);
		b.buf().writeDouble(maxX);
		b.buf().writeDouble(maxZ);
	}
	
	@Override
	public void read(ByteBufferWrapper b) throws IOException {
		if (b.buf().readBoolean()) {
			this.id = b.readString();
		}
		
		this.world = b.readString();
		this.cancelsEntry = b.buf().readBoolean();
		this.cancelsExit = b.buf().readBoolean();
		this.canShrinkExpand = b.buf().readBoolean();
		this.color = b.buf().readInt();
		this.minX = b.buf().readDouble();
		this.minZ = b.buf().readDouble();
		this.maxX = b.buf().readDouble();
		this.maxZ = b.buf().readDouble();
	}
	
	@Override
	public void process(LCNetHandler handler) {
		((LCNetHandlerClient) handler).handleWorldBorderCreateNew(this);
	}
}
