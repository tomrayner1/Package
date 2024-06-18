package uk.rayware.gboqueue.player

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import uk.rayware.gboqueue.QueuePlugin
import java.util.*
import kotlin.collections.HashMap

class QueuePlayerHandler(queuePlugin: QueuePlugin) : Listener {

    val queuePlayers: HashMap<UUID, QueuePlayer> = HashMap()

    init {
        queuePlugin.server.pluginManager.registerEvents(this, queuePlugin)
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        if (!queuePlayers.containsKey(event.player.uniqueId)) {
            queuePlayers[event.player.uniqueId] = QueuePlayer()
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        queuePlayers[event.player.uniqueId]!!.queues.keys.forEach {queue ->
            QueuePlugin.jedisPool.resource.use {jedis ->
                jedis.lrem("nitrogen:queue:${queue}", 0, event.player.uniqueId.toString())
            }
        }

        queuePlayers.remove(event.player.uniqueId)
    }

}