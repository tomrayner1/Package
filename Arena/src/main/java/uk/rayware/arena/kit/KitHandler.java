package uk.rayware.arena.kit;

import lombok.Getter;
import uk.rayware.arena.kit.impl.PvPKit;

import java.util.HashSet;
import java.util.Set;

@Getter
public class KitHandler {

	private final Set<Kit> kits = new HashSet<>();
	private final Kit defaultKit = new PvPKit();

	public KitHandler() {
		kits.add(new PvPKit());
	}

	public Kit getByName(String kitName) {
		for (Kit kit : kits) {
			if (kit.getName().equalsIgnoreCase(kitName)) {
				return kit;
			}
		}
		return null;
	}

}
