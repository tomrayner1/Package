package uk.rayware.nitrogen.menus

import org.bukkit.ChatColor
import org.bukkit.Material
import uk.rayware.nitrolib.menu.Menu
import uk.rayware.nitrolib.menu.components.ItemComponent
import uk.rayware.nitrolib.util.ItemBuilder

class GoldShopMenu : Menu("${ChatColor.GOLD}${ChatColor.BOLD}Gold Shop", 5) {

    private val pane1 = listOf(0, 2, 6, 8, 10, 16, 18, 26, 28, 34, 36, 38, 42, 44)
    private val pane2 = listOf(1, 7, 9, 17, 19, 25, 27, 35, 37, 43)

    init {
        pane1.forEach { pos ->
            addMenuComponent(ItemComponent(this, pos, ItemBuilder(Material.STAINED_GLASS_PANE).name(" ").durability(4).build()))
        }

        pane2.forEach { pos ->
            addMenuComponent(ItemComponent(this, pos, ItemBuilder(Material.STAINED_GLASS_PANE).name(" ").durability(1).build()))
        }

        fill(null)
    }

}