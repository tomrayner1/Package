package uk.rayware.nitrogen.punishment

import org.bson.Document
import uk.rayware.nitrogen.util.SerializableObject
import java.util.UUID

data class Punishment(
    val id: UUID,
    var target: UUID,
    var punishmentType: PunishmentType,

    ) : SerializableObject {

    constructor(target: UUID, punishmentType: PunishmentType) : this(UUID.randomUUID(), target, punishmentType)


    override fun serialize(): Document {
        val document = Document()

        document["_id"] = id
        document["target"] = target.toString()
        document["punishmentType"] = punishmentType.name

        return document
    }

    override fun deserialize(data: Document?) {
        if (data != null && data["_id"] == id) {
            target = UUID.fromString(data.getString("target"))
            punishmentType = PunishmentType.valueOf(data.getString("punishmentType"))
        }
    }

}