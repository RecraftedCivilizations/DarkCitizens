package com.github.recraftedcivilizations.darkcitizens.events

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * @author DarkVanityOfLight
 */

/**
 * An event that gets emitted when a player leaves his job
 * @param dPlayer The player who leaves his job
 * @param job The job he leaves
 */
class JobLeaveEvent(
    val dPlayer: DPlayer,
    val job: IJob): Event() {

    companion object{
        private val HANDLER_LIST: HandlerList = HandlerList()
    }

    /**
     * Get all handlers
     * @return The HandlerList
     */
    override fun getHandlers(): HandlerList {
        return HANDLER_LIST
    }
}