package com.github.recraftedcivilizations.darkcitizens.events

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * @author DarkVanityOfLight
 */

/**
 * An event that gets emitted when someone gains xp
 * @param player The player who gained xp
 * @param group The group which gains xp
 * @param amount The amount of xp that is gained
 * @property stateOfCancelled If the event is cancelled or not
 */
class XpGainEvent(
    val player: DPlayer,
    val group: String,
    val amount: Int,
): Event(), Cancellable {
    // Stupid name but I can't use cancelled because of jvm
    var stateOfCancelled: Boolean = false


    companion object{
        private val HANDLER_LIST: HandlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLER_LIST
        }
    }

    /**
     * Get all handlers
     * @return The HandlerList
     */
    override fun getHandlers(): HandlerList {
        return HANDLER_LIST
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    override fun isCancelled(): Boolean {
        return stateOfCancelled
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * @param cancel true if you wish to cancel this event
     */
    override fun setCancelled(cancel: Boolean) {
        stateOfCancelled = cancel
    }
}