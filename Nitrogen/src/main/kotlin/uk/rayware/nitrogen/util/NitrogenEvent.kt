package uk.rayware.nitrogen.util

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

open class NitrogenEvent : Event() {

    private final val handlers: HandlerList = HandlerList()

    override fun getHandlers(): HandlerList {
        return handlers
    }

    fun getHandlerList(): HandlerList {
        return handlers
    }


}