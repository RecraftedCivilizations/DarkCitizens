package com.github.recraftedcivilizations.darkcitizens.listeners

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.election.ElectionManager
import com.github.recraftedcivilizations.darkcitizens.events.JobLeaveEvent
import com.github.recraftedcivilizations.darkcitizens.jobs.ElectedJob
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class ElectionTrigger(private val dPlayerManager: DPlayerManager, private val jobManager: JobManager, private val electionManager: ElectionManager) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onDeath(e: PlayerDeathEvent){
        val player = e.entity
        val dPlayer = dPlayerManager.getDPlayer(player)!!

        val job = jobManager.getJob(dPlayer.job) ?: return

        // Leave the job if the leave on death option is set
        if (job.leaveOnDeath){
            job.leave(dPlayer)
        }

    }

    @EventHandler(ignoreCancelled = true)
    fun onJobLeave(e: JobLeaveEvent){
        // Create a new election if a player left his elected job
        if(e.job is ElectedJob) electionManager.createElection(e.job)
    }
}