package uk.rayware.nitrogen.commands.chat

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import uk.rayware.nitrogen.Nitrogen
import uk.rayware.nitrogen.NitrogenAPI
import uk.rayware.nitrogen.network.ChatMutePacket
import uk.rayware.nitrogen.network.ChatSlowPacket
import uk.rayware.nitrolib.NitroLib

class SlowChatCommand(val nitrogen: Nitrogen) : Command("slowchat") {

    init {
        permission = "nitrogen.command.slowchat"
    }

    override fun execute(sender: CommandSender?, commandLabel: String?, args: Array<out String>?): Boolean {
        if (!sender!!.hasPermission("nitrogen.command.slowchat")) {
            sender.sendMessage(NitroLib.permissionMessage)
            return true
        }

        if (args!!.size != 1) {
            sender.sendMessage("${ChatColor.YELLOW}Usage:${ChatColor.GREEN} /slowchat${ChatColor.WHITE} <seconds>")
            sender.sendMessage("${ChatColor.GOLD}Chat is slowed by ${ChatColor.WHITE}${nitrogen.chatSettings.slow} second" +
                    "${if (nitrogen.chatSettings.slow != 1) "s" else ""}${ChatColor.GOLD}.")
            return true
        }

        val newSlow: Int

        try {
            newSlow = Integer.parseInt(args[0])
        } catch (e: NumberFormatException) {
            sender.sendMessage("${ChatColor.RED}Could not parse '${ChatColor.YELLOW}${args[0]}${ChatColor.RED}'.")
            return true
        }

        if (nitrogen.chatSettings.slow == newSlow) {
            sender.sendMessage("${ChatColor.RED}Chat is already slowed by this amount.")
            return true
        }

        if (newSlow < 0) {
            sender.sendMessage("${ChatColor.RED}Invalid amount.")
            return true
        }

        nitrogen.chatSettings.slow = newSlow
        Bukkit.broadcastMessage("${ChatColor.GOLD}Global chat is now slowed by ${ChatColor.WHITE}$newSlow second" +
                "${if (newSlow != 1) "s" else ""}${ChatColor.GOLD}.")

        nitrogen.config["config.chat.slow"] = newSlow
        nitrogen.saveConfig()

        var target: String? = null

        if (sender is Player) {
            target = sender.displayName
        }

        NitrogenAPI.pyrite.sendPacket(ChatSlowPacket(target, NitrogenAPI.nitrogenServer.identifier, nitrogen.chatSettings.slow), "Nitrogen")

        return true
    }

    override fun tabComplete(sender: CommandSender?, alias: String?, args: Array<out String>?): MutableList<String>? {
        return null
    }
}