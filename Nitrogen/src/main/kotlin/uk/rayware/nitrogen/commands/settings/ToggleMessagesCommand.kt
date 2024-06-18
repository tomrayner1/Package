package uk.rayware.nitrogen.commands.settings

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import uk.rayware.nitrogen.NitrogenAPI

class ToggleMessagesCommand : Command("togglemessages") {

    init {
        aliases = listOf("togglepms", "tpm", "toggleprivatemessages", "togglepm")
    }

    override fun execute(sender: CommandSender?, commandLabel: String?, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            return true
        }

        val profile = NitrogenAPI.getProfile(sender.uniqueId) ?: return true

        profile.acceptingMessages = !profile.acceptingMessages

        sender.sendMessage(
            if (profile.acceptingMessages) "${ChatColor.GREEN}You will now receive private messages."
            else "${ChatColor.RED}You will no longer receive private messages."
        )
        return true
    }

    override fun tabComplete(sender: CommandSender?, alias: String?, args: Array<out String>?): MutableList<String>? {
        return null
    }

}