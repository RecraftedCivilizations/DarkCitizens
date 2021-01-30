package com.github.recraftedcivilizations.jobs

import com.github.recraftedcivilizations.dPlayer.DPlayer
import com.github.recraftedcivilizations.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.tasks.ITask
import net.milkbowl.vault.economy.Economy

/**
 * @author DarkVanityOfLight
 */

/**
 * Construct Jobs
 */
object JobFactory {

    /**
     * Create a new job
     * @see IJob
     * @param name The name of the job
     * @param group The group name
     * @param playerLimit The maximum number of players allowed in this Job
     * @param tasks A set of [ITask] the members of the job can fullfill
     * @param canDemote A set of other Job names members of this job can demote others from
     * @param baseIncome The base income for this job
     * @param baseXPGain The base xp gain for this job
     * @param minLvl The minimum level required to join this job
     * @param electionRequired If an election is required to join this job
     * @param permissionRequired If a permission is required to join this job
     * @param dPlayerManager The [DPlayerManager] the members belong to
     */
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