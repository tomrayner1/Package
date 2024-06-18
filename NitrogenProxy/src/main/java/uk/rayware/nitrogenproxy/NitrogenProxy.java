package uk.rayware.nitrogenproxy;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import lombok.Getter;
import org.apache.commons.configuration2.YAMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import uk.rayware.nitrogenproxy.balancer.HubBalancer;
import uk.rayware.nitrogenproxy.commands.HubCommand;
import uk.rayware.nitrogenproxy.config.NitrogenConfig;
import uk.rayware.nitrogenproxy.listener.NitrogenListeners;
import uk.rayware.nitrogenproxy.listener.PacketHandler;
import uk.rayware.nitroproxy.pyrite.Pyrite;
import uk.rayware.nitroproxy.pyrite.PyriteCredentials;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Getter
@Plugin(id = "nitrogenproxy", name = "NitrogenProxy", version = "1.0", authors = {"tomGBO"})
public class NitrogenProxy {

	private final NitrogenProxy nitrogenProxy;

	public final ProxyServer server;
	public final Logger logger;
	private final Path dataDirectory;

	private final File configFile;
	public final YAMLConfiguration config;

	private final NitrogenConfig nitrogenConfig;

	public final JedisPool jedisPool;
	public final Pyrite pyrite;

	public final HashMap<String, RegisteredServer> lobbies = new HashMap<>();
	private ScheduledTask retrieveLobbiesTask;

	@Inject
	public NitrogenProxy(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) throws IOException {
		nitrogenProxy = this;

		this.server = server;
		this.logger = logger;
		this.dataDirectory = dataDirectory;

		if (!dataDirectory.toFile().exists()) {
			dataDirectory.toFile().mkdir();
		}

		configFile = new File(dataDirectory + "/config.yml");

		if (!configFile.exists()) {
			configFile.createNewFile();
		}

		config = new YAMLConfiguration();

		try (FileReader in = new FileReader(configFile)) {
			config.read(in);
		} catch (IOException | ConfigurationException e) {
			e.printStackTrace();
		}

		nitrogenConfig = new NitrogenConfig(nitrogenProxy);

		String host = nitrogenConfig.getRedisHost();
		int port = nitrogenConfig.getRedisPort();

		PyriteCredentials pyriteCredentials = new PyriteCredentials(host, null, port);

		jedisPool = new JedisPool(host, port);

		if (nitrogenConfig.getRedisAuth()) {
			String pass = nitrogenConfig.getRedisPassword();
			pyriteCredentials.setPassword(pass);

			try (Jedis jedis = jedisPool.getResource()) {
				jedis.auth(pass);
			}
		}

		pyrite = new Pyrite(pyriteCredentials);

		pyrite.registerContainer(new PacketHandler(this));
	}

	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event) {
		server.getEventManager().register(this, new NitrogenListeners(this));
		server.getEventManager().register(this, new HubBalancer(this));

		CommandMeta commandMeta = server.getCommandManager().metaBuilder("hub").aliases("lobby", "wheezyouttahere").build();
		server.getCommandManager().register(commandMeta, new HubCommand(this));

		retrieveLobbiesTask = server.getScheduler().buildTask(nitrogenProxy, this::retrieveLobbies).repeat(5L, TimeUnit.SECONDS).schedule();
	}

	@Subscribe
	public void close(ProxyShutdownEvent event) {
		retrieveLobbiesTask.cancel();
	}

	public void saveConfig() {
		try (FileWriter out = new FileWriter(configFile)) {
			config.write(out);
		} catch (IOException | ConfigurationException e) {
			e.printStackTrace();
		}
	}

	private void retrieveLobbies() {
		Set<String> servers = new HashSet<>();

		try (Jedis jedis = jedisPool.getResource()) {
			Set<String> response = new HashSet<>(jedis.keys("nitrogen:server:*"));

			response.forEach(r -> {
				Map<String, String> data = jedis.hgetAll(r);

				if (!data.isEmpty() && data.get("platform").equals("bukkit") && data.get("online").equals("true") &&
						Long.parseLong(data.get("updated")) + (5 * 1000) > System.currentTimeMillis() && data.get("group").equals(nitrogenConfig.getHubGroup())) {
					servers.add(r.replace("nitrogen:server:", ""));
				}
			});
		}

		servers.forEach(server -> getServer().getServer(server).ifPresent(lobbyServer -> lobbies.put(server, lobbyServer)));
	}

}
