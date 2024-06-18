package uk.rayware.hub.util;

import lombok.experimental.UtilityClass;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.HologramTrait;
import org.bukkit.ChatColor;
import uk.rayware.nitrogen.NitrogenAPI;

import java.util.Map;

@UtilityClass
public class NPCUtil {

	public void updateHologram(NPC npc) {
		Map<String, String> data = NitrogenAPI.getStoredServerData(npc.data().get("nitrogen-server-name"));

		npc.getTraitNullable(HologramTrait.class).setLine(1, ChatColor.WHITE + "Status: " + StatusUtil.getStatus(data));
		npc.getTraitNullable(HologramTrait.class).setLine(2, ChatColor.WHITE + "Players: " + ChatColor.YELLOW +
				data.get("players") + ChatColor.WHITE + "/" + ChatColor.YELLOW + data.get("max"));
	}

}
