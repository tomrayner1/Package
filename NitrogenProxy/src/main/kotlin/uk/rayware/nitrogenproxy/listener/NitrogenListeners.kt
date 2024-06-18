package uk.rayware.nitrogenproxy.listener

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.player.KickedFromServerEvent
import com.velocitypowered.api.event.player.ServerConnectedEvent
import net.kyori.adventure.text.TextComponent
import uk.rayware.nitrogen.network.StaffJoinPacket
import uk.rayware.nitrogen.network.StaffQuitPacket
import uk.rayware.nitrogen.network.StaffSwapPacket
import uk.rayware.nitrogenproxy.NitrogenProxy
import uk.rayware.nitrogenproxy.util.ChatColour
import kotlin.jvm.optionals.getOrNull

class NitrogenListeners(private val proxy: NitrogenProxy) {

    @Subscribe
    fun onPlayerConnection(event: ServerConnectedEvent) {
        if (!event.player.hasPermission("nitrogen.staff")) {
            return
        }

        var colour: String

        proxy.jedisPool.resource.use { jedis ->
            colour = jedis.hget("nitrogen:profiles:colour", event.player.uniqueId.toString())
        }

        val name = ChatColour.translate(colour) + event.player.username

        if (event.previousServer.getOrNull() == null) {
            // Join
            proxy.logger.info("[Staff] [connection] (${name}${ChatColour.translate("&r")}) join ${event.server.serverInfo.name}")
            proxy.pyrite.sendPacket(StaffJoinPacket(name, event.server.serverInfo.name), "Nitrogen")
        } else {
            // Swap
            proxy.logger.info("[Staff] [connection] (${name}${ChatColour.translate("&r")}) join ${event.server.serverInfo.name} from ${event.previousServer.get().serverInfo.name}")
            proxy.pyrite.sendPacket(StaffSwapPacket(name, event.server.serverInfo.name, event.previousServer.get().serverInfo.name), "Nitrogen")
         }
    }

    @Subscribe
    fun onPlayerLeave(event: DisconnectEvent) {
        if (event.player.currentServer.getOrNull() == null) {
            return
        }

        if (!event.player.hasPermission("nitrogen.staff")) {
            return
        }

        var colour: String

        proxy.jedisPool.resource.use { jedis ->
            colour = jedis.hget("nitrogen:profiles:colour", event.player.uniqueId.toString())
        }

        val name = ChatColour.translate(colour) + event.player.username

        proxy.logger.info("[Staff] [connection] (${name}${ChatColour.translate("&r")}) leave ${event.player.currentServer.get().serverInfo.name}")
        proxy.pyrite.sendPacket(StaffQuitPacket(name, event.player.currentServer.get().serverInfo.name), "Nitrogen")
    }

    @Subscribe
    fun onKickedFromServer(event: KickedFromServerEvent) {
        if (event.result !is KickedFromServerEvent.DisconnectPlayer) {
            return
        }

        if (!event.player.hasPermission("nitrogen.staff")) {
            return
        }

        //if (event.kickedDuringServerConnect())
        //{
        //    return
        //}

        // == Prevent outdated clients from spamming the chat!
        val reason = event.serverKickReason.getOrNull()

        if (reason != null)
        {
            if ((reason as TextComponent).content().contains("Outdated"))
            {
                return
            }
        }
        // ==

        var colour: String

        proxy.jedisPool.resource.use { jedis ->
            colour = jedis.hget("nitrogen:profiles:colour", event.player.uniqueId.toString())
        }

        val name = ChatColour.translate(colour) + event.player.username

        proxy.logger.info("[Staff] [connection] (${name}${ChatColour.translate("&r")}) leave ${event.server.serverInfo.name}")
        proxy.pyrite.sendPacket(StaffQuitPacket(name, event.server.serverInfo.name), "Nitrogen")
    }

}