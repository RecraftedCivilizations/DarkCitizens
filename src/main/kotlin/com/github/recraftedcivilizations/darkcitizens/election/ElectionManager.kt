package com.github.recraftedcivilizations.darkcitizens.election

import com.github.darkvanityoflight.recraftedcore.ARecraftedPlugin
import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.ElectedJob
import net.milkbowl.vault.economy.Economy
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle

class ElectionManager(private val dPlayerManager: DPlayerManager, private val economy: Economy, private val plugin: ARecraftedPlugin, private val bukkitWrapper: BukkitWrapper = BukkitWrapper()) {
    private val elections = emptySet<IElect>().toMutableSet()


    /**
     * Create and register an election here
     * @param electTime The time the election should run for
     * @param job The job the election is for
     * @param voteFee The fee that has to be payed to vote
     * @param candidateFee The fee that has to be payed to candidate
     */
    fun createElection(job: ElectedJob){
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

    fun getElection(job: ElectedJob): IElect?{
        for (election in elections){
            if (election.job == job){
                return  election
            }
        }
        return null
    }

    fun isRunningElection(job: ElectedJob): Boolean{
        for (election in elections){
            if (election.job == job){
                return  true
            }
        }
        return false
    }
}