package com.github.recraftedcivilizations.darkcitizens.tasks

import com.github.recraftedcivilizations.darkcitizens.recraftedcivilizations.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.recraftedcivilizations.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.recraftedcivilizations.tasks.actions.IAction
import net.milkbowl.vault.economy.Economy

/**
 * @author DarkVanityOfLight
 */

/**
 * Construct tasks
 */
object TaskFactory {
    /**
     * Create a new task of the form [ITask] using:
     * @param name The name of the task
     * @param income The money reward for the task
     * @param xp The XP reward for the task
     * @param actions A list of the action names this task consist of
     * @param description The description of this task
     * @param dPlayerManager The DPlayer manager
     * @param econ The economy to pay too
     */
    fun createTask(name: String, income: Int, xp: Int, actions: List<IAction>, description: String, dPlayerManager: DPlayerManager, econ: Economy, jobManager: JobManager): ITask {
        return Task(name, income, xp, actions, description, dPlayerManager, econ, jobManager)
    }

}