package uk.rayware.nitrogen.commands.staff

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import uk.rayware.nitrogen.NitrogenAPI
import uk.rayware.nitrolib.NitroLib

class StaffChatCommand : Command("staffchat") {

    init {
        aliases = listOf("sc")
        permission = "nitrogen.command.staffchat"
    }

    override fun execute(sender: CommandSender?, commandLabel: String?, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            return true
        }

        if (!sender.hasPermission("nitrogen.command.staffchat")) {
            sender.sendMessage(NitroLib.permissionMessage)
            return true
        }

        val profile = NitrogenAPI.getProfile(sender.uniqueId)

        profile!!.staffChat = !profile.staffChat

        if (profile.staffChat) {
            sender.sendMessage("${ChatColor.GREEN}You are now in the staff chat.")
        } else {
            sender.sendMessage("${ChatColor.RED}You are no longer in the staff chat.")
        }

        NitrogenAPI.saveProfile(profile)

        return true
    }

    override fun tabComplete(sender: CommandSender?, alias: String?, args: Array<out String>?): MutableList<String>? {
        return null
    }

}