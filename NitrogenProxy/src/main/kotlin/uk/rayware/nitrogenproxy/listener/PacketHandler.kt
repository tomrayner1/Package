package uk.rayware.nitrogenproxy.listener

import uk.rayware.nitrogen.network.SendPlayerPacket
import uk.rayware.nitrogen.network.ServerOfflinePacket
import uk.rayware.nitrogenproxy.NitrogenProxy
import uk.rayware.nitroproxy.pyrite.packet.PacketContainer
import uk.rayware.nitroproxy.pyrite.packet.PacketListener
import kotlin.jvm.optionals.getOrNull

class PacketHandler(private val nitrogenProxy: NitrogenProxy) : PacketContainer {

    @PacketListener
    fun onServerOfflinePacket(packet: ServerOfflinePacket) {
        if (nitrogenProxy.lobbies.containsKey(packet.serverName)) {
            nitrogenProxy.lobbies.remove(packet.serverName)
        }
    }

    @PacketListener
    fun onSendPlayerPacket(packet: SendPlayerPacket) {
        val player = nitrogenProxy.server.getPlayer(packet.target).getOrNull() ?: return
        val server = nitrogenProxy.server.getServer(packet.serverName).getOrNull() ?: return

        player.createConnectionRequest(server).fireAndForget()
    }

}