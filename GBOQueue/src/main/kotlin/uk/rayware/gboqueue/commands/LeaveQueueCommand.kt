package uk.rayware.gboqueue.commands

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import uk.rayware.gboqueue.QueuePlugin
import uk.rayware.gboqueue.util.QueueResult
import uk.rayware.nitrogen.NitrogenAPI

class LeaveQueueCommand : Command("leavequeue") {

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            return true
        }

        if (args.size != 1) {
            sender.sendMessage("${ChatColor.YELLOW}Usage:${ChatColor.GREEN} /leavequeue${ChatColor.WHITE} <queue>")
            return true
        }

        val data = NitrogenAPI.getStoredServerData(args[0])

        if (data["display"] == null) {
            sender.sendMessage("${ChatColor.RED}Could not find a queue with name \"${args[0]}\".")
            return true
        }

        when (QueuePlugin.leaveQueue(sender, args[0])) {
            QueueResult.SUCCESS -> {
                sender.sendMessage("${ChatColor.GREEN}You have left the queue for ${ChatColor.BOLD}${data["display"]}${ChatColor.GREEN}.")

            }
            QueueResult.ERROR -> sender.sendMessage("${ChatColor.RED}You are not in the queue for ${ChatColor.BOLD}${data["display"]}${ChatColor.RED}.")
            else -> return true
        }

        return true
    }
}