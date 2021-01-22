package com.github.recraftedcivilizations.jobs

import com.github.recraftedcivilizations.dPlayer.DPlayer
import com.github.recraftedcivilizations.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.tasks.ITask
import net.milkbowl.vault.economy.Economy

object JobFactory {

    fun createJob(
        name: String,
        group: String,
        playerLimit: Int,
        tasks: Set<ITask>,
        canDemote: Set<String>,
        baseIncome: Int,
        baseXPGain: Int,
        minLvl: Int,
        electionRequired: Boolean,
        permissionRequired: Boolean,
        dPlayerManager: DPlayerManager,
    ): IJob{
        return Job(name, group, playerLimit, tasks, canDemote, baseIncome, baseXPGain, minLvl, electionRequired, permissionRequired, dPlayerManager)
    }
}