package uk.rayware.gboqueue.task

import org.bukkit.Bukkit
import uk.rayware.gboqueue.QueuePlugin
import uk.rayware.nitrogen.NitrogenAPI
import uk.rayware.nitrogen.network.SendPlayerPacket
import java.util.*

class ManageQueueTask : Runnable {

    override fun run() {
        QueuePlugin.jedisPool.resource.use { jedis ->
            val keys = jedis.keys("nitrogen:queue:*")

            if (keys.isEmpty())
                return

            keys.forEach { queueId ->
                val firstInQueue = jedis.lindex(queueId, -1)
                val first = UUID.fromString(firstInQueue)

                val serverId = queueId.substringAfterLast(":")

                val data = NitrogenAPI.getStoredServerData(serverId)

                if (QueuePlugin.playerHandler.queuePlayers.containsKey(first)) {
                    val player = Bukkit.getPlayer(first)

                    if (player == null || !player.isOnline) {
                        return@forEach
                    }

                    val whitelisted = data["whitelisted"] == "true"
                    val online = data["online"] == "true"
                    val outdated = (data["updated"]!!.toLong()) + (10 * 1000) < System.currentTimeMillis()

                    if (!whitelisted && online && !outdated) {
                        NitrogenAPI.pyrite.sendPacket(SendPlayerPacket(player.name, serverId), "Nitrogen")
                        QueuePlugin.leaveQueue(player, serverId)
                        QueuePlugin.playerHandler.queuePlayers.values.forEach { it.queues[serverId] = it.queues[serverId]!! - 1 }
                    }
                }
            }
        }
    }

}