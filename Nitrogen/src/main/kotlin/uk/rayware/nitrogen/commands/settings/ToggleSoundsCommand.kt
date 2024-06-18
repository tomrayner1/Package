package uk.rayware.nitrogen.commands.settings

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import uk.rayware.nitrogen.NitrogenAPI

class ToggleSoundsCommand : Command("togglemessagesounds") {

    init {
        aliases = listOf("togglesounds", "sounds", "tms")
    }

    override fun execute(sender: CommandSender?, commandLabel: String?, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            return true
        }

        val profile = NitrogenAPI.getProfile(sender.uniqueId) ?: return true

        profile.messageSounds = !profile.messageSounds

        sender.sendMessage(
            if (profile.messageSounds) "${ChatColor.GREEN}You will now be notified of private messages."
            else "${ChatColor.RED}You will no longer be notified of private messages."
        )
        return true
    }

    override fun tabComplete(sender: CommandSender?, alias: String?, args: Array<out String>?): MutableList<String>? {
        return null
    }
}