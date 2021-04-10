package com.github.recraftedcivilizations.darkcitizens.election

import com.github.darkvanityoflight.recraftedcore.ARecraftedPlugin
import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.elected.GenericElectedJob
import net.milkbowl.vault.economy.Economy
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle

/**
 * @author DarkVanityOfLight
 */

/**
 * Create and manage elections here
 * @param dPlayerManager The DPlayerManager to get DPlayer data from
 * @param economy The economy to withdraw fees from
 * @param plugin The Main class
 * @param bukkitWrapper An optional bukkit wrapper for debugging
 */
class ElectionManager(private val dPlayerManager: DPlayerManager, private val economy: Economy, private val plugin: ARecraftedPlugin, private val bukkitWrapper: BukkitWrapper = BukkitWrapper()) {
    private val elections = emptySet<IElect>().toMutableSet()


    /**
     * Create and register an election here
     * @param job The job the election is for
     */
    fun createElection(job: GenericElectedJob){
        val election = ElectionFactory.createElection(job, job.voteFee, job.candidateFee, job.candidateTime, job.voteTime, plugin, dPlayerManager, economy, this)
        elections.add(election)
        election.start()
        bukkitWrapper.notify("A new election for the job ${job.name} has started!!", BarColor.RED, BarStyle.SEGMENTED_10, 15, bukkitWrapper.getOnlinePlayers())
    }

    /**
     * Get all registered Elections
     * @return A set of all registered Elections
     */
    fun getElections(): Set<IElect>{
        return elections.toSet()
    }

    /**
     * This should only be called by the election itself,
     * it ll remove the election from the manager
     * @param election The election to end
     */
    fun electionEnded(election: IElect){
        elections.remove(election)
    }

    /**
     * Get an existing election for the given job
     * @param job The Job the election should be for
     * @return The Election or null if no election is found for the job
     */
    fun getElection(job: GenericElectedJob): IElect?{
        for (election in elections){
            if (election.job == job){
                return  election
            }
        }
        return null
    }

    /**
     * Check if an job has an running election
     * @param job The job to check if an election is running
     * @return true if there is an election for this job running false if not
     */
    fun isRunningElection(job: GenericElectedJob): Boolean{
        for (election in elections){
            if (election.job == job){
                return  true
            }
        }
        return false
    }
}