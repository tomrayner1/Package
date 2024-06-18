package uk.rayware.nitrolib.economy;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import uk.rayware.nitrolib.NitroLib;
import uk.rayware.nitrolib.economy.cmds.BalCommand;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class EconomyHandler implements Listener {

	@Getter
	private final NitroEconomy nitroEconomy = new NitroEconomy();
	private File file;
	@Getter
	private static FileConfiguration config;
	@Getter
	private boolean enabled = false;

	public EconomyHandler(NitroLib plugin)
	{
		Plugin vault = plugin.getServer().getPluginManager().getPlugin("Vault");

		if (vault == null)
		{
			return;
		}

		setupFile( plugin );
		setupRunnable( plugin );

		Bukkit.getServicesManager().register(Economy.class, nitroEconomy, vault, ServicePriority.Normal);

		plugin.getNitroCommandMap().register("nitrolib", new BalCommand());

		enabled = true;
	}

	@EventHandler
	public void join(PlayerJoinEvent event)
	{
		UUID uuid = event.getPlayer().getUniqueId();

		if (config.isSet(uuid + ".bal"))
		{
			nitroEconomy.update(uuid, config.getDouble(uuid + ".bal"));
		}
		else
		{
			nitroEconomy.update(uuid, 500D);
		}
	}

	private void setupRunnable(NitroLib plugin)
	{
		plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::save, 1, 300);
	}

	public void save()
	{
		nitroEconomy.playerBal.forEach((uuid, aDouble) ->
		{
			config.set(uuid + ".bal", aDouble);
		});

		try
		{
			config.save(file);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void setupFile(NitroLib plugin)
	{
		file = new File(plugin.getDataFolder(), "economy.yml");

		if (!file.exists())
		{
			file.getParentFile().mkdirs();
			plugin.saveResource("economy.yml", false);
		}

		config = new YamlConfiguration();

		try
		{
			config.load(file);
		}
		catch (IOException | InvalidConfigurationException e)
		{
			e.printStackTrace();
		}
	}

}
