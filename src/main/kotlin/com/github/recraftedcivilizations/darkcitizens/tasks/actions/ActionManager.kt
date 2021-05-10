package com.github.recraftedcivilizations.darkcitizens.tasks.actions

/**
 * @author DarkVanityOfLight
 */

/**
 * Register new actions here and get registered ones
 */
object ActionManager {
    val actions: MutableSet<IAction> = emptySet<IAction>().toMutableSet()

    /**
     * Register a new action
     * @param action The action to register
     */
    fun registerAction(action: IAction){
        actions.add(action)
    }

    /**
     * Get a registered action
     * @param name The name of the action
     * @return The action or null if nothing is found
     */
    fun getAction(name: String?): IAction?{
        for (action in actions){
            if (action.name == name){
                return action
            }
        }
        return null
    }



}