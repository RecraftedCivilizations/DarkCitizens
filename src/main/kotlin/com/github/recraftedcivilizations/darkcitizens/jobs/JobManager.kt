package com.github.recraftedcivilizations.darkcitizens.jobs

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.laws.LawManager
import com.github.recraftedcivilizations.darkcitizens.tasks.ITask
import com.github.recraftedcivilizations.darkcitizens.tasks.TaskManager
import org.bukkit.Material

/**
 * @author DarkVanityOfLight
 */

/**
 * Contains all jobs and create new Jobs from here using [createJob]
 * @constructor Construct using a [TaskManager] and a [DPlayerManager]
 */
class JobManager(private val dPlayerManager: DPlayerManager, private val lawManager: LawManager) {
    private val jobs: MutableSet<IJob> = emptySet<IJob>().toMutableSet()
    private lateinit var taskManager: TaskManager

    fun setTaskManager(taskManager: TaskManager){
        this.taskManager = taskManager
    }

    /**
     * Get a job using its name
     * @param name The name of the job you want
     */
    fun getJob(name: String?): IJob?{
        for (job in jobs){
            if (job.name == name){ return job }
        }
        return null
    }

    /**
     * Create a new Job and add it to the [jobs] Set
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
     * @param icon The icon this Job gets displayed as
     * @param leaveOnDeath  Leave this job when the player dies?
     * @param candidateTime The time span someone can candidate in an elected job
     * @param voteTime The time span in which can be voted in an elected job
     * @param voteFee The amount of money that has to be paid to vote
     * @param candidateFee The amount of money that has to be paid to candidate for a job
     * @param isMajor If the job is a major and can set tax laws etc.
     */
    fun createJob(
        name: String,
        group: String,
        playerLimit: Int,
        tasks: Set<String>,
        canDemote: Set<String>,
        baseIncome: Int,
        baseXPGain: Int,
        minLvl: Int,
        electionRequired: Boolean,
        permissionRequired: Boolean,
        icon: Material,
        leaveOnDeath: Boolean,
        candidateTime: Int? = null,
        voteTime: Int? = null,
        voteFee: Int? = null,
        candidateFee: Int? = null,
        isMajor: Boolean = false,
        ){
        val iTasks = emptySet<ITask>().toMutableSet()

        for (taskName in tasks){
            val task = taskManager.getTask(taskName)
            if (task != null){
                iTasks.add(task)
            }
        }
        jobs.add(
            JobFactory.createJob(
                name,
                group,
                playerLimit,
                iTasks,
                canDemote,
                baseIncome,
                baseXPGain,
                minLvl,
                electionRequired,
                permissionRequired,
                icon,
                leaveOnDeath,
                dPlayerManager,
                this,
                candidateTime,
                voteTime,
                voteFee,
                candidateFee,
                isMajor,
                lawManager,
            )
        )


    }

    /**
     * Returns all registered jobs
     */
    fun getJobs(): Set<IJob>{
        return jobs
    }
}