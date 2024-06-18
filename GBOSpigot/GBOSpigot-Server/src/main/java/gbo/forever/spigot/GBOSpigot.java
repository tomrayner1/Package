package gbo.forever.spigot;

import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import gbo.forever.spigot.hitdetection.LagCompensator;
import gbo.forever.spigot.protocol.MovementListener;
import gbo.forever.spigot.protocol.PacketListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;

import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import gbo.forever.spigot.async.AsyncUtil;
import gbo.forever.spigot.async.pathsearch.SearchHandler;
import gbo.forever.spigot.async.thread.CombatThread;
import gbo.forever.spigot.commands.KnockbackCommand;
import gbo.forever.spigot.commands.MobAICommand;
import gbo.forever.spigot.commands.PingCommand;
import gbo.forever.spigot.commands.SpawnMobCommand;
import gbo.forever.spigot.config.WindSpigotConfig;
import gbo.forever.spigot.statistics.StatisticsClient;
import net.minecraft.server.MinecraftServer;
import gbo.forever.spigot.anticrash.AntiCrash;
import gbo.forever.spigot.async.AsyncExplosions;

public class GBOSpigot {

	private StatisticsClient client;
	
	public static final Logger LOGGER = LogManager.getLogger();
	private static final Logger DEBUG_LOGGER = LogManager.getLogger();
	private static GBOSpigot INSTANCE;
	
	private CombatThread knockbackThread;
	
	private final Executor statisticsExecutor = Executors
			.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("WindSpigot Statistics Thread")
			.build());
	
	private volatile boolean statisticsEnabled = false;
	
	private LagCompensator lagCompensator;
	
	private final Set<PacketListener> packetListeners = Sets.newConcurrentHashSet();
	private final Set<MovementListener> movementListeners = Sets.newConcurrentHashSet();

	public GBOSpigot() {
		INSTANCE = this;
		this.init();
	}

	public void reload() {
		this.init();
	}

	private void initCmds() {
		
		SimpleCommandMap commandMap = MinecraftServer.getServer().server.getCommandMap();
		
		if (WindSpigotConfig.mobAiCmd) {
			MobAICommand mobAiCommand = new MobAICommand("mobai");
			commandMap.register(mobAiCommand.getName(), "gbospigot", mobAiCommand);
		}
		
		if (WindSpigotConfig.pingCmd) {
			PingCommand pingCommand = new PingCommand("ping");
			commandMap.register(pingCommand.getName(), "gbospigot", pingCommand);
		}
		// GBO spigot - Remove setmaxslots command
		SpawnMobCommand spawnMobCommand = new SpawnMobCommand("spawnmob");
		commandMap.register(spawnMobCommand.getName(), "gbospigot", spawnMobCommand);

		KnockbackCommand knockbackCommand = new KnockbackCommand("kb");
		commandMap.register(knockbackCommand.getName(), "gbospigot", knockbackCommand);
	}

	private void initStatistics() {
		if (WindSpigotConfig.statistics && !statisticsEnabled) {
			Runnable statisticsRunnable = (() -> {
				client = new StatisticsClient();
				try {
					statisticsEnabled = true;

					if (!client.isConnected) {
						// Connect to the statistics server and notify that there is a new server
						client.start("150.230.35.78", 500);
						client.sendMessage("new server");

						while (true) {
							// Keep alive, this tells the statistics server that this server
							// is still online
							client.sendMessage("keep alive packet");

							// Online players, this tells the statistics server how many players
							// are on
							client.sendMessage("player count packet " + Bukkit.getOnlinePlayers().size());

							// Statistics are sent every 40 secs.
							TimeUnit.SECONDS.sleep(40);
						}

					}
				} catch (Exception ignored) {}
			});
			AsyncUtil.run(statisticsRunnable, statisticsExecutor);
		}
	}

	private void init() {
		initCmds();
		initStatistics();
		
		// We do not want to initialize this again after a reload
		if (WindSpigotConfig.asyncPathSearches && SearchHandler.getInstance() == null) {
			new SearchHandler();
		}
		
		if (WindSpigotConfig.asyncKnockback) {
			knockbackThread = new CombatThread("Knockback Thread");
		}
		lagCompensator = new LagCompensator();	
		if (WindSpigotConfig.asyncTnt) {
			AsyncExplosions.initExecutor(WindSpigotConfig.fixedPoolSize);
		}
		if (WindSpigotConfig.enableAntiCrash) {
			registerPacketListener(new AntiCrash());
		}
	}

	public StatisticsClient getClient() {
		return this.client;
	}
	
	public CombatThread getKnockbackThread() {
		return knockbackThread;
	}
	
    public LagCompensator getLagCompensator() {
        return lagCompensator;
    }
    
	public static void debug(String msg) {
		if (WindSpigotConfig.debugMode)
			DEBUG_LOGGER.info(msg);
	}
	
	public void registerPacketListener(PacketListener packetListener) {
		this.packetListeners.add(packetListener);
	}

	public void unregisterPacketListener(PacketListener packetListener) {
		this.packetListeners.remove(packetListener);
	}

	public Set<PacketListener> getPacketListeners() {
		return this.packetListeners;
	}

	public void registerMovementListener(MovementListener movementListener) {
		this.movementListeners.add(movementListener);
	}

	public void unregisterMovementListener(MovementListener movementListener) {
		this.movementListeners.remove(movementListener);
	}

	public Set<MovementListener> getMovementListeners() {
		return this.movementListeners;
	}
	
	public static GBOSpigot getInstance() {
		return INSTANCE;
	}
}
