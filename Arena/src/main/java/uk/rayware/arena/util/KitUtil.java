package uk.rayware.arena.util;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@UtilityClass
public class KitUtil {

	public void applyOrGive(Player player, ItemStack head, ItemStack chest, ItemStack legs, ItemStack feet) {
		PlayerInventory inv = player.getInventory();

		if (head != null) {
			if (inv.getHelmet() == null) {
				inv.setHelmet(head);
			} else {
				inv.addItem(head);
			}
		}

		if (chest != null) {
			if (inv.getChestplate() == null) {
				inv.setChestplate(chest);
			} else {
				inv.addItem(chest);
			}
		}

		if (legs != null) {
			if (inv.getLeggings() == null) {
				inv.setLeggings(legs);
			} else {
				inv.addItem(legs);
			}
		}

		if (feet != null) {
			if (inv.getBoots() == null) {
				inv.setBoots(feet);
			} else {
				inv.addItem(feet);
			}
		}
	}

}
