package com.github.recraftedcivilizations.darkcitizens.election

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import net.milkbowl.vault.economy.Economy

object ElectionFactory {

    fun createElection(job: IJob, voteFee: Int, candidateFee: Int, dPlayerManager: DPlayerManager, economy: Economy, electionManager: ElectionManager, bukkitWrapper: BukkitWrapper? = null): IElect{
        return if (bukkitWrapper != null){
            GUIElection(job, voteFee, candidateFee, dPlayerManager, economy, electionManager, bukkitWrapper)
        }else{
            GUIElection(job, voteFee, candidateFee, dPlayerManager, economy, electionManager)
        }
    }
}