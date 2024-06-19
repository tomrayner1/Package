package uk.rayware.arena.lobby;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class LobbyItemListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void interact(PlayerInteractEvent event) {
		if (event.getItem() == null || !LobbyItems.hasLobbyItems.contains(event.getPlayer().getUniqueId())) {
			return;
		}

		Player player = event.getPlayer();
		ItemStack item = event.getItem();

		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (item.getItemMeta().getDisplayName() == LobbyItems.SELECT_KIT.getItemMeta().getDisplayName()) {
				player.chat("/kits");
			} else if (item.getItemMeta().getDisplayName() == LobbyItems.START_SPECTATING.getItemMeta().getDisplayName()) {
				player.chat("/spectate");
			}
		}
	}

	@EventHandler
	public void quit(PlayerQuitEvent event) {
		if (LobbyItems.hasOnlyLobbyItems(event.getPlayer())) {
			event.getPlayer().getInventory().clear();
		}
	}

	@EventHandler
	public void interact(PlayerInteractAtEntityEvent event) {
		if (LobbyItems.hasLobbyItems.contains(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void interact(PlayerInteractEntityEvent event) {
		if (LobbyItems.hasLobbyItems.contains(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void drop(PlayerDropItemEvent event) {
		if (LobbyItems.hasLobbyItems.contains(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void pickup(PlayerPickupItemEvent event) {
		if (LobbyItems.hasLobbyItems.contains(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void inv1(InventoryClickEvent event) {
		if (LobbyItems.hasLobbyItems.contains(event.getWhoClicked().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void inv2(InventoryDragEvent event) {
		if (LobbyItems.hasLobbyItems.contains(event.getWhoClicked().getUniqueId())) {
			event.setCancelled(true);
		}
	}

}
