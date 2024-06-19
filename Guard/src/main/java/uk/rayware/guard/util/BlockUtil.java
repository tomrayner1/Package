package uk.rayware.guard.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashSet;

@UtilityClass
public class BlockUtil {

	private final HashSet<Material> CLIMBABLE = new HashSet<>(Arrays.asList(Material.LADDER, Material.VINE));
	private final HashSet<Material> LIQUID = new HashSet<>(Arrays.asList(Material.WATER, Material.STATIONARY_WATER, Material.LAVA, Material.STATIONARY_LAVA));
	private final HashSet<Material> AIR = new HashSet<>(Arrays.asList(Material.AIR, Material.STONE_BUTTON, Material.WOOD_BUTTON, Material.LONG_GRASS, Material.LEVER));

	public boolean containsClimbable(World world, Vector[] vectors) {
		for (Vector vector : vectors) {
			Block block = world.getBlockAt(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());

			if (CLIMBABLE.contains(block.getType())) {
				return true;
			}
		}

		return false;
	}

	public boolean containsLiquid(World world, Vector[] vectors) {
		for (Vector vector : vectors) {
			Block block = world.getBlockAt(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());

			if (LIQUID.contains(block.getType())) {
				return true;
			}
		}

		return false;
	}

	public boolean containsSolid(World world, Vector[] vectors) {
		for (Vector vector : vectors) {
			Block block = world.getBlockAt(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());

			if (LIQUID.contains(block.getType())) {
				return false;
			}

			if (!AIR.contains(block.getType())) {
				return true;
			}
		}

		return false;
	}

}
