package uk.rayware.nitrogen.commands.settings

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import uk.rayware.nitrogen.Nitrogen
import uk.rayware.nitrogen.NitrogenAPI

class ToggleTipsCommand : Command("toggletips") {

    init {
        aliases = listOf("togglespam", "tt", "hidetips")
    }

    override fun execute(sender: CommandSender?, commandLabel: String?, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            return true
        }

        val profile = NitrogenAPI.getProfile(sender.uniqueId) ?: return true

        profile.tipsEnabled = !profile.tipsEnabled

        sender.sendMessage(
            if (profile.tipsEnabled) "${ChatColor.GREEN}You will now receive tips."
            else "${ChatColor.RED}You will no longer receive tips."
        )

        if (profile.tipsEnabled) {
            sender.player.removeMetadata("hidetips", Nitrogen.nitrogen)
        }  else {
            sender.player.setMetadata("hidetips", FixedMetadataValue(Nitrogen.nitrogen, true))
        }
        return true
    }

    override fun tabComplete(sender: CommandSender?, alias: String?, args: Array<out String>?): MutableList<String>? {
        return null
    }

}