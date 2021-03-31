package com.github.recraftedcivilizations.darkcitizens.events

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class JobLeaveEvent(
    val dPlayer: DPlayer,
    val job: IJob): Event() {

    companion object{
        private val HANDLER_LIST: HandlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList {
        return HANDLER_LIST
    }
}