package uk.rayware.nitrogen.commands.settings

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import uk.rayware.nitrogen.NitrogenAPI
import uk.rayware.nitrogen.menus.SettingsMenu
import uk.rayware.nitrolib.NitroLib

class SettingsCommand : Command("settings") {

    init {
        aliases = listOf("options", "prefs")
    }

    override fun execute(sender: CommandSender?, commandLabel: String?, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            return true
        }

        NitroLib.getInstance().menuHandler.openMenuForPlayer(sender,
            NitrogenAPI.getProfile(sender.uniqueId)?.let { SettingsMenu(it) })
        return true
    }

    override fun tabComplete(sender: CommandSender?, alias: String?, args: Array<out String>?): MutableList<String>? {
        return null
    }
}