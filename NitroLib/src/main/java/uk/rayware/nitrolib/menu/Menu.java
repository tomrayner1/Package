package uk.rayware.nitrolib.menu;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import uk.rayware.nitrolib.menu.components.ItemComponent;
import uk.rayware.nitrolib.menu.components.MenuComponent;
import uk.rayware.nitrolib.util.ItemBuilder;

import java.util.ArrayList;

public class Menu {

	private final String title;
	private int rows;
	@Getter
	private ArrayList<MenuComponent> componentList;
	private Inventory inventory;
	@Getter @Setter
	private boolean allowDragging = false;

	public Menu(String title, int rows) {
		this.title = title;
		this.rows = rows;
		this.componentList = new ArrayList<>();
	}

	public void onClose(Player player) { }

	public Inventory getInventory() {
		if (inventory == null) {
			inventory = Bukkit.createInventory(null, rows * 9, title);

			for (MenuComponent component : componentList) {
				if (!(component instanceof ItemComponent itemComponent))
					continue;

				inventory.setItem(itemComponent.getMenuSlot(), itemComponent.getItemStack());
			}
		}

		return inventory;
	}

	public void addMenuComponent(MenuComponent component) {
		if (isSlotTaken(component.getMenuSlot()))
			throw new RuntimeException("Multiple components using the same inventory slot");

		componentList.add(component);
	}

	public void addMenuComponents(MenuComponent... components) {
		for (MenuComponent component : components) {
			addMenuComponent(component);
		}
	}

	public boolean isSlotTaken(int slot) {
		for (MenuComponent component : componentList) {
			if (component.getMenuSlot() == slot)
				return true;
		}

		return false;
	}

	public void fill(ItemStack itemStack) {
		if (itemStack == null) {
			itemStack = new ItemBuilder(Material.STAINED_GLASS_PANE).durability(15).name(" ").build();
		}

		for (int i = 0; i < rows * 9; i++) {
			try {
				addMenuComponent(new ItemComponent(this, i, itemStack));
			} catch (RuntimeException ignored) {
			}
		}
	}


}
