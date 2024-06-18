package uk.rayware.nitrogen.util

import org.bson.Document

interface SerializableObject {

    fun serialize(): Document
    fun deserialize(data: Document?)

}