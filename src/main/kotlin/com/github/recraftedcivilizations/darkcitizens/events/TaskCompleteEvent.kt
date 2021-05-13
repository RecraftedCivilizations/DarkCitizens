package com.github.recraftedcivilizations.darkcitizens.events

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.tasks.ITask
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * @author DarkVanityOfLight
 */

/**
 * An event that gets emitted when a player leaves his job
 * @param dPlayer The player who completed the task
 * @param task The completed task
 */
class TaskCompleteEvent(
    val dPlayer: DPlayer,
    val task: ITask
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