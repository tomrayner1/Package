package uk.rayware.arena.command;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import uk.rayware.arena.Arena;
import uk.rayware.arena.ArenaLocale;

public class UpdateSpawnCommand {

	@Command("updatespawn")
	@CommandPermission("arena.command.updatespawn")
	public void claim(Player sender) {
		Selection selection;

		try {
			selection = ((WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit")).getSelection(sender);
		} catch (Exception e) {
			sender.sendMessage(ArenaLocale.ERROR);
			return;
		}

		if (selection == null) {
			sender.sendMessage(ArenaLocale.NO_SELECTION);
			return;
		}

		World world = selection.getWorld();
		Location p1 = selection.getMinimumPoint();
		Location p2 = selection.getMaximumPoint();

		p2.add(1, 0, 1);

		Arena.getInstance().getLobbyHandler().setSpawn(new CuboidSelection(world, p1, p2));

		Arena.getInstance().getConfig().set("spawn.world", world.getName());
		Arena.getInstance().getConfig().set("spawn.p1.x", p1.getX());
		Arena.getInstance().getConfig().set("spawn.p1.y", p1.getY());
		Arena.getInstance().getConfig().set("spawn.p1.z", p1.getZ());
		Arena.getInstance().getConfig().set("spawn.p2.x", p2.getX());
		Arena.getInstance().getConfig().set("spawn.p2.y", p2.getY());
		Arena.getInstance().getConfig().set("spawn.p2.z", p2.getZ());
		Arena.getInstance().saveConfig();

		sender.sendMessage(ArenaLocale.CLAIMED_SPAWN);
	}

}
