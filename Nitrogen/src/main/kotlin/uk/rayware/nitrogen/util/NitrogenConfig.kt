package uk.rayware.nitrogen.util

import org.bukkit.configuration.file.FileConfiguration

class NitrogenConfig(config: FileConfiguration) {

    val updateTabName: Boolean

    init {
        updateTabName = config.getBoolean("config.update-tab-name")
    }

}