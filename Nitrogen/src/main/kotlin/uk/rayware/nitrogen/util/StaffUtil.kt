package uk.rayware.nitrogen.util

import org.bukkit.Bukkit
import uk.rayware.nitrogen.Nitrogen

class StaffUtil {

    companion object {
        @JvmStatic
        fun messageStaff(message: String) {
            Nitrogen.nitrogen.profileHandler.profileMap.values.forEach {
                val player = it.getPlayer()
                if (it.staffMessages && player != null && player.hasPermission("nitrogen.staff")) {
                    player.sendMessage(message)
                }
            }

            Bukkit.getConsoleSender().sendMessage(message)
        }
    }

}