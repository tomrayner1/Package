package uk.rayware.gboqueue.commands

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import uk.rayware.gboqueue.QueuePlugin
import uk.rayware.gboqueue.util.QueueResult
import uk.rayware.nitrogen.NitrogenAPI

class JoinQueueCommand : Command("joinqueue") {

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            return true
        }

        if (args.size != 1) {
            sender.sendMessage("${ChatColor.YELLOW}Usage:${ChatColor.GREEN} /joinqueue${ChatColor.WHITE} <queue>")
            return true
        }

        val data = NitrogenAPI.getStoredServerData(args[0])

        if (NitrogenAPI.nitrogenServer.identifier == data["name"]) {
            sender.sendMessage("${ChatColor.RED}You are already connected to this server.")
            return true
        }

        when (QueuePlugin.joinQueue(sender, args[0])) {
            QueueResult.SUCCESS ->
                sender.sendMessage("${ChatColor.GREEN}You have joined the queue for ${ChatColor.BOLD}${data["display"]}${ChatColor.GREEN}.")
            QueueResult.NOT_FOUND ->
                sender.sendMessage("${ChatColor.RED}Could not find a queue with name \"${args[0]}\".")
            QueueResult.ALREADY_IN ->
                sender.sendMessage("${ChatColor.RED}You are already in the queue for ${ChatColor.BOLD}${data["display"]}${ChatColor.RED}.")
            QueueResult.ERROR ->
                sender.sendMessage("${ChatColor.RED}You cannot queue for \"${args[0]}\".")
        }

        return true
    }
}