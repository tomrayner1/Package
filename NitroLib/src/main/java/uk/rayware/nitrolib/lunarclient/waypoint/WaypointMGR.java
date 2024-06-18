package uk.rayware.nitrolib.lunarclient.waypoint;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import uk.rayware.nitrolib.lunarclient.LunarClientAPI;
import uk.rayware.nitrolib.lunarclient.object.LCWaypoint;

import java.util.HashMap;

public class WaypointMGR {
	
	@Getter
	public HashMap<String, LCWaypoint> Waypoints;
	LunarClientAPI main;
	
	public WaypointMGR(LunarClientAPI main) {
		this.main = main;
		Waypoints = new HashMap<>();
	}
	
	public EWaypointMGR addWaypoint(String name, int x, int y, int z, String world, int color, boolean forced, boolean visible) {
		if (Waypoints.containsKey(name)) {
			return EWaypointMGR.ALREADY_EXISTS;
		}
		Waypoints.put(name, new LCWaypoint(name, x, y, z, world, color, forced, visible));
		this.broadcastWaypoint(name);
		return EWaypointMGR.ADDED;
	}
	
	public EWaypointMGR addWaypoint(String name, int color, Location location) {
		if (Waypoints.containsKey(name)) {
			return EWaypointMGR.ALREADY_EXISTS;
		}
		Waypoints.put(name, new LCWaypoint(name, location, color , false, true));
		this.broadcastWaypoint(name);
		return EWaypointMGR.ADDED;
	}
	
	public EWaypointMGR addWaypoint(String name, int color, Location location, boolean forced, boolean visible) {
		if (Waypoints.containsKey(name)) {
			return EWaypointMGR.ALREADY_EXISTS;
		}
		Waypoints.put(name, new LCWaypoint(name, location, color , forced, visible));
		this.broadcastWaypoint(name);
		return EWaypointMGR.ADDED;
	}
	
	public EWaypointMGR addWaypoint(String name, int color, Location location, boolean forced) {
		if (Waypoints.containsKey(name)) {
			return EWaypointMGR.ALREADY_EXISTS;
		}
		Waypoints.put(name, new LCWaypoint(name, location, color , forced, true));
		this.broadcastWaypoint(name);
		return EWaypointMGR.ADDED;
	}
	
	public EWaypointMGR setForced(boolean forced, String name) {
		if (!Waypoints.containsKey(name)) {
			return EWaypointMGR.NOT_FOUND;
		}
		Waypoints.get(name).setForced(forced);
		this.broadcastWaypoint(name);
		return EWaypointMGR.AMENDED;
	}
	
	public EWaypointMGR setVisible(boolean visible, String name) {
		if (!Waypoints.containsKey(name)) {
			return EWaypointMGR.NOT_FOUND;
		}
		Waypoints.get(name).setVisible(visible);
		this.broadcastWaypoint(name);
		return EWaypointMGR.AMENDED;
	}
	
	public void sendAddWaypointTo(Player player, String name) {
		if (Waypoints.containsKey(name)) {
			main.sendWaypoint(player, Waypoints.get(name));
		}
	}
	
	public void sendRemoveWaypointTo(Player player, String name) {
		if (Waypoints.containsKey(name)) {
			main.removeWaypoint(player, Waypoints.get(name));
		}
	}
	
	public void broadcastWaypoint(String name) {
		if (Waypoints.containsKey(name)) {
			main.broadcastWaypoint(Waypoints.get(name));
		}
	}
	
	public EWaypointMGR removeWaypoint(String name) {
		if (Waypoints.containsKey(name)) {
			Waypoints.remove(name);
			return EWaypointMGR.REMOVED;
		} else return EWaypointMGR.NOT_FOUND;
	}
	
	public LCWaypoint getWaypoint(String name) {
		return Waypoints.get(name);
	}
}
