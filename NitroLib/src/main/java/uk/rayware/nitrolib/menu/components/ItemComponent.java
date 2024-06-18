package uk.rayware.nitrolib.menu.components;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import uk.rayware.nitrolib.menu.Menu;

import java.util.Collections;
import java.util.List;

public class ItemComponent extends MenuComponent {

	@Getter
	private final ItemStack itemStack;

	public ItemComponent(Menu parentMenu, int menuSlot, ItemStack itemStack) {
		super(parentMenu, menuSlot);
		this.itemStack = itemStack;
	}

	public ItemComponent(Menu parentMenu, int menuSlot, Material material, String name, List<String> lore) {
		super(parentMenu, menuSlot);

		this.itemStack = new ItemStack(material, 1);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(lore);
		itemStack.setItemMeta(itemMeta);
	}

	public ItemComponent(Menu parentMenu, int menuSlot, Material material, String name) {
		this(parentMenu, menuSlot, material, name, Collections.emptyList());
	}

	public ItemComponent(Menu parentMenu, int menuSlot, Material material) {
		this(parentMenu, menuSlot, material, "");
	}

}
