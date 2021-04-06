package com.github.recraftedcivilizations.darkcitizens.jobs

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.elected.GenericElectedJob
import com.github.recraftedcivilizations.darkcitizens.jobs.special.major.ElectedMajor
import com.github.recraftedcivilizations.darkcitizens.jobs.special.major.UnelectedMajor
import com.github.recraftedcivilizations.darkcitizens.laws.LawManager
import com.github.recraftedcivilizations.darkcitizens.tasks.ITask
import org.bukkit.Material

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
     * @param isMajor If the Job should be of type major
     * @param lawManager The law manager, only needed if you want to create a Major job
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
        icon: Material,
        leaveOnDeath: Boolean,
        dPlayerManager: DPlayerManager,
        jobManager: JobManager,
        candidateTime: Int? = null,
        voteTime: Int? = null,
        voteFee: Int? = null,
        candidateFee: Int? = null,
        isMajor: Boolean = false,
        lawManager: LawManager? = null
    ): IJob {

        if (isMajor){
            if (electionRequired){
                return ElectedMajor(lawManager!!,
                    name, group, playerLimit, tasks, canDemote, baseIncome, baseXPGain, minLvl, permissionRequired, icon,
                    leaveOnDeath, dPlayerManager, jobManager, candidateTime!!, voteTime!!, voteFee!!, candidateFee!!)
            }else{
                return UnelectedMajor(
                    lawManager!!,
                    name, group, playerLimit, tasks, canDemote, baseIncome, baseXPGain, minLvl, permissionRequired, icon,
                    leaveOnDeath, dPlayerManager, jobManager)
            }
        }else{
            if (electionRequired){
                return GenericElectedJob(
                    leaveOnDeath,
                    candidateTime!!,
                    voteTime!!,
                    voteFee!!,
                    candidateFee!!,
                    dPlayerManager,
                    name, group, playerLimit, tasks, canDemote, baseIncome, baseXPGain, minLvl, permissionRequired, icon,
                    jobManager)
            }else{

            return Job(
                name,
                group,
                playerLimit,
                tasks,
                canDemote,
                baseIncome,
                baseXPGain,
                minLvl,
                permissionRequired,
                icon,
                leaveOnDeath,
                dPlayerManager,
                jobManager)
            }
        }
    }
}