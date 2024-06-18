package uk.rayware.hub.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import uk.rayware.nitrogen.NitrogenAPI;
import uk.rayware.nitrolib.NitroLib;
import uk.rayware.nitrolib.menu.Menu;
import uk.rayware.nitrolib.menu.components.ButtonComponent;
import uk.rayware.nitrolib.menu.components.MenuComponent;
import uk.rayware.nitrolib.util.ItemBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@UtilityClass
public class GUIUtil {

	public MenuComponent generateGUI(Menu menu, int slot, Material material, String serverId, List<String> description) {
		Map<String, String> data = NitrogenAPI.getStoredServerData(serverId);

		List<String> lore = new ArrayList<>();

		if (data.isEmpty() || data.get("name") == null) {
			lore.add(ChatColor.GRAY + serverId);
			lore.add("");
			lore.add(ChatColor.GOLD + NitroLib.bar + ChatColor.WHITE + " Status: " + ChatColor.RED + "Offline");

			if (!description.isEmpty()) {
				lore.add("");
				description.forEach(line -> lore.add(ChatColor.GOLD + line));
			}

			ItemStack item = new ItemBuilder(Material.REDSTONE_BLOCK)
					.name(ChatColor.RED + "" + ChatColor.BOLD + serverId)
					.lore(lore)
					.build();

			return new ButtonComponent(menu, slot, item, ((player, menu1) -> {
				player.sendMessage(ChatColor.RED + "Server is unavailable at this time, please try again later!");
				return true;
			}));
		}

		String status = StatusUtil.getStatus(data);

		lore.add(ChatColor.GRAY + data.get("name"));
		lore.add("");
		lore.add(ChatColor.GOLD + NitroLib.bar + ChatColor.WHITE + " Online: " + ChatColor.YELLOW + data.get("players") + ChatColor.WHITE + "/" + ChatColor.YELLOW + data.get("max"));
		lore.add(ChatColor.GOLD + NitroLib.bar + ChatColor.WHITE + " Status: " + status);
		lore.add("");

		if (!description.isEmpty()) {
			description.forEach(line -> lore.add(ChatColor.GOLD + line));
		}

		lore.add("");
		lore.add(ChatColor.YELLOW + "Left click to join the queue.");

		ItemStack item;

		if (status.equals(ChatColor.RED + "Offline")) {
			item = new ItemBuilder(Material.REDSTONE_BLOCK)
					.name(ChatColor.RED + "" + ChatColor.BOLD + data.get("display"))
					.lore(lore)
					.build();
		} else {
			item = new ItemBuilder(material)
					.name(ChatColor.GREEN + "" + ChatColor.BOLD + data.get("display"))
					.lore(lore)
					.build();
		}

		return new ButtonComponent(menu, slot, item, ((player, menu1) -> {
			player.closeInventory();
			player.chat("/gboqueue:joinqueue " + serverId);
			return true;
		}));
	}

}
