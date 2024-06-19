package uk.rayware.arena.kit.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import uk.rayware.arena.kit.Kit;
import uk.rayware.arena.util.KitUtil;

import java.util.List;

public class PvPKit extends Kit {

	public PvPKit() {
		super("PvP", Material.STONE_SWORD, List.of("Basic PvP Kit"), 0);
	}

	@Override
	public void giveKitToPlayer(Player player) {
		Inventory inv = player.getInventory();

		inv.addItem(new ItemStack(Material.STONE_SWORD), new ItemStack(Material.GOLDEN_APPLE, 10));

		KitUtil.applyOrGive(player,
				null,
				new ItemStack(Material.IRON_CHESTPLATE),
				new ItemStack(Material.CHAINMAIL_LEGGINGS),
				new ItemStack(Material.CHAINMAIL_BOOTS));
	}
}
