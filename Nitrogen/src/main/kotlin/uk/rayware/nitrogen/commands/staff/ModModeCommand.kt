package uk.rayware.nitrogen.commands.staff

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import uk.rayware.nitrogen.Nitrogen
import uk.rayware.nitrogen.NitrogenAPI
import uk.rayware.nitrogen.staff.StaffHandler
import uk.rayware.nitrolib.NitroLib

class ModModeCommand(val nitrogen: Nitrogen) : Command("modmode") {

    init {
        aliases = listOf("adminonduty", "aod", "h", "mod", "v", "vanish")
        permission = "nitrogen.command.modmode"
    }

    override fun execute(sender: CommandSender?, commandLabel: String?, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            return true
        }

        if (!sender.hasPermission("nitrogen.command.modmode")) {
            sender.sendMessage(NitroLib.permissionMessage)
            return true
        }

        val player: Player = sender

        if (player.hasMetadata("modmode")) {
            player.removeMetadata("modmode", nitrogen)
            StaffHandler.disableModMode(NitrogenAPI.getProfile(player.uniqueId))
        } else {
            player.setMetadata("modmode", FixedMetadataValue(nitrogen, true))
            StaffHandler.enableModMode(NitrogenAPI.getProfile(player.uniqueId))
        }

        sender.sendMessage(ChatColor.GOLD.toString() + "Mod Mode: " +
                    if (player.hasMetadata("modmode")) ChatColor.GREEN.toString() + "Enabled" else ChatColor.RED.toString() + "Disabled")

        return true
    }

    override fun tabComplete(sender: CommandSender?, alias: String?, args: Array<out String>?): MutableList<String>? {
        return null
    }

}