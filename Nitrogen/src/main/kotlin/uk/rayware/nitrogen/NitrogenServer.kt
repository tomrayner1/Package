package uk.rayware.nitrogen

import org.bukkit.Bukkit
import uk.rayware.nitrogen.network.ServerOfflinePacket
import java.util.logging.Level

class NitrogenServer(
    val nitrogen: Nitrogen,
    val identifier: String,
    val displayName: String,
    val group: String) {

    var valid: Boolean

    private val customData: HashMap<String, String> = HashMap()

    private val queryHash = "nitrogen:server:${identifier}"

    init {
        valid = validate()
    }

    fun setCustomData(key: String, value: String) {
        if (customData.containsKey("_$key")) {
            customData.replace("_$key", value)
        } else {
            customData["_$key"] = value
        }
    }

    fun getCustomData(key: String): String? {
        return customData["_$key"]
    }

    fun heartbeat() {
        try {
            NitrogenAPI.jedisPool.resource.use { jedis ->
                jedis.hset(queryHash, "platform", "bukkit")
                jedis.hset(queryHash, "name", identifier)
                jedis.hset(queryHash, "display", displayName)
                jedis.hset(queryHash, "group", group)
                jedis.hset(queryHash, "online", true.toString())
                jedis.hset(queryHash, "players", nitrogen.server.onlinePlayers.size.toString())
                jedis.hset(queryHash, "max", nitrogen.server.maxPlayers.toString())
                jedis.hset(queryHash, "updated", System.currentTimeMillis().toString())
                jedis.hset(queryHash, "whitelisted", nitrogen.server.hasWhitelist().toString())

                customData.forEach { entry ->
                    jedis.hset(queryHash, entry.key, entry.value)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun validate(): Boolean {
        NitrogenAPI.jedisPool.resource.use { jedis ->
            if (jedis.hgetAll(queryHash).isEmpty()) {
                return true
            }

            if (jedis.hget(queryHash, "online") == "false") {
                return true
            }

            if (jedis.hget(queryHash, "updated").toLong() + (15 * 1000) < System.currentTimeMillis()) {
                return true
            }
        }
        
        return false
    }

    fun setOffline() {
        NitrogenAPI.pyrite.sendPacket(ServerOfflinePacket(identifier), "Nitrogen")

        NitrogenAPI.jedisPool.resource.use { jedis ->
            jedis.hset(queryHash, "online", false.toString())
            jedis.hset(queryHash, "players", "0")
        }
    }

    fun runCommand(server: NitrogenServer, command: String, global: Boolean) {
        Bukkit.getServer().logger.log(Level.INFO,
            "[${server.identifier} from ${server.group}] Issued command '${command}'" +
                    if (global) " on ALL servers" else "" + ".")
        //TODO: run cmd
    }

}