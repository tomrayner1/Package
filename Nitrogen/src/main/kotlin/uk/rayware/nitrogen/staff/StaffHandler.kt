package uk.rayware.nitrogen.staff

import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import uk.rayware.nitrogen.Nitrogen
import uk.rayware.nitrogen.profile.Profile
import uk.rayware.nitrolib.NitroLib
import uk.rayware.nitrolib.util.ItemBuilder

class StaffHandler {

    companion object {
        @JvmStatic
        val vanishedPlayers: HashSet<Player> = HashSet()

        @JvmStatic
        fun enableModMode(target: Profile?) {
            if (target == null || !target.loaded) {
                return
            }

            val player = target.getPlayer() ?: return

            target.modInventory = player.inventory.contents
            target.modArmour = player.inventory.armorContents

            player.inventory.clear()
            player.inventory.helmet = ItemStack(Material.AIR)
            player.inventory.chestplate = ItemStack(Material.AIR)
            player.inventory.leggings = ItemStack(Material.AIR)
            player.inventory.boots = ItemStack(Material.AIR)

            if (player.hasMetadata("restoreflight")) {
                player.removeMetadata("restoreflight", Nitrogen.nitrogen)
            }

            player.setMetadata("restoreflight", FixedMetadataValue(Nitrogen.nitrogen, player.isFlying))

            player.allowFlight = true

            player.inventory.setItem(0, ItemBuilder(Material.BOOK).name("&bInspection book").build())
            player.inventory.setItem(1, ItemBuilder(Material.EMERALD).name("&bRandom teleport").build())

            if (Nitrogen.nitrogen.server.pluginManager.isPluginEnabled("WorldEdit")) {
                player.inventory.setItem(2, ItemBuilder(Material.COMPASS).name("&bTeleport").build())

                if (player.hasPermission("worldedit.wand")) {
                    player.inventory.setItem(4, ItemBuilder(Material.WOOD_AXE).name("&bWorldedit wand").build())
                }
            }

            player.inventory.setItem(6, ItemBuilder(Material.CARPET).name("&b").build())

            if (vanishedPlayers.contains(player)) {
                player.inventory.setItem(7, ItemBuilder(Material.INK_SACK).durability(8).name("&bBecome visible").build())
            } else {
                player.inventory.setItem(7, ItemBuilder(Material.INK_SACK).durability(10).name("&bBecome invisible").build())
            }

            player.inventory.setItem(8, ItemBuilder(Material.SKULL_ITEM).skull(player.name).name("&bOnline staff").build())

            target.previousGamemode = player.gameMode

            if (player.hasPermission("nitrogen.command.gamemode")) {
                player.gameMode = GameMode.CREATIVE
            }

            NitroLib.getInstance().lunarClientAPI.giveAllStaffModules(player)
        }

        @JvmStatic
        fun disableModMode(target: Profile?) {
            if (target == null || !target.loaded) {
                return
            }

            val player = target.getPlayer() ?: return

            player.inventory.clear()
            player.inventory.helmet = ItemStack(Material.AIR)
            player.inventory.chestplate = ItemStack(Material.AIR)
            player.inventory.leggings = ItemStack(Material.AIR)
            player.inventory.boots = ItemStack(Material.AIR)

            player.inventory.contents = target.modInventory
            target.modInventory = null
            player.inventory.armorContents = target.modArmour
            target.modArmour = null

            if (player.hasMetadata("restoreflight")) {
                player.removeMetadata("restoreflight", Nitrogen.nitrogen)
                player.allowFlight = true
            } else {
                player.allowFlight = false
            }

            if (player.hasMetadata("vanished")) {
                player.removeMetadata("vanished", Nitrogen.nitrogen)
            }

            player.gameMode = target.previousGamemode

            NitroLib.getInstance().lunarClientAPI.disableAllStaffModules(target.getPlayer())
        }
    }

}