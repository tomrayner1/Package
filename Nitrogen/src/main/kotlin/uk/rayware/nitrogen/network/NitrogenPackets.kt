package uk.rayware.nitrogen.network

import uk.rayware.nitrogen.pyrite.packet.Packet

class NitrogenPackets

class ServerOfflinePacket(val serverName: String) : Packet()

class RequestPacket(val target: String, val serverName: String, val request: String) : Packet()
class ReportPacket(val target: String, val reportedBy: String, val targetCount: Int, val serverName: String, val reason: String?) : Packet()

class StaffJoinPacket(val target: String, val serverName: String) : Packet()
class StaffQuitPacket(val target: String, val serverName: String) : Packet()
class StaffSwapPacket(val target: String, val toServer: String, val fromServer: String) : Packet()

class StaffChatPacket(val target: String, val serverName: String, val message: String) : Packet()

class ChatMutePacket(val sender: String?, val serverName: String, val muted: Boolean) : Packet()
class ChatSlowPacket(val sender: String?, val serverName: String, val amount: Int) : Packet()

class SendPlayerPacket(val target: String, val serverName: String) : Packet()

class AlertPacket(val message: String) : Packet()