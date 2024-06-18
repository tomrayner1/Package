package uk.rayware.hub.npc;

import lombok.Getter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.CommandTrait;
import net.citizensnpcs.trait.HologramTrait;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import uk.rayware.hub.HubPlugin;
import uk.rayware.nitrogen.NitrogenAPI;

import java.util.Map;

@Getter
public class ServerNPC {

	private final NPC npc;

	public ServerNPC(String serverId, String displayName, Location location, String skinName) {
		npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, displayName);
		npc.spawn(location);

		npc.data().setPersistent("nitrogen-server-name", serverId);
		npc.data().setPersistent("nitrogen-displayname-name", displayName);
		npc.data().setPersistent("nameplate-visible", false);

		npc.getOrAddTrait(HologramTrait.class).addLine(ChatColor.GOLD + "" + ChatColor.BOLD + displayName);
		npc.getOrAddTrait(HologramTrait.class).addLine(ChatColor.WHITE + "Status: " + ChatColor.YELLOW + "Loading...");
		npc.getOrAddTrait(HologramTrait.class).addLine(ChatColor.WHITE + "Players: " + ChatColor.YELLOW + "0");
		npc.getOrAddTrait(HologramTrait.class).addLine(ChatColor.GREEN + "Click to join the queue");

		npc.getOrAddTrait(HologramTrait.class).setDirection(HologramTrait.HologramDirection.TOP_DOWN);
		npc.getOrAddTrait(HologramTrait.class).setLineHeight(0.25);

		npc.getOrAddTrait(CommandTrait.class)
				.addCommand(new CommandTrait.NPCCommandBuilder("joinqueue " + serverId, CommandTrait.Hand.BOTH)
						.globalCooldown(-1).cooldown(-1).player(true));

		npc.getOrAddTrait(SkinTrait.class).setSkinName(skinName);
		npc.getOrAddTrait(SkinTrait.class).setShouldUpdateSkins(false);

		HubPlugin.getInstance().getOwningNPCs().put(displayName, npc.getId());
		HubPlugin.getInstance().saveNPCs();
	}

}
