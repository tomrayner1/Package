package uk.rayware.nitrogenproxy.commands

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.Component
import uk.rayware.nitrogenproxy.NitrogenProxy
import uk.rayware.nitrogenproxy.util.ChatColour
import kotlin.jvm.optionals.getOrNull

class HubCommand(private val nitrogenProxy: NitrogenProxy) : SimpleCommand {

    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()

        if (source !is Player) {
            return
        }

        if (nitrogenProxy.lobbies.containsValue(source.currentServer.get().server)) {
            source.sendMessage(Component.text(ChatColour.translate("&cYou are already connected to a hub.")))
        } else {
            source.sendMessage(Component.text(ChatColour.translate("&aConnecting you to a hub...")))
            source.createConnectionRequest(nitrogenProxy.lobbies.values.stream().min(Comparator.comparingInt { it.playersConnected.size }).getOrNull()).fireAndForget()
        }
    }

    override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
        return true
    }

    override fun suggest(invocation: SimpleCommand.Invocation?): MutableList<String> {
        return mutableListOf()
    }

}