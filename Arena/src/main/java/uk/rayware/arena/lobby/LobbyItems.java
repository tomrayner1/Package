package uk.rayware.arena.lobby;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import uk.rayware.arena.ArenaLocale;
import uk.rayware.nitrolib.util.ItemBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@UtilityClass
public final class LobbyItems {

	public final Set<UUID> hasLobbyItems = new HashSet<>();

	public final ItemStack SELECT_KIT = new ItemBuilder(Material.CHEST).name(ArenaLocale.wrap("&e&lSelect a Kit")).lore("&7Right click to run /kits").build();
	public final ItemStack SELECT_PREVIOUS_KIT = new ItemBuilder(Material.WATCH).name(ArenaLocale.wrap("&e&lSelect Previous Kit")).lore("&7Right click").build();
	public final ItemStack START_SPECTATING = new ItemBuilder(Material.INK_SACK).durability(8).name(ArenaLocale.wrap("&e&lStart Spectating")).lore("&7Right click to run /spectate").build();

	public void giveLobbyItems(Player player) {
		hasLobbyItems.add(player.getUniqueId());
		player.getInventory().setHeldItemSlot(0);

		player.getInventory().setItem(0, LobbyItems.SELECT_KIT);
		player.getInventory().setItem(8, LobbyItems.START_SPECTATING);
	}

	public boolean hasOnlyLobbyItems(Player player) {
		PlayerInventory inv = player.getInventory();

		for (ItemStack item : inv.getArmorContents()) {
			if (item != null) {
				if (item.getType() != Material.AIR) {
					return false;
				}
			}
		}

		for (int i = 1; i < 36; i++) {
			if (i != 8) {
				ItemStack item = inv.getItem(i);

				if (item != null) {
					if (item.getType() != Material.AIR) {
						return false;
					}
				}
			}
		}

		return true;
	}

}
