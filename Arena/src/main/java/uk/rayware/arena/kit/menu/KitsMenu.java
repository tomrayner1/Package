package uk.rayware.arena.kit.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import uk.rayware.arena.Arena;
import uk.rayware.arena.kit.Kit;
import uk.rayware.nitrolib.menu.Menu;
import uk.rayware.nitrolib.menu.components.ButtonComponent;
import uk.rayware.nitrolib.menu.components.ItemComponent;
import uk.rayware.nitrolib.util.ItemBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KitsMenu extends Menu {

	private static final int ROWS;

	static {
		ROWS = Math.max(3, Math.floorDiv(Arena.getInstance().getKitHandler().getKits().size(), 9) + 1);
	}

	public KitsMenu() {
		super(ChatColor.GREEN + "Select a Kit", ROWS);

		List<Integer> protectedSlots = Arrays.asList(17, 18, 26, 27, 35, 36, 44, 45, 53, 54, 62, 63, 71, 72, 80, 81);

		for (int i = 0; i < 10; i++) {
			addMenuComponent(new ItemComponent(this, i, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(15).name(" ").build()));
		}

		for (int i : protectedSlots) {
			if (i < ROWS * 9) {
				addMenuComponent(new ItemComponent(this, i, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(15).name(" ").build()));
			}
		}

		for (int i = (ROWS - 1) * 9; i < ROWS * 9; i++) {
			if (!protectedSlots.contains(i)) {
				addMenuComponent(new ItemComponent(this, i, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(15).name(" ").build()));
			}
		}

		int slot = 10;

		for (Kit kit : Arena.getInstance().getKitHandler().getKits()) {

			if (protectedSlots.contains(slot)) {
				slot++;
				if (protectedSlots.contains(slot)) {
					slot++;
				}
			}

			List<String> lore = new ArrayList<>();

			for (String line : kit.getDescription()) {
				lore.add("&7" + line);
			}

			lore.add("");
			lore.add("&eLeft click to use");

			ItemStack item = new ItemBuilder(kit.getIcon()).name("&a&l" + kit.getName() + " Kit").lore(lore).build();

			addMenuComponent(new ButtonComponent(this, slot++, item, (player, menu) -> {
				player.closeInventory();
				player.chat("/kit " + kit.getName());
				return true;
			}));
		}
	}

}
