package uk.rayware.nitrogen.staff

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockCanBuildEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import uk.rayware.nitrogen.NitrogenAPI
import uk.rayware.nitrogen.menus.OnlineStaffMenu
import uk.rayware.nitrogen.network.StaffChatPacket
import uk.rayware.nitrolib.NitroLib
import uk.rayware.nitrolib.util.ItemBuilder

class StaffListener : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onChat(event: AsyncPlayerChatEvent) {
        val profile = NitrogenAPI.getProfile(event.player.uniqueId)

        if (profile!!.staffChat) {
            if (!profile.isStaff()) {
                profile.staffChat = false
                event.player.sendMessage("${ChatColor.RED}You are no longer in the staff chat.")
                return
            }

            event.isCancelled = true
            NitrogenAPI.pyrite.sendPacket(StaffChatPacket(event.player.displayName, NitrogenAPI.nitrogenServer.identifier, event.message), "Nitrogen")
        }
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (event.item == null || !event.item.hasItemMeta()) {
            return
        }

        val player = event.player
        val item = event.item

        if (!player.hasMetadata("modmode")) {
            return
        }

        when (item.type) {
            Material.INK_SACK -> {
                if (item.durability == 10.toShort()) {
                    if (item.itemMeta.displayName != "${ChatColor.AQUA}Become invisible") {
                        return
                    }

                    StaffHandler.vanishedPlayers.add(player)
                    player.sendMessage("${ChatColor.GRAY}You are now invisible. (Showing Staff)")
                    player.inventory.setItem(player.inventory.heldItemSlot, ItemBuilder(Material.INK_SACK).durability(8).name("&bBecome visible").build())
                    Bukkit.getServer().onlinePlayers.stream().filter { p -> !NitrogenAPI.getProfile(p.uniqueId)!!.showingVanished }.forEach { p -> p.hidePlayer(player) }
                } else if (item.durability == 8.toShort()) {
                    if (item.itemMeta.displayName != "${ChatColor.AQUA}Become visible") {
                        return
                    }

                    StaffHandler.vanishedPlayers.remove(player)
                    player.sendMessage("${ChatColor.GREEN}You are now visible. (Showing Staff)")
                    player.inventory.setItem(player.inventory.heldItemSlot, ItemBuilder(Material.INK_SACK).durability(10).name("&bBecome invisible").build())
                    Bukkit.getServer().onlinePlayers.stream().forEach { p -> p.showPlayer(player) }
                }
            }
            Material.SKULL_ITEM -> {
                if (item.itemMeta.displayName == "${ChatColor.AQUA}Online staff") {
                    NitroLib.getInstance().menuHandler.openMenuForPlayer(player, OnlineStaffMenu())
                }
            }
            Material.EMERALD -> {
                if (item.itemMeta.displayName == "${ChatColor.AQUA}Random teleport") {
                    player.chat("/nitrogen:teleport ${Bukkit.getOnlinePlayers().random().name}")
                }
            }
            else -> return
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (event.itemInHand.itemMeta.displayName == ChatColor.AQUA.toString()) {
            event.isCancelled = true
            return
        }
    }

    @EventHandler
    fun onBlockCanPlace(event: BlockCanBuildEvent) {
        event.isBuildable = true
    }

    @EventHandler
    fun onDamage(event: EntityDamageByEntityEvent) {
        if (event.entity is Player) {
            if (event.entity.hasMetadata("modmode")) {
                event.isCancelled = true
            }
        } else if (event.damager is Player) {
            if (StaffHandler.vanishedPlayers.contains(event.damager)) {
                event.isCancelled = true
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onQuit(event: PlayerQuitEvent) {
        if (event.player.hasMetadata("modmode")) {
            StaffHandler.disableModMode(NitrogenAPI.getProfile(event.player.uniqueId))
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onJoin(event: PlayerJoinEvent) {
        if (event.player.hasMetadata("modmode")) {
            StaffHandler.enableModMode(NitrogenAPI.getProfile(event.player.uniqueId))
        }
    }

}