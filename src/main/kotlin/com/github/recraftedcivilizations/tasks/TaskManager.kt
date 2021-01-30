package com.github.recraftedcivilizations.tasks

import com.github.recraftedcivilizations.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.tasks.actions.Actions
import com.github.recraftedcivilizations.tasks.actions.IAction
import net.milkbowl.vault.economy.Economy
import java.lang.IllegalArgumentException

/**
 * @author DarkVanityOfLight
 */

/**
 * Contains all tasks and create new tasks from here using [createTask]
 * @constructor Construct a new [TaskManager] using an economy and a [DPlayerManager]
 */
class TaskManager(val econ: Economy, val dPlayerManager: DPlayerManager) {
    val tasks: MutableSet<ITask> = emptySet<ITask>().toMutableSet()

    /**
     * Create a new task through the [TaskFactory] and add it to the [tasks] list
     * @param name The name of the task
     * @param income The reward income for this task
     * @param xp The reward XP for this task
     * @param action A list of actions this task consist of
     * @param description A description of this task
     */
    fun createTask(name: String, income: Int, xp: Int, actions: List<String>, description: String){
        val parsedActions = emptyList<IAction>().toMutableList()

        for (action in actions){
            try{
                val parsedAction = Actions.valueOf(action)
                parsedActions.plus(parsedAction)

            } catch (e: IllegalArgumentException){}

        }
        val task = TaskFactory.createTask(name, income, xp, parsedActions, description, dPlayerManager, econ)
        tasks.add(task)
    }

    /**
     * Get a task from the task manager
     * @param name The name of the job to get
     */
    fun getTask(name: String): ITask?{
        for (task in tasks){
            if (task.name.equals(name, ignoreCase = true)){
                return task
            }
        }
        return null
    }
}