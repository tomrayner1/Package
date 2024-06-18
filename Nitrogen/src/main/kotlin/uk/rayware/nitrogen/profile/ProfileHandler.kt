package uk.rayware.nitrogen.profile

import net.luckperms.api.event.group.GroupDataRecalculateEvent
import net.luckperms.api.event.user.UserDataRecalculateEvent
import net.luckperms.api.node.NodeType
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable
import uk.rayware.nitrogen.Nitrogen
import uk.rayware.nitrogen.NitrogenAPI

import java.util.UUID

class ProfileHandler(val nitrogen: Nitrogen) : Listener {

    val profileMap: HashMap<UUID, Profile> = HashMap()

    init {
        nitrogen.server.pluginManager.registerEvents(this, nitrogen)

        val bus = nitrogen.lp?.eventBus

        bus?.subscribe(nitrogen, GroupDataRecalculateEvent::class.java, this::onGroupDataRecalculate)
        bus?.subscribe(nitrogen, UserDataRecalculateEvent::class.java, this::onUserDataRecalculateEvent)
    }

    @EventHandler
    fun onPreJoin(event: AsyncPlayerPreLoginEvent) {
        val profile = Profile(event.uniqueId)

        if (!profileMap.containsKey(event.uniqueId))
            profileMap[event.uniqueId] = profile
        else
            profileMap.replace(event.uniqueId, profile)

        //TODO: check punishments

        NitrogenAPI.loadProfile(profile)

        profile.loaded = true

        profile.online = true

        if (profile.firstSeen == null)
            profile.firstSeen = System.currentTimeMillis()

        profile.lastSeen = System.currentTimeMillis()
        profile.lastSeenOn = NitrogenAPI.nitrogenServer.identifier

        NitrogenAPI.saveProfile(profile)
    }

    @EventHandler
    fun join(event: PlayerJoinEvent) {
        val player = event.player

        val user = nitrogen.lp!!.userManager.getUser(player.uniqueId)
        val group = nitrogen.lp!!.groupManager.getGroup(user!!.primaryGroup)
        val metadata = group!!.getNodes(NodeType.META)

        for (meta in metadata) {
            if (meta.metaKey == "colour") {
                updateRedis(player.uniqueId, player.name, meta.metaValue)
                player.displayName = ChatColor.translateAlternateColorCodes('&', meta.metaValue) + player.name
                return
            }
        }

        val profile = NitrogenAPI.getProfile(player.uniqueId) ?: return

        if (profile.tipsEnabled) {
            player.removeMetadata("hidetips", Nitrogen.nitrogen)
        } else {
            player.setMetadata("hidetips", FixedMetadataValue(Nitrogen.nitrogen, true))
        }

        if (!profile.isStaff() && profile.showingVanished)
            profile.showingVanished = false
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val profile = NitrogenAPI.getProfile(event.player.uniqueId)

        if (profile != null) {
            profile.lastSeenOn = NitrogenAPI.nitrogenServer.identifier
            profile.online = false

            NitrogenAPI.saveProfile(profile)
        }

        profileMap.remove(event.player.uniqueId)
    }

    private fun onGroupDataRecalculate(event: GroupDataRecalculateEvent) {
        Bukkit.getScheduler().runTaskAsynchronously(nitrogen) {
            for (player in Bukkit.getOnlinePlayers()) {
                val user = nitrogen.lp!!.userManager.getUser(player.uniqueId)
                val group = nitrogen.lp!!.groupManager.getGroup(user!!.primaryGroup)
                val metadata = group!!.getNodes(NodeType.META)

                for (meta in metadata) {
                    if (meta.metaKey == "colour") {
                        updateRedis(player.uniqueId, player.name, meta.metaValue)
                        val name = ChatColor.translateAlternateColorCodes('&', meta.metaValue) + player.name

                        player.displayName = name

                        if (nitrogen.nitrogenConfig.updateTabName) {
                            player.playerListName = name
                        }

                        break
                    }
                }
            }
        }
    }

    private fun onUserDataRecalculateEvent(event: UserDataRecalculateEvent) {
        val player = Bukkit.getPlayer(event.user.uniqueId)

        if (player == null || !player.isOnline)
            return

        val group = nitrogen.lp!!.groupManager.getGroup(event.user.primaryGroup)
        val metadata = group!!.getNodes(NodeType.META)

        for (meta in metadata) {
            if (meta.metaKey == "colour") {
                updateRedis(player.uniqueId, player.name, meta.metaValue)
                val name = ChatColor.translateAlternateColorCodes('&', meta.metaValue) + player.name

                player.displayName = name

                if (nitrogen.nitrogenConfig.updateTabName) {
                    player.playerListName = name
                }

                break
            }
        }
    }

    private fun updateRedis(id: UUID, name: String, colour: String) {
        NitrogenAPI.jedisPool.resource.use { jedis ->
            jedis.hset("nitrogen:profiles:name", id.toString(), name)
            jedis.hset("nitrogen:profiles:colour", id.toString(), colour)
            jedis.hset("nitrogen:profiles:weight", id.toString(), NitrogenAPI.getProfile(id)!!.getRankWeight().toString())
        }
    }

}