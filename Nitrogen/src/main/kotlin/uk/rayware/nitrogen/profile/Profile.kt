package uk.rayware.nitrogen.profile

import net.luckperms.api.model.user.User
import net.luckperms.api.node.Node
import net.luckperms.api.node.NodeType
import org.bson.Document
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import uk.rayware.nitrogen.Nitrogen
import uk.rayware.nitrogen.profile.event.ProfileBreachedEvent
import uk.rayware.nitrogen.util.SerializableObject
import java.time.Instant
import java.util.UUID
import java.util.stream.Collectors

data class Profile(val uuid: UUID) : SerializableObject {

    var online = false

    var firstSeen: Long? = null
    var lastSeen: Long? = null
        get() = if (online) System.currentTimeMillis() else field
    var lastSeenOn: String? = null

    var loaded = false

    var requestCooldown = 0L
    var staffMessages = true
    var staffChat = false

    var gold = 0
    var freeGoldCooldown = 0L

    var friends: List<UUID> = mutableListOf()

    var acceptingMessages = true
    var messageSounds = true
    var tipsEnabled = true

    var showingVanished = false

    // dont serialize or deserialize
    var lastChat = 0L

    var reportCount = 0

    var modInventory: Array<ItemStack>? = null
    var modArmour: Array<ItemStack>? = null
    var previousGamemode = GameMode.SURVIVAL

    fun getPlayer(): Player? {
        return Bukkit.getPlayer(uuid)
    }

    fun getOnlineFriends(): List<UUID> {
        return emptyList()
    }

    fun getUser(): User? {
        return Nitrogen.nitrogen.lp!!.userManager.getUser(uuid)
    }

    fun getRank(): String {
        return Nitrogen.nitrogen.lp!!.userManager.getUser(uuid)!!.primaryGroup
    }

    fun getRankDisplayName(): String {
        val rank = getRank()
        var colour = ""

        for (meta in Nitrogen.nitrogen.lp!!.groupManager.getGroup(rank)!!.getNodes(NodeType.META)) {
            if (meta.metaKey == "colour") {
                colour = meta.metaValue
                break
            }
        }

        return ChatColor.translateAlternateColorCodes('&', colour) +
                Nitrogen.nitrogen.lp!!.groupManager.getGroup(rank)!!.displayName
    }

    fun getRankWeight(): Int {
        val rank = getRank()

        return Nitrogen.nitrogen.lp!!.groupManager.getGroup(rank)!!.weight.orElse(0)
    }

    fun getAllRanks(): List<String> {
        val ranks = listOf<String>().toMutableList()

        getUser()!!.getNodes(NodeType.INHERITANCE).filter { node -> !node.hasExpired()} .forEach {
            ranks.add(it.groupName)
        }

        return ranks
    }

    fun getRankExpiry(name: String): Instant? {
        val rank = getUser()!!.getNodes(NodeType.INHERITANCE)
            .stream()
            .filter(Node::hasExpiry)
            .filter { node -> !node.hasExpired() }
            .filter { node -> node.groupName == name }
            .collect(Collectors.toList())

        if (rank.isEmpty()) {
            return null
        }

        return rank[0].expiry
    }

    fun isStaff(): Boolean {
        if (getPlayer()!!.isOp && !getPlayer()!!.hasPermission("nitrogen.staff")) {
            Bukkit.getPluginManager().callEvent(ProfileBreachedEvent(this, 1))
            return false
        }

        if (getPlayer()!!.hasPermission("nitrogen.staff")) {
            return true
        }

        return false
    }

    override fun serialize(): Document {
        val document = Document()

        document["uuid"] = uuid.toString()
        document["online"] = online
        document["firstSeen"] = firstSeen
        document["lastSeen"] = lastSeen
        document["lastSeenOn"] = lastSeenOn
        document["requestCooldown"] = requestCooldown
        document["staffMessages"] = staffMessages
        document["staffChat"] = staffChat
        document["gold"] = gold
        document["freeGoldCooldown"] = freeGoldCooldown
        document["friends"] = friends
        document["acceptingMessages"] = acceptingMessages
        document["messageSounds"] = messageSounds
        document["showingVanished"] = showingVanished
        document["tipsEnabled"] = tipsEnabled

        return document
    }

    override fun deserialize(data: Document?) {
        if (data != null && data["uuid"] == uuid.toString()) {
            online = data.getBoolean("online")
            firstSeen = data.getLong("firstSeen")
            lastSeen = data.getLong("lastSeen")
            lastSeenOn = data.getString("lastSeenOn")
            requestCooldown = data.getLong("requestCooldown")
            staffMessages = data.getBoolean("staffMessages")
            staffChat = data.getBoolean("staffChat")
            gold = data.getInteger("gold")
            freeGoldCooldown = data.getLong("freeGoldCooldown")
            friends = data.getList("friends", UUID::class.java)?: emptyList()
            acceptingMessages = data.getBoolean("acceptingMessages")
            messageSounds = data.getBoolean("messageSounds")
            showingVanished = data.getBoolean("showingVanished")
            tipsEnabled = data.getBoolean("tipsEnabled")?: true
        }
    }

}
