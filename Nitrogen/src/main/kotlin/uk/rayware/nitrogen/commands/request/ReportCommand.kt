package uk.rayware.nitrogen.commands.request

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import uk.rayware.nitrogen.NitrogenAPI
import uk.rayware.nitrogen.network.ReportPacket

class ReportCommand : Command("report") {

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage("${ChatColor.YELLOW}Usage:${ChatColor.GREEN} /report${ChatColor.WHITE} <player> [reason ...]")
            return true
        }

        if (NitrogenAPI.getProfile(sender.uniqueId)!!.requestCooldown > System.currentTimeMillis()) {
            sender.sendMessage("${ChatColor.RED}You can only make a request or report every minute.")
            return true
        }

        val target = Bukkit.getPlayer(args[0])

        if (target == null) {
            sender.sendMessage("${ChatColor.RED}Could not find the player '${ChatColor.YELLOW}${args[0]}${ChatColor.RED}'.")
            return true
        }

        if (target == sender) {
            sender.sendMessage("${ChatColor.RED}You cannot report yourself.")
            return true
        }

        val targetProfile = NitrogenAPI.getProfile(target.uniqueId)

        var reason: String? = null

        if (args.size > 1) {
            val sb = StringBuilder()
            val req: List<String> = args.asList().subList(1, args.size)

            for (string in req) {
                sb.append("$string ")
            }

            reason = sb.toString().trim()
        }

        targetProfile!!.reportCount++

        NitrogenAPI.getProfile(sender.uniqueId)!!.requestCooldown = System.currentTimeMillis() + (60 * 1000)

        sender.sendMessage("${ChatColor.GREEN}We have received your request.")

        NitrogenAPI.pyrite.sendPacket(ReportPacket(target.displayName, sender.displayName, targetProfile.reportCount, NitrogenAPI.nitrogenServer.identifier, reason), "Nitrogen")
        return true
    }



}