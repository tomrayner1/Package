package uk.rayware.guard.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import uk.rayware.guard.check.Check;

import java.util.*;

@Getter
@RequiredArgsConstructor
@Setter
public class PlayerData {

	private final UUID uuid;
	private Set<Check> checks = new HashSet<>();

	private boolean banWaiting;

	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}

}
