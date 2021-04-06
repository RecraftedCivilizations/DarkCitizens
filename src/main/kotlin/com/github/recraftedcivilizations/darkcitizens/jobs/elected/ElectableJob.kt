package com.github.recraftedcivilizations.darkcitizens.jobs.elected

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob

interface ElectableJob : IJob{
    override val leaveOnDeath: Boolean
    val candidateTime: Int
    val voteTime: Int
    val voteFee: Int
    val candidateFee: Int
    val dPlayerManager: DPlayerManager
}