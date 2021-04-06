package com.github.recraftedcivilizations.darkcitizens.listeners

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.election.ElectionManager
import com.github.recraftedcivilizations.darkcitizens.events.JobLeaveEvent
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.jobs.elected.GenericElectedJob
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

/**
 * @author DarkVanityOfLight
 */

/**
 * Trigger a new election if someone dies or leaves his job
 * @param dPlayerManager The dPlayerManager to get data from
 * @param jobManager The JobManager to get the jobs from
 * @param electionManager The ElectionManager to create new elections
 */
class ElectionTrigger(private val dPlayerManager: DPlayerManager, private val jobManager: JobManager, private val electionManager: ElectionManager) : Listener {

    /**
     * Leave the job if someone dies and the job has leaveOnDeath
     */
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

    /**
     * Trigger a new election if someone leaves his elected job
     */
    @EventHandler(ignoreCancelled = true)
    fun onJobLeave(e: JobLeaveEvent){
        // Create a new election if a player left his elected job
        if(e.job is GenericElectedJob) electionManager.createElection(e.job)
    }
}