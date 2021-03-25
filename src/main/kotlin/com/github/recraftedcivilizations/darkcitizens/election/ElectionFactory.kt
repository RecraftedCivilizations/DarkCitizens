package com.github.recraftedcivilizations.darkcitizens.election

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import net.milkbowl.vault.economy.Economy

object ElectionFactory {

    fun createElection(electTime: Int, job: IJob, voteFee: Int, candidateFee: Int, dPlayerManager: DPlayerManager, economy: Economy): IElect{
        return GUIElection(electTime, job, voteFee, candidateFee, dPlayerManager, economy)
    }
}