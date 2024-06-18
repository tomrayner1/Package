package uk.rayware.gboqueue.commands

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import uk.rayware.nitrogen.NitrogenAPI
import uk.rayware.nitrolib.NitroLib

class AllowQueueCommand : Command("allowqueue") {

    init {
        permission = "gboqueue.command.allowqueue"
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("gboqueue.command.allowqueue")) {
            sender.sendMessage(NitroLib.permissionMessage)
            return true
        }

        if (args.size != 1 || !(args[0] == "on" || args[0] == "off")) {
            sender.sendMessage("${ChatColor.YELLOW}Usage:${ChatColor.GREEN} /allowqueue ${ChatColor.WHITE}<on/off>")
            sender.sendMessage("${ChatColor.GOLD}Queueing for this server is ${ChatColor.WHITE}" +
                    "${if (NitrogenAPI.nitrogenServer.getCustomData("allowgboqueue") == "true") "ON" else "OFF"}${ChatColor.GOLD}.")
            return true
        }

        val on = args[0] == "on"

        if (on) {
            NitrogenAPI.nitrogenServer.setCustomData("allowgboqueue", "true")
            sender.sendMessage("${ChatColor.GREEN}Queueing for this server is now allowed.")
        } else {
            NitrogenAPI.nitrogenServer.setCustomData("allowgboqueue", "false")
            sender.sendMessage("${ChatColor.RED}Queueing for this server is now forbidden.")
        }
        return true
    }

}
