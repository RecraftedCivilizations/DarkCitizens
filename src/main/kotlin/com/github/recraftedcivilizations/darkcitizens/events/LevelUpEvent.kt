package com.github.recraftedcivilizations.darkcitizens.events

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import org.bukkit.event.Event
import org.bukkit.event.HandlerList


/**
 * @author DarkVanityOfLight
 */

/**
 * An event that gets emitted when a player gets a lvl up in a specific group
 * @param dPlayer The player who has a lvl up
 * @param group The group that gets a lvl up
 * @param newLvl The new level
 */
class LevelUpEvent(
    val dPlayer: DPlayer,
    val group: String,
    val newLvl: Int,
    ): Event() {

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
}