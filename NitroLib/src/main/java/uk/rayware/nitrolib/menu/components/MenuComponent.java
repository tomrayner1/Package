package uk.rayware.nitrolib.menu.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import uk.rayware.nitrolib.menu.Menu;

@AllArgsConstructor
@Getter
public class MenuComponent {

	private Menu parentMenu;
	private int menuSlot;

	public ItemStack getItemInSlot() {
		return parentMenu.getInventory().getItem(menuSlot);
	}

}
