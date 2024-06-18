package uk.rayware.nitrogenproxy.balancer

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.KickedFromServerEvent
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent
import com.velocitypowered.api.proxy.server.RegisteredServer
import net.kyori.adventure.text.Component
import uk.rayware.nitrogenproxy.NitrogenProxy
import kotlin.jvm.optionals.getOrNull

class HubBalancer(private val nitrogenProxy: NitrogenProxy) {

    @Subscribe
    fun onJoin(event: PlayerChooseInitialServerEvent) {
        // Prevent lower than 1.8 and unknown version
        if (event.player.protocolVersion.protocol < 47)
        {
            event.player.disconnect(Component.text("Please use minecraft version 1.8 or higher!"))
            return
        }

        val server = balance()

        if (server != null) {
            event.setInitialServer(server)
        }
    }

    @Subscribe
    fun onKick(event: KickedFromServerEvent) {
        if (event.kickedDuringServerConnect())
            return

        // Check for outdated sv or cl
        if (event.result is KickedFromServerEvent.Notify)
        {
            val reason = (event.result as KickedFromServerEvent.Notify).messageComponent!!.toString()

            if (reason.contains("Outdated", ignoreCase = true))
            {
                return
            }
        }

        val server = balance()

        if (server != null) {
            event.result = KickedFromServerEvent.RedirectPlayer.create(server)
        }
    }

    private fun balance(): RegisteredServer? {
        return nitrogenProxy.lobbies.values.stream().min(Comparator.comparingInt { it.playersConnected.size }).getOrNull()
    }

}