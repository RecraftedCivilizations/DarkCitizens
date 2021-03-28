package com.github.recraftedcivilizations.darkcitizens.election

import com.github.darkvanityoflight.recraftedcore.ARecraftedPlugin
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.ElectedJob
import net.milkbowl.vault.economy.Economy

class ElectionManager(private val dPlayerManager: DPlayerManager, private val economy: Economy, private val plugin: ARecraftedPlugin) {
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
}