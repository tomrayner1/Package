package uk.rayware.nitrogen

import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.MongoCommandException
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoDatabase
import net.luckperms.api.LuckPerms
import net.milkbowl.vault.chat.Chat
import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.permission.Permission
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandMap
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.java.JavaPlugin
import redis.clients.jedis.JedisPool
import uk.rayware.nitrogen.chat.ChatListener
import uk.rayware.nitrogen.chat.ChatSettings
import uk.rayware.nitrogen.commands.chat.*
import uk.rayware.nitrogen.commands.other.HelpCommand
import uk.rayware.nitrogen.commands.request.HelpOpCommand
import uk.rayware.nitrogen.commands.request.ReportCommand
import uk.rayware.nitrogen.commands.server.EnvironmentDumpCommand
import uk.rayware.nitrogen.commands.settings.SettingsCommand
import uk.rayware.nitrogen.commands.settings.ToggleMessagesCommand
import uk.rayware.nitrogen.commands.settings.ToggleSoundsCommand
import uk.rayware.nitrogen.commands.settings.ToggleTipsCommand
import uk.rayware.nitrogen.commands.staff.ModModeCommand
import uk.rayware.nitrogen.commands.staff.StaffChatCommand
import uk.rayware.nitrogen.commands.teleport.TeleportPosition
//import uk.rayware.nitrogen.lampcommands.GoldCommand
import uk.rayware.nitrogen.network.NitrogenPacketHandler
import uk.rayware.nitrogen.profile.Profile
import uk.rayware.nitrogen.profile.ProfileHandler
import uk.rayware.nitrogen.pyrite.Pyrite
import uk.rayware.nitrogen.pyrite.PyriteCredentials
import uk.rayware.nitrogen.staff.StaffListener
import uk.rayware.nitrogen.util.NitrogenConfig
import uk.rayware.nitrolib.NitroLib
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger


class Nitrogen : JavaPlugin() {

    companion object {
        @JvmStatic
        lateinit var nitrogen: Nitrogen
    }

    lateinit var profileHandler: ProfileHandler
    //lateinit var lampHandler: BukkitCommandHandler

    lateinit var nitrogenConfig: NitrogenConfig
    lateinit var chatSettings: ChatSettings

    var econ: Economy? = null
    var perms: Permission? = null
    var chat: Chat? = null
    var lp: LuckPerms? = null

    private var attempts = 0
    private var successes = 0

    override fun onLoad() {
        nitrogen = this

        saveDefaultConfig()

        NitrogenAPI.mongo = registerMongo()

        try {
            logger.info("Creating profiles collection.")
            NitrogenAPI.mongo.createCollection("profiles")
        } catch (_: MongoCommandException) {
            logger.info("Collection profiles already exists, loading.")
        } finally {
            NitrogenAPI.profilesCollection = NitrogenAPI.mongo.getCollection("profiles")
            NitrogenAPI.punishmentCollection = NitrogenAPI.mongo.getCollection("punishments")
        }

        NitrogenAPI.jedisPool = registerRedis()

        NitrogenAPI.pyrite.registerContainer(NitrogenPacketHandler())

        NitrogenAPI.nitrogenServer = NitrogenServer(this, config.getString("server.name"),
            config.getString("server.displayName"), config.getString("server.group"))

        logger.log(Level.INFO, "Starting ${NitrogenAPI.nitrogenServer.identifier}. Current group: ${NitrogenAPI.nitrogenServer.group}.")

        if (!NitrogenAPI.nitrogenServer.valid) {
            logger.log(Level.SEVERE, "Nitrogen instance is not valid, this is because there is a server with the " +
                    "same identifier online or because of other connection issues with either mongo or redis.")
            server.shutdown()
            return
        }

        System.setProperty("luckperms.server", NitrogenAPI.nitrogenServer.identifier)
    }

    override fun onEnable() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            logger.log(Level.WARNING, "Vault was not found.")
        } else {
            if (hookVault()) {
                logger.log(Level.INFO, "Hooked into Vault.")
            } else {
                logger.log(Level.SEVERE, "An error occurred whilst trying to hook into Vault. Some features may not work as intended.")
            }
        }

        if (Bukkit.getPluginManager().getPlugin("LuckPerms") == null) {
            logger.log(Level.WARNING, "LuckPerms was not found.")
        } else {
            if (hookPerms()) {
                logger.log(Level.INFO, "Hooked into LuckPerms.")
            } else {
                logger.log(Level.SEVERE, "An error occurred whilst trying to hook into LuckPerms. Some features may not work as intended.")
            }
        }

        server.scheduler.runTaskTimerAsynchronously(this, {
            try {
                attempts++
                NitrogenAPI.nitrogenServer.heartbeat()
                successes++
            } catch (e: Exception) {
                logger.log(Level.SEVERE, "Error whilst trying to heartbeat:")
                e.printStackTrace()
            }

            if (attempts == 60) {
                logger.log(Level.INFO, "${successes}/$attempts heartbeats were successful.")
                attempts = 0
                successes = 0
            }
        }, 0, 20 * 2)

        nitrogenConfig = NitrogenConfig(config)
        profileHandler = ProfileHandler(nitrogen)

        chatSettings = ChatSettings()
        chatSettings.slow = config.getInt("config.chat.slow")
        chatSettings.muted = config.getBoolean("config.chat.muted")

        commandsAndListeners()

        //lampHandler = BukkitCommandHandler.create(this)
        //lampHandler.register(GoldCommand())
    }

    override fun onDisable() {
        if (NitrogenAPI.nitrogenServer.valid) {
            NitrogenAPI.nitrogenServer.setOffline()
        }

        Bukkit.getOnlinePlayers().forEach { player -> player.kickPlayer("${ChatColor.RED}Server is restarting...") }

        // Dont save: https://trello.com/c/JjgW4EXm/29-nitrogen-weird-profile-data-loss-issue-idk-either
        // profileHandler.profileMap.forEach { entry: Map.Entry<UUID, Profile> -> NitrogenAPI.saveProfile(entry.value) }

        try {
            NitrogenAPI.jedisPool.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //lampHandler.unregisterAllCommands()
    }

    private fun hookVault(): Boolean {
        //val rspe: RegisteredServiceProvider<Economy> = server.servicesManager.getRegistration(Economy::class.java)
        //econ = rspe.provider

        val rspp: RegisteredServiceProvider<Permission> = server.servicesManager.getRegistration(Permission::class.java)
        perms = rspp.provider

        val rspc: RegisteredServiceProvider<Chat> = server.servicesManager.getRegistration(Chat::class.java)
        chat = rspc.provider

        return /*econ != null ||*/ perms != null || chat != null
    }

    private fun hookPerms(): Boolean {
        val rsplp: RegisteredServiceProvider<LuckPerms> = server.servicesManager.getRegistration(LuckPerms::class.java)
        lp = rsplp.provider

        return lp != null
    }

    private fun registerMongo(): MongoDatabase {
        val host = config.getString("mongo.host")
        val port = config.getInt("mongo.port")

        return if (config.getBoolean("mongo.auth.enabled")) {
            val user = config.getString("mongo.auth.username")
            val pass = config.getString("mongo.auth.password")

            val address = ServerAddress(host, port)
            val credential = MongoCredential.createCredential(user, "admin", pass.toCharArray())

            MongoClient(address, credential, MongoClientOptions.builder().build()).getDatabase("Nitrogen")
        } else {
            MongoClient(host, port).getDatabase("Nitrogen")
        }
    }

    private fun registerRedis(): JedisPool {
        val host = config.getString("redis.host")
        val port = config.getInt("redis.port")

        val jedisPool = JedisPool(host, port)

        val pyriteCredentials = PyriteCredentials(host, null, port)

        if (config.getBoolean("redis.auth.enabled")) {
            val password = config.getString("redis.auth.password")
            try {
                val jedis = jedisPool.resource
                jedis.auth(password)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            pyriteCredentials.password = config.getString("redis.auth.password")
        }

        NitrogenAPI.pyrite = Pyrite(pyriteCredentials)

        return jedisPool
    }

    private fun commandsAndListeners() {
        listOf(
            StaffChatCommand(),
            ModModeCommand(this),

            MuteChatCommand(this),
            SlowChatCommand(this),
            AlertCommand(),
            AlertRawCommand(),

            EnvironmentDumpCommand(this),

            TeleportPosition(),

            SettingsCommand(),
            ToggleMessagesCommand(),
            ToggleSoundsCommand(),
            ToggleTipsCommand(),

            HelpCommand(),
            HelpOpCommand(this),
            ReportCommand()
        ).forEach { cmd ->
            NitroLib.getInstance().nitroCommandMap.register("nitrogen", cmd)
        }

        listOf(
            ChatListener(this),
            StaffListener()
        ).forEach { server.pluginManager.registerEvents(it, this) }
    }

}