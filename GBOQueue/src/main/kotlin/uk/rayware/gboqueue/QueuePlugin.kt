package uk.rayware.gboqueue

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import redis.clients.jedis.JedisPool
import redis.clients.jedis.args.ListPosition
import uk.rayware.gboqueue.commands.AllowQueueCommand
import uk.rayware.gboqueue.commands.JoinQueueCommand
import uk.rayware.gboqueue.commands.LeaveQueueCommand
import uk.rayware.gboqueue.player.QueuePlayerHandler
import uk.rayware.gboqueue.task.ManageQueueTask
import uk.rayware.gboqueue.task.UpdatePositionTask
import uk.rayware.gboqueue.util.QueueResult
import uk.rayware.nitrogen.NitrogenAPI
import uk.rayware.nitrolib.NitroLib
import java.util.*

class QueuePlugin : JavaPlugin() {

    override fun onEnable() {
        queuePlugin = this

        jedisPool = NitrogenAPI.jedisPool

        playerHandler = QueuePlayerHandler(this)

        listOf(
            JoinQueueCommand(),
            LeaveQueueCommand(),
            AllowQueueCommand()
        ).forEach { command -> NitroLib.getInstance().nitroCommandMap.register("gboqueue", command) }

        server.scheduler.runTaskTimerAsynchronously(queuePlugin, ManageQueueTask(), 0, 5)
        server.scheduler.runTaskTimerAsynchronously(queuePlugin, UpdatePositionTask(), 0, 20)
    }

    override fun onDisable() {
        server.scheduler.cancelTasks(queuePlugin)
        playerHandler.queuePlayers.forEach { (uuid, player) -> player.queues.keys.forEach { queue -> leaveQueue(uuid.toString(), queue) } }
    }

    companion object {
        lateinit var queuePlugin: QueuePlugin

        lateinit var jedisPool: JedisPool
        lateinit var playerHandler: QueuePlayerHandler

        @JvmStatic
        fun joinQueue(player: Player, queue: String): QueueResult {
            if (!NitrogenAPI.getAllNitrogenServerIdentifiers().contains(queue)) {
                return QueueResult.NOT_FOUND
            }

            if (NitrogenAPI.getStoredServerData(queue)["_allowgboqueue"] == "false") {
                return QueueResult.ERROR
            }

            val uuid = player.uniqueId.toString()

            jedisPool.resource.use { jedis ->
                val weight = jedis.hget("nitrogen:profiles:weight", uuid).toInt()

                if (jedis.llen("nitrogen:queue:${queue}").toInt() == 0) {
                    jedis.rpush("nitrogen:queue:${queue}", uuid)

                    if (playerHandler.queuePlayers.containsKey(UUID.fromString(uuid)))
                        playerHandler.queuePlayers[UUID.fromString(uuid)]?.queues?.put(queue, jedis.lpos("nitrogen:queue:${queue}", uuid).toInt())

                    return QueueResult.SUCCESS
                } else {
                    val queuedUUIDS = jedis.lrange("nitrogen:queue:${queue}", 0, -1)

                    if (queuedUUIDS.contains(uuid)) {
                        return QueueResult.ALREADY_IN
                    }

                    for (storedUUID in queuedUUIDS) {
                        if (jedis.hget("nitrogen:profiles:weight", storedUUID).toInt() < weight) {
                            jedis.linsert("nitrogen:queue:${queue}", ListPosition.BEFORE, storedUUID, uuid)

                            if (playerHandler.queuePlayers.containsKey(UUID.fromString(uuid)))
                                playerHandler.queuePlayers[UUID.fromString(uuid)]?.queues?.put(queue, jedis.lpos("nitrogen:queue:${queue}", uuid).toInt())

                            return QueueResult.SUCCESS
                        }
                    }

                    jedis.rpush("nitrogen:queue:${queue}", uuid)

                    if (playerHandler.queuePlayers.containsKey(UUID.fromString(uuid)))
                        playerHandler.queuePlayers[UUID.fromString(uuid)]?.queues?.put(queue, jedis.lpos("nitrogen:queue:${queue}", uuid).toInt())

                    return QueueResult.SUCCESS
                }
            }
        }

        @JvmStatic
        fun leaveQueue(player: Player, queue: String): QueueResult {
            return leaveQueue(player.uniqueId.toString(), queue)
        }

        @JvmStatic
        fun leaveQueue(uuid: String, queue: String): QueueResult {
            jedisPool.resource.use { jedis ->
                if (jedis.lpos("nitrogen:queue:${queue}", uuid) == null) {
                    return QueueResult.ERROR
                }

                jedis.lrem("nitrogen:queue:${queue}", 0, uuid)

                if (playerHandler.queuePlayers.containsKey(UUID.fromString(uuid)))
                    playerHandler.queuePlayers[UUID.fromString(uuid)]?.queues?.remove(queue)

                return QueueResult.SUCCESS
            }
        }
    }

}