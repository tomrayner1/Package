package uk.rayware.nitrolib.menu.components;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import uk.rayware.nitrolib.menu.Menu;

public class SlotComponent extends MenuComponent {

	public interface SlotListener {
		boolean run(Player player, ItemStack oldStack, ItemStack newStack);
	}

	private final SlotListener onSlotFill;
	private final SlotListener onSlotEmpty;

	public SlotComponent(Menu parentMenu, int menuSlot, SlotListener onSlotFill, SlotListener onSlotEmpty) {
		super(parentMenu, menuSlot);
		this.onSlotFill = onSlotFill;
		this.onSlotEmpty = onSlotEmpty;
	}

	public boolean onSlotFill(Player player, ItemStack oldStack, ItemStack newStack) {
		try {
			return onSlotFill.run(player, oldStack, newStack);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean onSlotEmpty(Player player, ItemStack oldStack) {
		try {
			return onSlotEmpty.run(player, oldStack, null);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
