package uk.rayware.nitrogen.commands.chat

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import uk.rayware.nitrogen.NitrogenAPI
import uk.rayware.nitrogen.network.AlertPacket
import uk.rayware.nitrolib.NitroLib

class AlertRawCommand: Command("alertraw") {

    init {
        permission = "nitrogen.command.alertraw"
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(NitroLib.permissionMessage)
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage("${ChatColor.YELLOW}Usage:${ChatColor.GREEN} /alertraw${ChatColor.WHITE} <message ...>")
            return true
        }

        var message = ""

        args.forEach { message = "$message$it " }

        message.trim()

        NitrogenAPI.pyrite.sendPacket(AlertPacket(ChatColor.translateAlternateColorCodes('&', message)), "Nitrogen")
        return true
    }

    override fun tabComplete(sender: CommandSender?, alias: String?, args: Array<out String>?): MutableList<String>? {
        return null
    }
}