/*package uk.rayware.nitrogen.lampcommands

import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.DefaultFor
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.BukkitCommandActor
import revxrsal.commands.bukkit.annotation.CommandPermission
import revxrsal.commands.exception.CommandErrorException
import uk.rayware.nitrogen.NitrogenAPI
import uk.rayware.nitrogen.menus.GoldShopMenu
import uk.rayware.nitrolib.NitroLib

@Command("gold")
class GoldCommand {

    @DefaultFor("gold")
    @Subcommand("help")
    fun help(actor: BukkitCommandActor) {
        actor.reply("&e === &a/gold help&e === ")
        actor.reply("&agold amount &e- Check how much gold you currently have.")
        if (actor.sender.hasPermission("nitrogen.command.gold.check")) {
            actor.reply("&agold check <target> &e- Check how much gold another player has.")
        }
        actor.reply("&agold help")
        actor.reply("&agold shop &e- Spend gold on cosmetics and more.")
    }

    @Subcommand("amount")
    fun amount(sender: Player) {
        val profile = NitrogenAPI.getProfile(sender.uniqueId)

        if (profile == null) {
            throw CommandErrorException("Your profile is not yet loaded.")
        } else {
            sender.sendMessage("${ChatColor.GOLD}You have ${ChatColor.YELLOW}${profile.gold}${ChatColor.GOLD}⛁ gold.")
        }
    }

    @Subcommand("check")
    @CommandPermission("nitrogen.command.gold.check")
    fun check(sender: BukkitCommandActor, target: OfflinePlayer) {
        val profile = NitrogenAPI.getProfile(target.uniqueId)

        if (profile == null) {
            throw CommandErrorException("No entry found for player '${target.name}'.")
        } else {
            sender.reply("${target.name}${ChatColor.GOLD} has ${ChatColor.YELLOW}${profile.gold}${ChatColor.GOLD}⛁ gold.")
        }
    }

    @Command("gold shop", "goldshop")
    fun shop(sender: Player) {
        NitroLib.getInstance().menuHandler.openMenuForPlayer(sender, GoldShopMenu())
    }

}*/