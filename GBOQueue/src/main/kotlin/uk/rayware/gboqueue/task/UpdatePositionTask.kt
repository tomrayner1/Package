package uk.rayware.gboqueue.task

import uk.rayware.gboqueue.QueuePlugin

class UpdatePositionTask : Runnable {

    override fun run() {
        QueuePlugin.jedisPool.resource.use { jedis ->
            QueuePlugin.playerHandler.queuePlayers.forEach { uuid, player ->
                if (player.queues.isEmpty())
                    return@forEach

                player.queues.keys.forEach { queue ->
                    player.queues[queue] = jedis.lpos("nitrogen:queue:$queue", uuid.toString()).toInt()
                }
            }
        }
    }

}