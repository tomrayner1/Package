package gbo.forever.spigot.async.entitytracker;

import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gbo.forever.spigot.async.AsyncUtil;
import gbo.forever.spigot.config.WindSpigotConfig;
import net.minecraft.server.EntityTracker;
import net.minecraft.server.WorldServer;

public class AsyncEntityTracker extends EntityTracker {
	
	private static final ExecutorService trackingThreadExecutor = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("WindSpigot Entity Tracker Thread").build());
	private final WorldServer worldServer;	
	
	public AsyncEntityTracker(WorldServer worldserver) {
		super(worldserver);
		this.worldServer = worldserver;
	}
	
	@Override
	public void updatePlayers() {	
		int offset = 0;
		
		for (int i = 1; i <= WindSpigotConfig.trackingThreads; i++) {
			final int finalOffset = offset++;
			
			AsyncUtil.run(() -> { // GBO - replace c with oldSet
				for (int index = finalOffset; index < oldSet.size(); index += WindSpigotConfig.trackingThreads) {
					oldSet.get(index).update();
				} // GBO - end
				worldServer.ticker.getLatch().decrement();

				setC(Sets.newHashSet(oldSet)); // GBO
			}, trackingThreadExecutor);
			
		}
	}

	public static ExecutorService getExecutor() {
		return trackingThreadExecutor;
	}
}
