package uk.rayware.nitrogen.network

import uk.rayware.nitroproxy.pyrite.packet.Packet

class NitrogenPackets

class ServerOfflinePacket(val serverName: String) : Packet()

class StaffJoinPacket(val target: String, val serverName: String) : Packet()
class StaffQuitPacket(val target: String, val serverName: String) : Packet()
class StaffSwapPacket(val target: String, val toServer: String, val fromServer: String) : Packet()

class SendPlayerPacket(val target: String, val serverName: String) : Packet()