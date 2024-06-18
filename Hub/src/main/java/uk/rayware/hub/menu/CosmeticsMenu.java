package uk.rayware.hub.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import uk.rayware.nitrolib.NitroLib;
import uk.rayware.nitrolib.menu.Menu;
import uk.rayware.nitrolib.menu.components.ButtonComponent;
import uk.rayware.nitrolib.menu.components.ItemComponent;
import uk.rayware.nitrolib.util.ItemBuilder;

import java.util.Arrays;

public class CosmeticsMenu extends Menu {

	public CosmeticsMenu() {
		super(ChatColor.GOLD + "" + ChatColor.BOLD + "Cosmetics", 3);

		for (int i = 0; i <= 9; i++) {
			addMenuComponent(new ItemComponent(this, i, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(15).name(" ").build()));
		}

		for (int i = 17; i <= 26; i++) {
			addMenuComponent(new ItemComponent(this, i, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(15).name(" ").build()));
		}

		addMenuComponents(
				new ButtonComponent(this, 10, new ItemBuilder(Material.WOOL).name(ChatColor.GOLD + "" + ChatColor.BOLD + "Custom Colours")
						.lore(Arrays.asList(
								ChatColor.GRAY + "Customize the colour of your name.",
								"",
								ChatColor.YELLOW + "Left click to run /colour."
						)).build(), (player, menu) -> {
					player.closeInventory();
					player.chat("/colour");
					return true;
				}),

				new ButtonComponent(this, 11, new ItemBuilder(Material.NAME_TAG).name(ChatColor.GOLD + "" + ChatColor.BOLD + "Tags")
						.lore(Arrays.asList(
								ChatColor.GRAY + "Customize your chat messages with tags.",
								"",
								ChatColor.YELLOW + "Left click to run /tags."
						)).build(), (player, menu) -> {
					player.closeInventory();
					player.chat("/tags");
					return true;
				})
		);
	}
}
