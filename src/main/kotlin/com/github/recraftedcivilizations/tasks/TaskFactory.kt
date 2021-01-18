package com.github.recraftedcivilizations.tasks

import com.github.recraftedcivilizations.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.tasks.actions.Actions
import com.github.recraftedcivilizations.tasks.actions.IAction
import net.milkbowl.vault.economy.Economy
import java.lang.IllegalArgumentException

object TaskFactory {

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