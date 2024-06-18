package uk.rayware.nitrogen.commands.chat

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import uk.rayware.nitrogen.Nitrogen
import uk.rayware.nitrogen.NitrogenAPI
import uk.rayware.nitrogen.network.ChatMutePacket
import uk.rayware.nitrolib.NitroLib

class MuteChatCommand(val nitrogen: Nitrogen) : Command("mutechat") {

    init {
        permission = "nitrogen.command.mutechat"
    }

    override fun execute(sender: CommandSender?, commandLabel: String?, args: Array<out String>?): Boolean {
        if (!sender!!.hasPermission("nitrogen.command.mutechat")) {
            sender.sendMessage(NitroLib.permissionMessage)
            return true
        }

        nitrogen.chatSettings.muted = !nitrogen.chatSettings.muted

        Bukkit.broadcastMessage("${ChatColor.GOLD}Global chat is now " +
                "${if (nitrogen.chatSettings.muted) "${ChatColor.RED}muted" else "${ChatColor.GREEN}unmuted"}${ChatColor.GOLD}.")

        nitrogen.config["config.chat.muted"] = nitrogen.chatSettings.muted
        nitrogen.saveConfig()

        var target: String? = null

        if (sender is Player) {
            target = sender.displayName
        }

        NitrogenAPI.pyrite.sendPacket(ChatMutePacket(target, NitrogenAPI.nitrogenServer.identifier, nitrogen.chatSettings.muted), "Nitrogen")

        return true
    }

    override fun tabComplete(sender: CommandSender?, alias: String?, args: Array<out String>?): MutableList<String>? {
        return null
    }

}