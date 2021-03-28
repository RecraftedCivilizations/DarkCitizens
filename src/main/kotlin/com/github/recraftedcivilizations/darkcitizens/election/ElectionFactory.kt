package com.github.recraftedcivilizations.darkcitizens.election

import com.github.darkvanityoflight.recraftedcore.ARecraftedPlugin
import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import net.milkbowl.vault.economy.Economy

object ElectionFactory {

    fun createElection(job: IJob, voteFee: Int, candidateFee: Int, candidateTime: Int, voteTime: Int, plugin: ARecraftedPlugin, dPlayerManager: DPlayerManager, economy: Economy, electionManager: ElectionManager, bukkitWrapper: BukkitWrapper? = null): IElect{
        return if (bukkitWrapper != null){
            GUIElection(job, voteFee, candidateFee, voteTime, candidateTime, dPlayerManager, economy, electionManager, plugin, bukkitWrapper)
        }else{
            GUIElection(job, voteFee, candidateFee, voteTime, candidateTime, dPlayerManager, economy, electionManager, plugin)
        }
    }
}