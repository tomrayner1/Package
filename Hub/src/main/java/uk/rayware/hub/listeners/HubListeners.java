package uk.rayware.hub.listeners;

import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import uk.rayware.hub.HubPlugin;
import uk.rayware.hub.menu.GameSelectorMenu;
import uk.rayware.hub.menu.ProfileMenu;
import uk.rayware.nitrolib.NitroLib;
import uk.rayware.nitrolib.util.ItemBuilder;

public class HubListeners implements Listener {

	public final String gameSelector = ChatColor.translateAlternateColorCodes('&', "&9»&6&l Game Selector &9«");
	public final String profile = ChatColor.translateAlternateColorCodes('&', "&9»&6&l Your Profile &9«");

	@EventHandler
	public void join(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		if (player.hasPermission("hub.fly"))
		{
			player.setAllowFlight(true);
		}
		else
		{
			player.setAllowFlight(false);
			player.setFlying(false);
		}

		player.setGameMode(GameMode.ADVENTURE);
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.getInventory().setHeldItemSlot(2);

		// Remove this AIDS speed, idk why i added it!
		//player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3, true));

		player.teleport(NitroLib.getInstance().getSpawn());

		player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 0f);

		player.getInventory().setItem(2, new ItemBuilder(Material.WATCH)
				.name(gameSelector)
				.lore(ChatColor.GRAY + "Right click to view all of the current game modes.").build());
		player.getInventory().setItem(6, new ItemBuilder(Material.SKULL_ITEM)
				.skull(player.getName())
				.name(profile)
				.lore(ChatColor.GRAY + "Right click to view and edit your profile and settings.").build());
	}

	@EventHandler
	public void interact(PlayerInteractEvent event) {
		if (event.getItem() == null || !event.getItem().hasItemMeta()) {
			return;
		}

		Player player = event.getPlayer();
		ItemStack item = event.getItem();

		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			switch (item.getType()) {
				case WATCH -> {
					if (item.getItemMeta().getDisplayName().equals(gameSelector))
						NitroLib.getInstance().getMenuHandler().openMenuForPlayer(player, new GameSelectorMenu());
				}
				case SKULL_ITEM -> {
					if (item.getItemMeta().getDisplayName().equals(profile))
						NitroLib.getInstance().getMenuHandler().openMenuForPlayer(player, new ProfileMenu(player));
				}
			}
		}
	}

	@EventHandler
	public void move2(PlayerMoveEvent event) {
		if (event.getTo().getY() > 300 || event.getTo().getY() < 0) {
			event.getPlayer().teleport(NitroLib.getInstance().getSpawn());
		}
	}

	@EventHandler
	public void destroy(BlockBreakEvent event) {
		if (!event.getPlayer().hasMetadata("build")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void place(BlockPlaceEvent event) {
		if (!event.getPlayer().hasMetadata("build")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void bucket(PlayerBucketFillEvent event) {
		if (!event.getPlayer().hasMetadata("build")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void bucket2(PlayerBucketEmptyEvent event) {
		if (!event.getPlayer().hasMetadata("build")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void inv(InventoryClickEvent event) {
		if (!event.getWhoClicked().hasMetadata("build")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void foodLevel(FoodLevelChangeEvent event) {
		event.setFoodLevel(20);
		event.setCancelled(true);
	}

	@EventHandler
	public void damage(EntityDamageEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void damageBy(EntityDamageByEntityEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void drop(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void weather(WeatherChangeEvent event) {
		if (event.toWeatherState()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void thunder(ThunderChangeEvent event) {
		if (event.toThunderState()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void pickup(PlayerPickupItemEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void spawn(CreatureSpawnEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void bed(PlayerBedEnterEvent event) {
		event.setCancelled(true);
	}

	/*
	@EventHandler
	public void move(PlayerMoveEvent event) {
		Player player = event.getPlayer();

		if (player.hasMetadata("modmode")) {
			return;
		}

		if (player.getGameMode() != GameMode.CREATIVE &&
				player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
			player.setAllowFlight(true);
		}
	}

	@EventHandler
	public void allowFly(PlayerToggleFlightEvent event) {
		Player player = event.getPlayer();

		if (player.hasMetadata("modmode")) {
			return;
		}

		if (player.getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
			player.setVelocity(player.getLocation().getDirection().multiply(1.5).setY(0.8));
			player.playSound(player.getLocation(), Sound.EXPLODE, 1, 1);
			player.spigot().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 1, 1, 1, 1, 1, 1, 1, 1);
		}
	}
	 */

}
