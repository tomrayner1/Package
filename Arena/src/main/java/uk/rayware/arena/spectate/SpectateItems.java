package uk.rayware.arena.spectate;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import uk.rayware.arena.ArenaLocale;
import uk.rayware.nitrolib.util.ItemBuilder;

@UtilityClass
public class SpectateItems {

	public final ItemStack STOP_SPECTATING = new ItemBuilder(Material.INK_SACK).durability(10).name(ArenaLocale.wrap("&c&lStop Spectating")).lore("&7Right click to run /spectate").build();
	public final ItemStack PLAYER_SELECTOR = new ItemBuilder(Material.ITEM_FRAME).name(ArenaLocale.wrap("&e&lSpectate Player")).lore("&7Right click").build();
	public final ItemStack HIDE_VIEWMODEL = new ItemBuilder(Material.CARPET).name(" ").build();

	public void giveSpecItems(Player player) {
		player.getInventory().setItem(0, PLAYER_SELECTOR);
		player.getInventory().setItem(1, HIDE_VIEWMODEL);
		player.getInventory().setItem(8, STOP_SPECTATING);
	}
}
