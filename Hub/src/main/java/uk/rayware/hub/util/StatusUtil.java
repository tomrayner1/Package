package uk.rayware.hub.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.Map;

@UtilityClass
public class StatusUtil {

	public String getStatus(Map<String, String> data) {
		if (Long.parseLong(data.get("updated")) + (10 * 1000) > System.currentTimeMillis() && data.get("online").equals("true")) {
			if (data.get("whitelisted").equals("true")) {
				return ChatColor.RED + "Whitelisted";
			} else {
				return ChatColor.GREEN + "Online";
			}
		} else {
			return ChatColor.RED + "Offline";
		}
	}

}
