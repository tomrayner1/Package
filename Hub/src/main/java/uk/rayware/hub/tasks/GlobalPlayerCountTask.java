package uk.rayware.hub.tasks;

import uk.rayware.hub.HubPlugin;
import uk.rayware.nitrogen.NitrogenAPI;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class GlobalPlayerCountTask implements Runnable {

	@Override
	public void run() {
		Set<String> ids = NitrogenAPI.getAllNitrogenServerIdentifiers();

		AtomicInteger count = new AtomicInteger(0);

		ids.forEach(id -> {
			HashMap<String, String> data = NitrogenAPI.getNitrogenServerData(id);

			if (data.get("platform").equals("bukkit") && Long.parseLong(data.get("updated")) + 10000 > System.currentTimeMillis()) {
				count.addAndGet(Integer.parseInt(data.get("players")));
			}
		});

		HubPlugin.getInstance().setGlobalCount(count.get());
	}

}
