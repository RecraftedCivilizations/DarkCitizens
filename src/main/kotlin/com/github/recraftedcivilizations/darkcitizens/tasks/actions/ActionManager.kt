package com.github.recraftedcivilizations.darkcitizens.tasks.actions

/**
 * Register new actions here and get registered ones
 */
object ActionManager {
    val actions: MutableSet<IAction> = emptySet<IAction>().toMutableSet()

    fun registerAction(action: IAction){
        actions.add(action)
    }

    fun getAction(name: String?): IAction?{
        for (action in actions){
            if (action.name == name){
                return action
            }
        }
        return null
    }



}