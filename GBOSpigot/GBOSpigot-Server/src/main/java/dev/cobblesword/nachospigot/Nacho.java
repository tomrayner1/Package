package dev.cobblesword.nachospigot;

import java.util.Set;

import gbo.forever.spigot.GBOSpigot;
import gbo.forever.spigot.protocol.MovementListener;
import gbo.forever.spigot.protocol.PacketListener;

@Deprecated
public class Nacho {

	private static Nacho INSTANCE;

	public Nacho() {
		INSTANCE = this;
	}

	public static Nacho get() {
		return INSTANCE == null ? new Nacho() : INSTANCE;
	}

	public void registerCommands() {

	}

	public void registerPacketListener(PacketListener packetListener) {
		GBOSpigot.getInstance().registerPacketListener(packetListener);
	}

	public void unregisterPacketListener(PacketListener packetListener) {
		GBOSpigot.getInstance().unregisterPacketListener(packetListener);
	}

	public Set<PacketListener> getPacketListeners() {
		return GBOSpigot.getInstance().getPacketListeners();
	}

	public void registerMovementListener(MovementListener movementListener) {
		GBOSpigot.getInstance().registerMovementListener(movementListener);
	}

	public void unregisterMovementListener(MovementListener movementListener) {
		GBOSpigot.getInstance().unregisterMovementListener(movementListener);
	}

	public Set<MovementListener> getMovementListeners() {
		return GBOSpigot.getInstance().getMovementListeners();
	}

}
