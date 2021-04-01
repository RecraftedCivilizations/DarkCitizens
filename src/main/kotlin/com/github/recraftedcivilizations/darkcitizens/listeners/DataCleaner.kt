package com.github.recraftedcivilizations.darkcitizens.listeners

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

/**
 * @author DarkVanityOfLight
 */

/**
 * Clean job data if a player leaves the server
 * @param dPlayerManager The DPlayerManager to save the cleaned data to and get the data from
 * @param jobManager The JobManager to get all jobs from
 */
class DataCleaner(private val dPlayerManager: DPlayerManager, private val jobManager: JobManager): Listener {

    /**
     * Clean data when a player quits
     */
    @EventHandler(ignoreCancelled = true)
    fun onQuit(e: PlayerQuitEvent){
        val dPlayer = dPlayerManager.getDPlayer(e.player)!!
        dPlayer.job?.let { jobManager.getJob(it)?.leave(dPlayer) }
        dPlayer.isCriminal = false
        dPlayer.wanted = false

        dPlayerManager.setDPlayer(dPlayer)
    }
}