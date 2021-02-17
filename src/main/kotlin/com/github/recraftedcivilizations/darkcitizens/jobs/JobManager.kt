package com.github.recraftedcivilizations.darkcitizens.jobs

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.tasks.ITask
import com.github.recraftedcivilizations.darkcitizens.tasks.TaskManager

/**
 * @author DarkVanityOfLight
 */

/**
 * Contains all jobs and create new Jobs from here using [createJob]
 * @constructor Construct using a [TaskManager] and a [DPlayerManager]
 */
class JobManager(private val dPlayerManager: DPlayerManager) {
    private val jobs: MutableSet<IJob> = emptySet<IJob>().toMutableSet()
    private lateinit var taskManager: TaskManager

    fun setTaskManager(taskManager: TaskManager){
        this.taskManager = taskManager
    }

    /**
     * Get a job using its name
     * @param name The name of the job you want
     */
    fun getJob(name: String): IJob?{
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
                dPlayerManager
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