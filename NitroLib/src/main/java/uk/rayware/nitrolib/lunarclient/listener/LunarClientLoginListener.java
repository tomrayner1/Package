package uk.rayware.nitrolib.lunarclient.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import uk.rayware.nitrolib.NitroLib;
import uk.rayware.nitrolib.lunarclient.LunarClientAPI;
import uk.rayware.nitrolib.lunarclient.events.LCPlayerRegisterEvent;
import uk.rayware.nitrolib.lunarclient.net.packets.client.LCPacketUpdateWorld;

@RequiredArgsConstructor
public class LunarClientLoginListener implements Listener {
	
	private final LunarClientAPI lunarClientAPI;
	private final NitroLib nitroLib = NitroLib.getInstance();
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		Bukkit.getScheduler().runTaskLater(nitroLib, () -> {
			if (!lunarClientAPI.isRunningLunarClient(player)) {
				lunarClientAPI.failPlayerRegister(player);
			}
		}, 2 * 20L);
	}
	
	@EventHandler
	public void onRegister(PlayerRegisterChannelEvent event) {
		//LunarClientAPI.getInstance().log("PlayerRegisterChannelEvent " + event.getPlayer().getName());
		if (!event.getChannel().equalsIgnoreCase(LunarClientAPI.MESSAGE_CHANNEL)) {
			return;
		}
		
		Player player = event.getPlayer();
		
		lunarClientAPI.registerPlayer(player);
		
		lunarClientAPI.log("Player " + player.getName() + " (" + player.getUniqueId().toString() + ") has registered with Lunar Client channel");
		
		nitroLib.getServer().getPluginManager().callEvent(new LCPlayerRegisterEvent(event.getPlayer()));
		updateWorld(event.getPlayer());
	}
	
	@EventHandler
	public void onUnregister(PlayerUnregisterChannelEvent event) {
		if (event.getChannel().equalsIgnoreCase(LunarClientAPI.MESSAGE_CHANNEL)) {
			lunarClientAPI.log("Player " + event.getPlayer().getName() + " (" + event.getPlayer().getUniqueId().toString() + ") has unregistered with Lunar Client channel");
			lunarClientAPI.unregisterPlayer(event.getPlayer(), false);
		}
	}
	
	@EventHandler
	public void onUnregister(PlayerQuitEvent event) {
		lunarClientAPI.log("Player " + event.getPlayer().getName() + " (" + event.getPlayer().getUniqueId().toString() + ") has unregistered with Lunar Client channel");
		lunarClientAPI.unregisterPlayer(event.getPlayer(), true);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onWorldChange(PlayerChangedWorldEvent event) {
		updateWorld(event.getPlayer());
	}
	
	private void updateWorld(Player player) {
		String worldIdentifier = lunarClientAPI.getWorldIdentifier(player.getWorld());
		
		lunarClientAPI.sendPacket(player, new LCPacketUpdateWorld(worldIdentifier));
	}
}
