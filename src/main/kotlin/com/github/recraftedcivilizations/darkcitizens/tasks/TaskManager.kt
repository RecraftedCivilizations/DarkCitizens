package com.github.recraftedcivilizations.darkcitizens.tasks

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.groups.GroupManager
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.actions.ActionManager
import com.github.recraftedcivilizations.darkcitizens.actions.Actions
import com.github.recraftedcivilizations.darkcitizens.actions.IAction
import net.milkbowl.vault.economy.Economy
import org.bukkit.Material

/**
 * @author DarkVanityOfLight
 */

/**
 * Contains all tasks and create new tasks from here using [createTask]
 * @constructor Construct a new [TaskManager] using an economy and a [DPlayerManager]
 */
class TaskManager(private val econ: Economy, private val dPlayerManager: DPlayerManager, private val groupManager: GroupManager) {
    val tasks: MutableSet<ITask> = emptySet<ITask>().toMutableSet()
    private lateinit var jobManager: JobManager

    fun setJobManager(jobManager: JobManager){
        this.jobManager = jobManager
    }

    /**
     * Create a new task through the [TaskFactory] and add it to the [tasks] list
     * @param name The name of the task
     * @param income The reward income for this task
     * @param xp The reward XP for this task
     * @param actions A list of actions this task consist of
     * @param description A description of this task
     */
    fun createTask(name: String, income: Int, xp: Int, actions: List<String>, description: String, icon: Material) {
        val parsedActions = emptyList<IAction>().toMutableList()

        for (action in actions) {
            try {
                val parsedAction = ActionManager.getAction(action)!!
                parsedActions.add(parsedAction)

            } catch (e: IllegalArgumentException) {
                val parsedAction = ActionManager.getAction(action)
                if (parsedAction != null){
                    parsedActions.add(parsedAction)
                }
            }

        }
        val task =
            TaskFactory.createTask(name, income, xp, parsedActions, description, icon, dPlayerManager, econ, jobManager, groupManager)
        tasks.add(task)
    }

    /**
     * Get a task from the task manager
     * @param name The name of the job to get
     */
    fun getTask(name: String): ITask? {
        for (task in tasks) {
            if (task.name.equals(name, ignoreCase = true)) {
                return task
            }
        }
        return null
    }
}