package com.github.recraftedcivilizations.tasks

import com.github.recraftedcivilizations.dPlayer.DPlayerManager
import net.milkbowl.vault.economy.Economy

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
    fun createTask(name: String, income: Int, xp: Int, action: List<String>, description: String){
        val task = TaskFactory.createTask(name, income, xp, action, description, dPlayerManager, econ)
        tasks.add(task)
    }
}