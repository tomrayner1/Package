package uk.rayware.nitrogen.menus

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import uk.rayware.nitrogen.staff.StaffHandler
import uk.rayware.nitrolib.menu.Menu
import uk.rayware.nitrolib.menu.components.ButtonComponent
import uk.rayware.nitrolib.util.ItemBuilder

class OnlineStaffMenu : Menu("${ChatColor.AQUA}${ChatColor.BOLD}Online Staff", 3) {

    init {
        var count = -1

        Bukkit.getOnlinePlayers().forEach { player ->
            if (player.hasPermission("nitrogen.staff")) {
                addMenuComponent(ButtonComponent(this, ++count, ItemBuilder(Material.SKULL_ITEM).skull(player.name).name(player.displayName).lore(
                    listOf(
                        "",
                        "${ChatColor.GOLD}Mod Mode: ${if (player.hasMetadata("modmode")) "${ChatColor.GREEN}Yes" else "${ChatColor.RED}No"}",
                        "${ChatColor.GOLD}Vanished: ${if (StaffHandler.vanishedPlayers.contains(player)) "${ChatColor.GREEN}Yes" else "${ChatColor.RED}No"}",
                        "",
                        "${ChatColor.YELLOW}Click to teleport."
                    )
                ).build()) { pl, _ ->
                    pl.chat("/tp ${player.name}")
                    return@ButtonComponent true
                })
            }
        }
    }

}