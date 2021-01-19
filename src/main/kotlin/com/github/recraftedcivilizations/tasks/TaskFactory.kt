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
    fun createTask(name: String, income: Int, xp: Int, actions: List<String>, description: String, dPlayerManager: DPlayerManager, econ: Economy): ITask{
        val parsedActions = emptyList<IAction>().toMutableList()

        for (action in actions){
            try{
                val parsedAction = Actions.valueOf(action)
                parsedActions.plus(parsedAction)

            } catch (e: IllegalArgumentException){}

        }

        return Task(name, income, xp, parsedActions, description, dPlayerManager, econ)
    }

}