package uk.rayware.nitrogen.commands.other

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import uk.rayware.nitrolib.NitroLib

class HelpCommand : Command("help") {

    override fun execute(sender: CommandSender?, commandLabel: String?, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            return true
        }
        sender.sendMessage(" ")
        sender.sendMessage("${ChatColor.GOLD}${ChatColor.BOLD}General Help")
        sender.sendMessage(" ")
        sender.sendMessage("${ChatColor.YELLOW}Request help from a staff member by using")
        sender.sendMessage("${ChatColor.GOLD}${NitroLib.bar} ${ChatColor.WHITE}/helpop <reason ...>")
        sender.sendMessage("${ChatColor.YELLOW}Report other players by using")
        sender.sendMessage("${ChatColor.GOLD}${NitroLib.bar} ${ChatColor.WHITE}/report <player> [reason ...]")
        sender.sendMessage(" ")
        sender.sendMessage("${ChatColor.GOLD}Remember to stick to the rules and have fun while playing.")
        sender.sendMessage(" ")
        return true
    }

    override fun tabComplete(sender: CommandSender?, alias: String?, args: Array<out String>?): MutableList<String>? {
        return null
    }

}