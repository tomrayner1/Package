package uk.rayware.nitrogen.chat

import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import uk.rayware.nitrogen.Nitrogen
import uk.rayware.nitrogen.NitrogenAPI

class ChatListener(val nitrogen: Nitrogen) : Listener {

    @EventHandler
    fun chat(event: AsyncPlayerChatEvent) {
        val player = event.player
        val profile = NitrogenAPI.getProfile(player.uniqueId)

        // TODO: check for channel

        if (!profile!!.isStaff()) {
            if (nitrogen.chatSettings.muted) {
                player.sendMessage("${ChatColor.RED}Global chat is currently muted.")
                event.isCancelled = true
                return
            }

            if (profile.lastChat + (nitrogen.chatSettings.slow * 1000) > System.currentTimeMillis()) {
                player.sendMessage("${ChatColor.RED}Global chat is currently slowed, you can type again in " +
                        "${((profile.lastChat + (nitrogen.chatSettings.slow * 1000) - System.currentTimeMillis()) / 100) / 10.0}s.")
                event.isCancelled = true
                return
            }
        }

        // TODO: check if muted

        val message = event.message

        // TODO: filter

        //val playerGroup = nitrogen.chat!!.getPrimaryGroup(player)

        val playerPrefix = nitrogen.chat!!.getPlayerPrefix(player)
        val playerSuffix = nitrogen.chat!!.getPlayerSuffix(player)

        val format: String = ChatColor.translateAlternateColorCodes('&',
            "${playerPrefix}&r%s&r${playerSuffix}&r&f:") + " %s"

        event.format = format
        profile.lastChat = System.currentTimeMillis()
    }

}