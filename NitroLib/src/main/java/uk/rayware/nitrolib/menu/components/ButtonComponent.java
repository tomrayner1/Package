package uk.rayware.nitrolib.menu.components;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import uk.rayware.nitrolib.menu.Menu;

import java.util.Collections;
import java.util.List;

public class ButtonComponent extends ItemComponent {

	public interface ClickListener {
		boolean onClick(Player player, Menu menu);
	}

	private final ClickListener clickListener;

	public ButtonComponent(Menu parentMenu, int menuSlot, ItemStack itemStack, ClickListener clickListener) {
		super(parentMenu, menuSlot, itemStack);
		this.clickListener = clickListener;
	}

	public ButtonComponent(Menu parentMenu, int menuSlot, Material material, String name, List<String> lore, ClickListener clickListener) {
		super(parentMenu, menuSlot, material, name, lore);
		this.clickListener = clickListener;
	}

	public ButtonComponent(Menu parentMenu, int menuSlot, Material material, String name, ClickListener clickListener) {
		this(parentMenu, menuSlot, material, name, Collections.emptyList(), clickListener);
	}

	public ButtonComponent(Menu parentMenu, int menuSlot, Material material, ClickListener clickListener) {
		this(parentMenu, menuSlot, material, "", clickListener);
	}

	public boolean runOnClick(Player player, Menu menu) {
		try {
			return clickListener.onClick(player, menu);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
