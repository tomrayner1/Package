package uk.rayware.arena.spectate;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import uk.rayware.arena.Arena;
import uk.rayware.arena.ArenaLocale;
import uk.rayware.arena.lobby.LobbyItems;
import uk.rayware.arena.player.PlayerState;
import uk.rayware.nitrolib.NitroLib;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class SpectateHandler implements Listener {

	private final Set<UUID> spectators = new HashSet<>();

	public SpectateHandler() {
		Bukkit.getServer().getPluginManager().registerEvents(this, Arena.getInstance());
	}

	public void startSpectating(Player player) {
		player.setGameMode(GameMode.CREATIVE);
		player.setFlying(true);
		player.getInventory().clear();
		LobbyItems.hasLobbyItems.remove(player.getUniqueId());
		SpectateItems.giveSpecItems(player);

		Arena.getInstance().getPlayerStateHandler().set(player, PlayerState.SPECTATING);
		spectators.add(player.getUniqueId());

		for (Player player1 : Bukkit.getOnlinePlayers()) {
			if (!player1.hasMetadata("modmode")) {
				player1.hidePlayer(player);
			}
		}

		player.getScoreboard().getTeam("ally").removeEntry(player.getName());
		player.getScoreboard().getTeam("spectator").addEntry(player.getName());

		player.sendMessage(ArenaLocale.NOW_SPECTATING);
	}

	public void stopSpectating(Player player) {
		player.setGameMode(GameMode.SURVIVAL);
		player.setFlying(false);
		player.getInventory().clear();
		LobbyItems.giveLobbyItems(player);

		Arena.getInstance().getPlayerStateHandler().set(player, PlayerState.IN_LOBBY);
		LobbyItems.hasLobbyItems.add(player.getUniqueId());
		spectators.remove(player.getUniqueId());

		player.teleport(NitroLib.getInstance().getSpawn());

		for (Player player1 : Bukkit.getOnlinePlayers()) {
			player1.showPlayer(player);
		}

		player.getScoreboard().getTeam("spectator").removeEntry(player.getName());
		player.getScoreboard().getTeam("ally").addEntry(player.getName());

		player.sendMessage(ArenaLocale.NO_LONGER_SPECTATING);
	}

	@EventHandler
	public void interact(PlayerInteractEvent event) {
		if (event.getItem() == null) {
			return;
		}

		Player player = event.getPlayer();
		ItemStack item = event.getItem();

		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (item.getItemMeta().getDisplayName() == SpectateItems.STOP_SPECTATING.getItemMeta().getDisplayName()) {
				player.chat("/spectate");
			}
		}
	}

	@EventHandler
	public void join(PlayerJoinEvent event) {
		for (UUID uuid : spectators) {
			event.getPlayer().hidePlayer(Bukkit.getPlayer(uuid));
		}
	}

	@EventHandler
	public void quit(PlayerQuitEvent event) {
		if (spectators.contains(event.getPlayer().getUniqueId())) {
			stopSpectating(event.getPlayer());
			event.getPlayer().getInventory().clear();
		}
	}

	@EventHandler
	public void specEvent(BlockBreakEvent event) {
		if (spectators.contains(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void specEvent(BlockPlaceEvent event) {
		if (spectators.contains(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void specEvent(InventoryClickEvent event) {
		if (spectators.contains(event.getWhoClicked().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void specEvent(InventoryDragEvent event) {
		if (spectators.contains(event.getWhoClicked().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void specEvent(PlayerInteractEvent event) {
		if (spectators.contains(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void specEvent(PlayerInteractAtEntityEvent event) {
		if (spectators.contains(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void specEvent(PlayerInteractEntityEvent event) {
		if (spectators.contains(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void specEvent(EntityDamageByEntityEvent event) {
		if (spectators.contains(event.getDamager().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void specEvent(PlayerDropItemEvent event) {
		if (spectators.contains(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void specEvent(PlayerPickupItemEvent event) {
		if (spectators.contains(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}

}
