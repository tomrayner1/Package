package uk.rayware.nitrogen.commands.server

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import uk.rayware.nitrogen.Nitrogen
import uk.rayware.nitrogen.NitrogenAPI
import uk.rayware.nitrolib.NitroLib

class EnvironmentDumpCommand(val nitrogen: Nitrogen) : Command("environmentdump") {

    init {
        aliases = listOf("envdump")
        permission = "nitrogen.command.environmentdump"
    }

    override fun execute(sender: CommandSender?, commandLabel: String?, args: Array<out String>?): Boolean {
        if (!sender!!.hasPermission("nitrogen.command.environmentdump")) {
            sender.sendMessage(NitroLib.permissionMessage)
            return true
        }

        val serverData: Map<String, String> = when (args!!.size) {
            0 -> NitrogenAPI.getNitrogenServerData(NitrogenAPI.nitrogenServer.identifier)
            1 -> NitrogenAPI.getNitrogenServerData(args[0])
            else -> {
                sender.sendMessage("${ChatColor.YELLOW}Usage:${ChatColor.GREEN} /environmentdump${ChatColor.WHITE} [server]")
                return true
            }
        }

        if (serverData.isEmpty() || serverData["display"] == null) {
            sender.sendMessage("${ChatColor.RED}Could not find specified server.")
            return true
        }

        sender.sendMessage("${ChatColor.LIGHT_PURPLE}${ChatColor.BOLD}Nitrogen environment dump")
        sender.sendMessage("${ChatColor.DARK_PURPLE}Server name:${ChatColor.WHITE} ${serverData["name"]} (${serverData["display"]})")
        sender.sendMessage("${ChatColor.DARK_PURPLE}Server group:${ChatColor.WHITE} ${serverData["group"]}")
        sender.sendMessage("${ChatColor.DARK_PURPLE}Platform:${ChatColor.WHITE} ${serverData["platform"]}")
        sender.sendMessage("${ChatColor.DARK_PURPLE}Online:${ChatColor.WHITE} ${serverData["online"]}")

        val last: Double = ((System.currentTimeMillis() - serverData["updated"]!!.toLong()) / 100) / 10.0
        sender.sendMessage("${ChatColor.DARK_PURPLE}Last updated:${ChatColor.WHITE} ${last}s ago")

        sender.sendMessage(" ")
        if (serverData["platform"] == "bukkit") {
            sender.sendMessage("${ChatColor.DARK_PURPLE}Players:${ChatColor.WHITE} ${serverData["players"]}/${serverData["max"]}")
            sender.sendMessage("${ChatColor.DARK_PURPLE}Whitelisted:${ChatColor.WHITE} ${serverData["whitelisted"]}")
        }


        return true
    }

    override fun tabComplete(sender: CommandSender?, alias: String?, args: Array<out String>?): MutableList<String>? {
        if (args!!.size == 1) {
            return NitrogenAPI.getAllNitrogenServerIdentifiers().toMutableList()
        }

        return null
    }
}