package uk.rayware.nitrogenproxy.config

import uk.rayware.nitrogenproxy.NitrogenProxy

class NitrogenConfig(proxy: NitrogenProxy) {

    var redisHost: String = "127.0.0.1"
    var redisPort: Int = 6379
    var redisAuth: Boolean = false
    var redisPassword: String = ""
    var hubGroup: String = "hubs"

    init {
        val config = proxy.config

        if (config.containsKey("redis.host")) {
            redisHost = config.getString("redis.host")
        } else {
            config.addProperty("redis.host", "127.0.0.1")
        }

        if (config.containsKey("redis.port")) {
            redisPort = config.getInt("redis.port")
        } else {
            config.addProperty("redis.port", 6379)
        }

        if (config.containsKey("redis.auth.enabled")) {
            redisAuth = config.getBoolean("redis.auth.enabled")
        } else {
            config.addProperty("redis.auth.enabled", false)
        }

        if (config.containsKey("redis.auth.password")) {
            redisPassword = config.getString("redis.auth.password")
        } else {
            config.addProperty("redis.auth.password", "")
        }

        if (config.containsKey("hub-group")) {
            hubGroup = config.getString("hub-group")
        } else {
            config.addProperty("hub-group", "hubs")
        }

        proxy.saveConfig()
    }

}