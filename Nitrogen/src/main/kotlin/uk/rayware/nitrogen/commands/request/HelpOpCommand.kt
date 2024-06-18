package uk.rayware.nitrogen.commands.request

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import uk.rayware.nitrogen.Nitrogen
import uk.rayware.nitrogen.NitrogenAPI
import uk.rayware.nitrogen.network.RequestPacket
import uk.rayware.nitrogen.util.NitrogenConfig
import uk.rayware.nitrogen.util.StaffUtil

class HelpOpCommand(val nitrogen: Nitrogen) : Command("helpop") {

    init {
        this.aliases = listOf("request")
    }

    override fun execute(sender: CommandSender?, commandLabel: String?, args: Array<out String>?): Boolean {
        if (sender !is Player)
            return true

        if (args!!.isEmpty()) {
            sender.sendMessage("${ChatColor.YELLOW}Usage: ${ChatColor.GREEN}/helpop ${ChatColor.WHITE}<reason ...>")
            return true
        }

        val profile = NitrogenAPI.getProfile(sender.uniqueId)

        if (profile == null) {
            sender.sendMessage("${ChatColor.RED}An error has occurred with this command.")
            return true
        }

        if (profile.requestCooldown > System.currentTimeMillis()) {
            sender.sendMessage("${ChatColor.RED}You can only make a request or report every minute.")
            return true
        }

        val sb = StringBuilder()

        for (string in args) {
            sb.append("$string ")
        }

        val request = sb.toString().trim()

        profile.requestCooldown = System.currentTimeMillis() + (60 * 1000)

        NitrogenAPI.pyrite.sendPacket(RequestPacket(sender.displayName, NitrogenAPI.nitrogenServer.identifier, request), "Nitrogen")

        sender.sendMessage("${ChatColor.GREEN}We have received your request.")
        return true
    }

    override fun tabComplete(sender: CommandSender?, alias: String?, args: Array<out String>?): MutableList<String>? {
        return null
    }

}