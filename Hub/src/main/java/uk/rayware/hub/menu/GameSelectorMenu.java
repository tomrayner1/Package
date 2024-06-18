package uk.rayware.hub.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import uk.rayware.hub.util.GUIUtil;
import uk.rayware.nitrolib.NitroLib;
import uk.rayware.nitrolib.menu.Menu;
import uk.rayware.nitrolib.menu.components.ButtonComponent;
import uk.rayware.nitrolib.util.ItemBuilder;

import java.util.Arrays;
import java.util.Collections;

public class GameSelectorMenu extends Menu {

	public GameSelectorMenu() {
		super(ChatColor.GOLD + "" + ChatColor.BOLD + "Game Selector", 3);

		addMenuComponents(
				GUIUtil.generateGUI(this, 10, Material.IRON_SWORD, "Arena", Arrays.asList(
						"PvP with different kits and teams.",
						"1v1 Ranked ladders with each kit.",
						"Supports versions 1.8+.")),
				GUIUtil.generateGUI(this, 12, Material.GRASS, "Skyblock", Arrays.asList(
						"Start on your own island.",
						"Build your way to the top of the leaderboard.",
						"Supports versions 1.8+.")),
				GUIUtil.generateGUI(this, 14, Material.DIAMOND_SPADE, "Towny", Arrays.asList(
						"Multiplayer survival with land claiming.",
						"Only supports version 1.19.3.")),
				GUIUtil.generateGUI(this, 16, Material.DIAMOND_SWORD, "Teams", Collections.singletonList("???"))
		);

		fill(null);
	}
}
