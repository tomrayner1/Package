package uk.rayware.nitrolib.lunarclient.net.LCNetHandlerInterfaces;

import uk.rayware.nitrolib.lunarclient.net.packets.client.LCPacketClientVoice;
import uk.rayware.nitrolib.lunarclient.net.packets.client.LCPacketVoiceChannelSwitch;
import uk.rayware.nitrolib.lunarclient.net.packets.client.LCPacketVoiceMute;
import uk.rayware.nitrolib.lunarclient.net.packets.server.LCPacketStaffModStatus;

public interface LCNetHandlerServer extends LCNetHandler {
	void handleStaffModStatus(LCPacketStaffModStatus packet);
	void handleVoice(LCPacketClientVoice packet);
	void handleVoiceMute(LCPacketVoiceMute packet);
	void handleVoiceChannelSwitch(LCPacketVoiceChannelSwitch packet);
}
