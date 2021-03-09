package com.github.recraftedcivilizations.darkcitizens.listeners

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class DataCleaner(private val dPlayerManager: DPlayerManager, private val jobManager: JobManager): Listener {

    @EventHandler(ignoreCancelled = true)
    fun onQuit(e: PlayerQuitEvent){
        val dPlayer = dPlayerManager.getDPlayer(e.player)!!
        dPlayer.job?.let { jobManager.getJob(it)?.leave(dPlayer) }
        dPlayer.isCriminal = false
        dPlayer.wanted = false

        dPlayerManager.setDPlayer(dPlayer)
    }
}