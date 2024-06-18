package uk.rayware.nitrogen.menus

import org.bukkit.ChatColor
import org.bukkit.Material
import uk.rayware.nitrogen.profile.Profile
import uk.rayware.nitrolib.NitroLib
import uk.rayware.nitrolib.menu.Menu
import uk.rayware.nitrolib.menu.components.ButtonComponent
import uk.rayware.nitrolib.menu.components.ItemComponent
import uk.rayware.nitrolib.util.ItemBuilder

class SettingsMenu(private val profile: Profile) : Menu("${ChatColor.GOLD}${ChatColor.BOLD}Settings", 3) {

    init {
        for (i in 0..9) {
            addMenuComponent(ItemComponent(this, i, ItemBuilder(Material.STAINED_GLASS_PANE).name(" ").durability(15).build()))
        }

        for (i in 17..26) {
            addMenuComponent(ItemComponent(this, i, ItemBuilder(Material.STAINED_GLASS_PANE).name(" ").durability(15).build()))
        }

        val togglePM = if (profile.acceptingMessages)
            ItemBuilder(Material.PAPER).name("${ChatColor.GOLD}${ChatColor.BOLD}Toggle private messages").lore(listOf(
                "${ChatColor.GRAY}Toggle if people can message you",
                "${ChatColor.GRAY}by using /message.",
                "",
                "${ChatColor.GREEN}▶ Enabled",
                "${ChatColor.GRAY}  Disabled",
                "",
                "${ChatColor.YELLOW}Left click to run /togglepm"
            )).build()
        else
            ItemBuilder(Material.PAPER).name("${ChatColor.GOLD}${ChatColor.BOLD}Toggle private messages").lore(listOf(
                "${ChatColor.GRAY}Toggle if people can message you",
                "${ChatColor.GRAY}by using /message.",
                "",
                "${ChatColor.GRAY}  Enabled",
                "${ChatColor.RED}▶ Disabled",
                "",
                "${ChatColor.YELLOW}Left click to run /togglepm"
            )).build()

        val toggleSounds = if (profile.messageSounds)
            ItemBuilder(Material.NOTE_BLOCK).name("${ChatColor.GOLD}${ChatColor.BOLD}Toggle messaging sounds").lore(listOf(
                "${ChatColor.GRAY}Toggle if you are alerted when",
                "${ChatColor.GRAY}someone messages you by playing",
                "${ChatColor.GRAY}a sound.",
                "",
                "${ChatColor.GREEN}▶ Enabled",
                "${ChatColor.GRAY}  Disabled",
                "",
                "${ChatColor.YELLOW}Left click to run /sounds"
            )).build()
        else
            ItemBuilder(Material.NOTE_BLOCK).name("${ChatColor.GOLD}${ChatColor.BOLD}Toggle messaging sounds").lore(listOf(
                "${ChatColor.GRAY}Toggle if you are alerted when",
                "${ChatColor.GRAY}someone messages you by playing",
                "${ChatColor.GRAY}a sound.",
                "",
                "${ChatColor.GRAY}  Enabled",
                "${ChatColor.RED}▶ Disabled",
                "",
                "${ChatColor.YELLOW}Left click to run /sounds"
            )).build()

        val toggleTips = if (profile.tipsEnabled)
            ItemBuilder(Material.PAPER).name("${ChatColor.GOLD}${ChatColor.BOLD}Toggle tips").lore(listOf(
                "${ChatColor.GRAY}Toggle if you are receive",
                "${ChatColor.GRAY}tips in chat.",
                "",
                "${ChatColor.GREEN}▶ Enabled",
                "${ChatColor.GRAY}  Disabled",
                "",
                "${ChatColor.YELLOW}Left click to run /toggletips"
            )).build()
        else
            ItemBuilder(Material.PAPER).name("${ChatColor.GOLD}${ChatColor.BOLD}Toggle tips").lore(listOf(
                "${ChatColor.GRAY}Toggle if you are receive",
                "${ChatColor.GRAY}tips in chat.",
                "",
                "${ChatColor.GRAY}  Enabled",
                "${ChatColor.RED}▶ Disabled",
                "",
                "${ChatColor.YELLOW}Left click to run /toggletips"
            )).build()

        addMenuComponents(
            ButtonComponent(this, 10, togglePM) { player, _ ->
                player.chat("/tpm")
                NitroLib.getInstance().menuHandler.openMenuForPlayer(player, SettingsMenu(profile))
                return@ButtonComponent true
            },

            ButtonComponent(this, 11, toggleSounds) { player, _ ->
                player.chat("/sounds")
                NitroLib.getInstance().menuHandler.openMenuForPlayer(player, SettingsMenu(profile))
                return@ButtonComponent true
            },

            ButtonComponent(this, 12, toggleTips) { player, _ ->
                player.chat("/toggletips")
                NitroLib.getInstance().menuHandler.openMenuForPlayer(player, SettingsMenu(profile))
                return@ButtonComponent true
            }
        )
    }

}