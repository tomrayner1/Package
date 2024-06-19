package uk.rayware.guard;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;
import uk.rayware.guard.command.GuardCommand;
import uk.rayware.guard.listener.PlayerListener;
import uk.rayware.guard.packet.PacketListener;
import uk.rayware.guard.player.PlayerDataHandler;

@Getter
public class Guard extends JavaPlugin {

	@Getter
	private static Guard guard;

	private PlayerDataHandler dataHandler;
	private BukkitCommandHandler commandHandler;

	@Override
	public void onLoad() {
		PacketEvents.create(this).getSettings().fallbackServerVersion(ServerVersion.v_1_8_3).checkForUpdates(true);
		PacketEvents.get().load();
	}

	@Override
	public void onEnable() {
		guard = this;

		dataHandler = new PlayerDataHandler(this);
		commandHandler = BukkitCommandHandler.create(this);
		commandHandler.register(new GuardCommand());

		PacketEvents.get().init();
		PacketEvents.get().registerListener(new PacketListener(this));

		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
	}

	@Override
	public void onDisable() {
		PacketEvents.get().terminate();
	}

}
