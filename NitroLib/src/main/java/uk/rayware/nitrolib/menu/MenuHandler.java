package uk.rayware.nitrolib.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import uk.rayware.nitrolib.NitroLib;
import uk.rayware.nitrolib.menu.components.ButtonComponent;
import uk.rayware.nitrolib.menu.components.ItemComponent;
import uk.rayware.nitrolib.menu.components.MenuComponent;
import uk.rayware.nitrolib.menu.components.SlotComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MenuHandler implements Listener {

	private final HashMap<Player, Menu> openMenus;

	public MenuHandler() {
		this.openMenus = new HashMap<>();
		NitroLib.getInstance().getServer().getPluginManager().registerEvents(this, NitroLib.getInstance());
	}

	public void openMenuForPlayer(Player player, Menu menu) {
		player.openInventory(menu.getInventory());

		openMenus.remove(player);
		openMenus.put(player, menu);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryDrag(InventoryDragEvent event) {
		Optional<Map.Entry<Player, Menu>> optionalEntry = openMenus.entrySet().stream()
				.filter(entry -> entry.getKey() == event.getWhoClicked() &&
						entry.getValue().getInventory().hashCode() == event.getInventory().hashCode()).findAny();

		if (optionalEntry.isEmpty())
			return;

		Menu menu = optionalEntry.get().getValue();

		if (menu.isAllowDragging())
			return;

		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getClickedInventory() == null) {
			return;
		}

		Optional<Map.Entry<Player, Menu>> optionalEntry = openMenus.entrySet().stream()
				.filter(entry -> entry.getKey() == event.getWhoClicked() &&
						entry.getValue().getInventory().hashCode() == event.getClickedInventory().hashCode()).findAny();

		if (optionalEntry.isEmpty())
			return;

		Player player = optionalEntry.get().getKey();
		Menu menu = optionalEntry.get().getValue();

		if (!(event.getClick() == ClickType.LEFT || event.getClick() == ClickType.SHIFT_LEFT)) {
			event.setCancelled(true);
			return;
		}

		for (MenuComponent component : menu.getComponentList()) {
			if (component.getMenuSlot() != event.getSlot())
				continue;

			if (component instanceof ButtonComponent) {
				event.setCancelled(true);
				((ButtonComponent) component).runOnClick(player, menu);
				continue;
			}

			if (component instanceof ItemComponent itemComponent) {
				ItemStack stackInSlot = itemComponent.getItemInSlot();

				event.getClickedInventory().setItem(event.getSlot(), stackInSlot);
				player.setItemOnCursor(event.getCursor());
				event.setCancelled(true);

				continue;
			}

			if (component instanceof SlotComponent slotComponent) {
				ItemStack stackInSlot = slotComponent.getItemInSlot();

				if (stackInSlot == null && event.getCursor().getType() == Material.AIR)
					continue;

				if (stackInSlot == null && event.getCursor().getType() != Material.AIR) {
					event.setCancelled(true);

					if (slotComponent.onSlotFill(player, event.getClickedInventory().getItem(event.getSlot()), event.getCursor())) {
						event.getClickedInventory().setItem(event.getSlot(), event.getCursor());
						player.setItemOnCursor(null);
					} else {
						event.getClickedInventory().setItem(event.getSlot(), stackInSlot);
						player.setItemOnCursor(event.getCursor());
					}

					return;
				}

				if (stackInSlot != null && event.getCursor().getType() == Material.AIR) {
					event.setCancelled(true);

					if (slotComponent.onSlotEmpty(player, event.getClickedInventory().getItem(event.getSlot()))) {
						player.setItemOnCursor(stackInSlot);
						event.getClickedInventory().setItem(event.getSlot(), null);
					} else {
						event.getClickedInventory().setItem(event.getSlot(), stackInSlot);
						player.setItemOnCursor(event.getCursor());
					}

					return;
				}

				if (stackInSlot != null && event.getCursor().getType() != Material.AIR) {
					if (!slotComponent.onSlotEmpty(player, event.getClickedInventory().getItem(event.getSlot()))) {
						event.setCancelled(true);
					} else {
						event.getClickedInventory().setItem(event.getSlot(), null);
					}

					if (!slotComponent.onSlotFill(player, event.getClickedInventory().getItem(event.getSlot()), event.getCursor())) {
						event.setCancelled(true);
					} else {
						event.getClickedInventory().setItem(event.getSlot(), event.getCursor());
						player.setItemOnCursor(null);
					}

				}
			}
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		if (openMenus.containsKey(player)) {
			Menu menu = openMenus.get(player);
			menu.onClose(player);
			openMenus.remove(player);
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (openMenus.containsKey(event.getPlayer())) {
			Menu menu = openMenus.get(event.getPlayer());
			Player player = event.getPlayer().getServer().getPlayer(event.getPlayer().getUniqueId());
			menu.onClose(player);
			openMenus.remove(event.getPlayer());
		}
	}
}
