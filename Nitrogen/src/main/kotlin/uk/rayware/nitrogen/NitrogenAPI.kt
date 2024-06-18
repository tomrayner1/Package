package uk.rayware.nitrogen

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import org.bson.Document
import redis.clients.jedis.JedisPool
import uk.rayware.nitrogen.profile.Profile
import uk.rayware.nitrogen.pyrite.Pyrite
import java.util.UUID

class NitrogenAPI {

    companion object {
        @JvmStatic
        lateinit var mongo: MongoDatabase
        @JvmStatic
        lateinit var jedisPool: JedisPool

        @JvmStatic
        lateinit var profilesCollection: MongoCollection<Document>
        @JvmStatic
        lateinit var punishmentCollection: MongoCollection<Document>

        @JvmStatic
        lateinit var nitrogenServer: NitrogenServer

        @JvmStatic
        lateinit var pyrite: Pyrite
        @JvmStatic

        val cachedServerData: HashMap<String, Map<String, String>> = HashMap()

        @JvmStatic
        fun loadProfileData(uuid: UUID): Document? {
            return profilesCollection.find(Filters.eq("uuid", uuid.toString())).first()
        }

        @JvmStatic
        fun loadProfile(profile: Profile): Profile {
            profile.deserialize(loadProfileData(profile.uuid))
            return profile
        }

        @JvmStatic
        fun saveProfileData(uuid: UUID, data: Document) {
            profilesCollection.replaceOne(Filters.eq("uuid", uuid.toString()), data, ReplaceOptions().upsert(true))
        }

        @JvmStatic
        fun saveProfile(profile: Profile) {
            Nitrogen.nitrogen.server.scheduler.runTaskAsynchronously(Nitrogen.nitrogen) {
                saveProfileData(profile.uuid, profile.serialize())
            }
        }

        @JvmStatic
        fun getProfile(uuid: UUID): Profile? {
            if (Nitrogen.nitrogen.profileHandler.profileMap.containsKey(uuid))
                return Nitrogen.nitrogen.profileHandler.profileMap[uuid]

            return loadProfile(Profile(uuid))
        }

        @JvmStatic
        fun getNitrogenServerData(name: String): HashMap<String, String> {
            val map = hashMapOf<String, String>()

            jedisPool.resource.use { jedis ->
                val response = jedis.hgetAll("nitrogen:server:${name}")

                if (response == null || response.isEmpty()) {
                    return map
                } else {
                    response.forEach { res ->
                        map[res.key] = res.value
                    }
                }
            }

            if (cachedServerData.containsKey(name)) {
                cachedServerData.replace(name, map)
            } else {
                cachedServerData[name] = map
            }

            return map
        }

        @JvmStatic
        fun getAllNitrogenServerIdentifiers(): Set<String> {
            val servers = mutableListOf<String>()

            jedisPool.resource.use { jedis ->
                val response = jedis.keys("nitrogen:server:*")

                if (response.isEmpty()) {
                    return servers.toSet()
                } else {
                    response.forEach {
                        if (it != null) {
                            servers.add(it.removePrefix("nitrogen:server:"))
                        }
                    }
                }
            }

            return servers.toSet()
        }

        @JvmStatic
        fun getStoredServerData(server: String): HashMap<String, String> {
            return if (cachedServerData.containsKey(server) && (System.currentTimeMillis() - cachedServerData[server]!!["updated"]!!.toLong()) <= 2) {
                cachedServerData[server] as HashMap<String, String>
            } else {
                getNitrogenServerData(server)
            }
        }
    }

}