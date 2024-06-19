package uk.rayware.arena.kit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import uk.rayware.arena.lobby.LobbyItems;

import java.util.List;

@AllArgsConstructor
@Getter
public abstract class Kit implements IKit {

	private String name;
	private Material icon;
	private List<String> description;
	private int cost;

	public abstract void giveKitToPlayer(Player player);

	@Override
	public void apply(Player player) {
		if (LobbyItems.hasLobbyItems.remove(player.getUniqueId())) {
			player.getInventory().clear();
		}

		giveKitToPlayer(player);
	}

	@Override
	public boolean equals(Object object) {
		return (object instanceof Kit) && ((Kit) object).getName().equalsIgnoreCase(name);
	}
}
