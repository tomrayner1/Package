package uk.rayware.arena.lobby;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import uk.rayware.arena.Arena;
import uk.rayware.arena.cooldown.impl.CombatTag;
import uk.rayware.arena.player.PlayerState;
import uk.rayware.arena.player.PlayerStateChangeEvent;
import uk.rayware.arena.util.InventoryUtil;
import uk.rayware.nitrolib.NitroLib;

@Getter
public class LobbyHandler implements Listener {

	@Setter
	private CuboidSelection spawn;

	public LobbyHandler() {
		Bukkit.getServer().getPluginManager().registerEvents(this, Arena.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new LobbyItemListener(), Arena.getInstance());

		FileConfiguration config = Arena.getInstance().getConfig();

		CombatTag.setDuration(config.getDouble("settings.combat-tag.duration"));

		if (config.isSet("spawn.world")) {
			BlockVector p1 = new BlockVector(
					config.getInt("spawn.p1.x"),
					config.getInt("spawn.p1.y"),
					config.getInt("spawn.p1.z"));

			BlockVector p2 = new BlockVector(
					config.getInt("spawn.p2.x"),
					config.getInt("spawn.p2.y"),
					config.getInt("spawn.p2.z"));

			spawn = new CuboidSelection(Bukkit.getWorld(config.getString("spawn.world")), p1, p2);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void block(BlockBreakEvent event) {
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void block(BlockPlaceEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void join(PlayerJoinEvent event) {
		if (!event.getPlayer().hasPlayedBefore()) {
			event.getPlayer().teleport(NitroLib.getInstance().getSpawn());
		}

		if (InventoryUtil.isEmpty(event.getPlayer().getInventory())) {
			LobbyItems.giveLobbyItems(event.getPlayer());
		}
	}

	@EventHandler
	public void respawn(PlayerRespawnEvent event) {
		CombatTag.get(event.getPlayer().getUniqueId()).end();

		if (Arena.getInstance().getPlayerStateHandler().get(event.getPlayer()) == PlayerState.IN_ARENA) {
			Arena.getInstance().getPlayerStateHandler().set(event.getPlayer(), PlayerState.IN_LOBBY);
		}
	}

	@EventHandler
	public void hit(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player || event.getEntity() instanceof Player) {
			Player player = ((Player) event.getEntity()).getPlayer();
			Player damager = ((Player) event.getDamager()).getPlayer();

			if (Arena.getInstance().getPlayerStateHandler().get(player) == PlayerState.IN_LOBBY || Arena.getInstance().getPlayerStateHandler().get(player) == PlayerState.SPECTATING) {
				event.setCancelled(true);
				return;
			}

			if (Arena.getInstance().getPlayerStateHandler().get(damager) == PlayerState.IN_LOBBY || Arena.getInstance().getPlayerStateHandler().get(damager) == PlayerState.SPECTATING) {
				event.setCancelled(true);
				return;
			}

			if (!CombatTag.get(player.getUniqueId()).active()) {
				player.sendMessage(ChatColor.RED + "You are now combat tagged for " + CombatTag.getDuration() + "s.");
			}

			if (!CombatTag.get(damager.getUniqueId()).active()) {
				damager.sendMessage(ChatColor.RED + "You are now combat tagged for " + CombatTag.getDuration() + "s.");
			}

			CombatTag.get(damager.getUniqueId()).reset();
			CombatTag.get(player.getUniqueId()).reset();
		}
	}

	@EventHandler
	public void move(PlayerMoveEvent event) {
		CuboidSelection spawn = Arena.getInstance().getLobbyHandler().getSpawn();

		if (spawn == null) {
			return;
		}

		PlayerState state = Arena.getInstance().getPlayerStateHandler().get(event.getPlayer());

		if (state == PlayerState.SPECTATING) {
			return;
		}

		boolean nowIn = spawn.contains(event.getTo());
		boolean wasIn = spawn.contains(event.getFrom());

		if (nowIn && !wasIn && CombatTag.get(event.getPlayer().getUniqueId()).active()) {
			event.setCancelled(true);
			event.getPlayer().teleport(event.getFrom());
		}
	}

	@EventHandler
	public void teleport(PlayerTeleportEvent event) {
		CuboidSelection spawn = Arena.getInstance().getLobbyHandler().getSpawn();

		if (spawn == null) {
			return;
		}

		boolean nowIn = spawn.contains(event.getTo());
		boolean wasIn = spawn.contains(event.getFrom());
		PlayerState state = Arena.getInstance().getPlayerStateHandler().get(event.getPlayer());

		if (nowIn && !wasIn && state != PlayerState.SPECTATING) {
			Arena.getInstance().getPlayerStateHandler().set(event.getPlayer(), PlayerState.IN_LOBBY);
		}
	}

	@EventHandler
	public void state(PlayerStateChangeEvent event) {
		Player player = event.getPlayer();

		if (event.getTo() == PlayerState.IN_LOBBY) {
			player.setHealth(20);
			player.setFoodLevel(20);

			if (event.getFrom() == null) {
				if (LobbyItems.hasOnlyLobbyItems(event.getPlayer())) {
					LobbyItems.hasLobbyItems.add(event.getPlayer().getUniqueId());
				}
			} else if (event.getFrom() == PlayerState.IN_ARENA) {
				if (InventoryUtil.isEmpty(player.getInventory())) {
					LobbyItems.giveLobbyItems(player);
				}
			}
		} else if (event.getFrom() == PlayerState.IN_LOBBY && event.getTo() == PlayerState.IN_ARENA) {
			if (LobbyItems.hasLobbyItems.contains(player.getUniqueId())) {
				player.getInventory().clear();

				Arena.getInstance().getKitHandler().getDefaultKit().apply(player);
			}
		}
	}

	@EventHandler
	public void food(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player player) {
			if (Arena.getInstance().getPlayerStateHandler().get(player) == PlayerState.IN_LOBBY) {
				event.setFoodLevel(20);
			}
		}
	}
}
