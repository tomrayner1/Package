package uk.rayware.arena.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@UtilityClass
public class InventoryUtil {

	public boolean isEmpty(PlayerInventory inventory) {
		for (ItemStack item : inventory.getContents()) {
			if (item != null) {
				if (item.getType() != Material.AIR) {
					return false;
				}
			}
		}

		for (ItemStack item : inventory.getArmorContents()) {
			if (item != null) {
				if (item.getType() != Material.AIR) {
					return false;
				}
			}
		}

		return true;
	}

}
