package uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces;

import uk.rayware.nitrolib.lunarclient.net.packets.client.*;
import uk.rayware.nitrolib.lunarclient.net.packets.server.*;

public interface LCNetHandlerClient {
	// Boss Bar
	void handleBossBar(LCPacketBossBar packet);
	// Cooldown
	void handleCooldown(LCPacketCooldown packet);
	// Ghost
	void handleGhost(LCPacketGhost packet);
	// Hologram
	void handleAddHologram(LCPacketHologram packet);
	void handleRemoveHologram(LCPacketHologramRemove packet);
	void handleUpdateHologram(LCPacketHologramUpdate packet);
	// Nametags
	void handleOverrideNametags(LCPacketNametagsOverride packet);
	void handleNametagsUpdate(LCPacketNametagsUpdate packet);
	// Notification
	void handleNotification(LCPacketNotification packet);
	// Server
	void handleServerRule(LCPacketServerRule packet);
	void handleServerUpdate(LCPacketServerUpdate packet);
	// Mod
	void handleStaffModState(LCPacketStaffModState packet);
	// Teammates
	void handleTeammates(LCPacketTeammates packet);
	// Title
	void handleTitle(LCPacketTitle packet);
	// World
	void handleUpdateWorld(LCPacketUpdateWorld packet);
	void handleWorldBorder(LCPacketWorldBorder packet);
	void handleWorldBorderCreateNew(LCPacketWorldBorderCreateNew packet);
	void handleWorldBorderRemove(LCPacketWorldBorderRemove packet);
	void handleWorldBorderUpdate(LCPacketWorldBorderUpdate packet);
	void handleWorldBorderUpdateNew(LCPacketWorldBorderUpdateNew packet);
	// Voice
	void handleVoice(LCPacketVoice packet);
	void handleVoiceChannels(LCPacketVoiceChannel packet);
	void handleVoiceChannelUpdate(LCPacketVoiceChannelUpdate packet);
	void handleVoiceChannelDelete(LCPacketVoiceChannelRemove packet);
	// ModSettings
	void handleModSettings(LCPacketModSettings packetModSettings);
	
}
