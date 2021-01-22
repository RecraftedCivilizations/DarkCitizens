package com.github.recraftedcivilizations.jobs

import com.github.recraftedcivilizations.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.tasks.ITask
import com.github.recraftedcivilizations.tasks.TaskManager

class JobManager(private val taskManager: TaskManager, private val dPlayerManager: DPlayerManager) {
    private val jobs: MutableSet<IJob> = emptySet<IJob>().toMutableSet()

    fun getJob(name: String): IJob?{
        for (job in jobs){
            if (job.name == name){ return job }
        }
        return null
    }

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
            jobs.add(JobFactory.createJob(name, group, playerLimit, iTasks, canDemote, baseIncome, baseXPGain, minLvl, electionRequired, permissionRequired, dPlayerManager))
        }


    }
}