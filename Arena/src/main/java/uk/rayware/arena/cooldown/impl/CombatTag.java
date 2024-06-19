package uk.rayware.arena.cooldown.impl;

import lombok.Getter;
import lombok.Setter;
import uk.rayware.arena.cooldown.Cooldown;
import java.util.HashMap;
import java.util.UUID;

public class CombatTag {

	private static final HashMap<UUID, Cooldown> combatTags = new HashMap<>();
	@Getter @Setter
	private static double duration;

	public static Cooldown get(UUID uuid) {
		if (!combatTags.containsKey(uuid)) {
			Cooldown cd = new Cooldown((long) (duration * 1000L));
			cd.end();

			combatTags.put(uuid, cd);
		}
		return combatTags.get(uuid);
	}

}
