package gbo.forever.spigot.async.pathsearch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import gbo.forever.spigot.async.AsyncUtil;
import gbo.forever.spigot.async.pathsearch.cache.SearchCacheEntryEntity;
import gbo.forever.spigot.async.pathsearch.cache.SearchCacheEntryPosition;
import gbo.forever.spigot.config.WindSpigotConfig;
import net.minecraft.server.ChunkCache;
import net.minecraft.server.Entity;
import net.minecraft.server.MathHelper;
import net.minecraft.server.PathEntity;

public class SearchHandler {

	private static SearchHandler INSTANCE;
	private final ExecutorService executor = Executors.newFixedThreadPool(WindSpigotConfig.pathSearchThreads,
			new ThreadFactoryBuilder().setNameFormat("WindSpigot Entity Path Search Thread %d").build());

	public SearchHandler() {
		INSTANCE = this;
	}

	public void issueSearch(Entity targetEntity, AsyncNavigation navigation) {
		
		final ChunkCache chunkCache = navigation.createChunkCache(true);
		
		if (chunkCache == null) {
			return;
		}

		navigation.isSearching.set(true);
		
		final int finalX = MathHelper.floor(targetEntity.locX);
		final int finalY = MathHelper.floor(targetEntity.locY) + 1;
		final int finalZ = MathHelper.floor(targetEntity.locZ);
		
		AsyncUtil.run(() -> {
			
			PathEntity path = navigation.doPathSearch(chunkCache, finalX, finalY, finalZ);
			SearchCacheEntryEntity cache = new SearchCacheEntryEntity(targetEntity, navigation.getEntity(), path);

			navigation.addEntry(cache);
			
			navigation.isSearching.set(false);

		}, executor);
	}

	public static SearchHandler getInstance() {
		return INSTANCE;
	}

	public void issueSearch(int x, int y, int z, AsyncNavigation navigation) {

		final ChunkCache chunkCache = navigation.createChunkCache(false);
		
		if (chunkCache == null) {
			return;
		}
		
		navigation.isSearching.set(true);
		
		AsyncUtil.run(() -> {
			
			PathEntity path = navigation.doPathSearch(chunkCache, x, y, z);
			SearchCacheEntryPosition cache = new SearchCacheEntryPosition(x, y, z, navigation.getEntity(), path);

			navigation.addEntry(cache);
			
			navigation.isSearching.set(false);

		}, executor);
	}

}
