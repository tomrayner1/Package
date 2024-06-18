package uk.rayware.nitrogen.commands.chat

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import uk.rayware.nitrogen.NitrogenAPI
import uk.rayware.nitrogen.network.AlertPacket
import uk.rayware.nitrolib.NitroLib

class AlertCommand : Command("alert") {

    init {
        permission = "nitrogen.command.alert"
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(NitroLib.permissionMessage)
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage("${ChatColor.YELLOW}Usage:${ChatColor.GREEN} /alert${ChatColor.WHITE} <message ...>")
            return true
        }

        var message = ""

        args.forEach { message = "$message$it " }

        message.trim()

        val template = "${ChatColor.DARK_GRAY}[${ChatColor.DARK_RED}" +
                "Alert${ChatColor.DARK_GRAY}]${ChatColor.RESET} "

        NitrogenAPI.pyrite.sendPacket(AlertPacket(template + ChatColor.translateAlternateColorCodes('&', message)), "Nitrogen")
        return true
    }

    override fun tabComplete(sender: CommandSender?, alias: String?, args: Array<out String>?): MutableList<String>? {
        return null
    }
}