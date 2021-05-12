package com.github.recraftedcivilizations.darkcitizens.tasks.actions

/**
 * @author DarkVanityOfLight
 */

/**
 * Register new actions here and get registered ones
 */
object ActionManager {
    private val actions: MutableSet<Any> = emptySet<Any>().toMutableSet()

    /**
     * Register a new action
     * @param action The action to register
     */
    fun registerAction(action: IAction){
        actions.add(action)
    }

    fun registerAction(actionWrapper: ActionWrapper<IAction>){
        actions.add(actionWrapper)
    }

    fun getAction(name: String?): IAction?{
        for (action in actions){
            if (action is IAction){
                if (action.name == name){
                    return action
                }
            }
        }
        return null
    }

    /**
     * Get a registered action
     * @param name The name of the action
     * @return The action wrapper or null if nothing is found
     */
    @Experimental
    fun experimentalGetAction(name: String?): ActionWrapper<*>? {
        for (action in actions){
            if (action is IAction){
                if (action.name == name){
                    return wrapAction(action)
                }
            }else if (action is ActionWrapper<*>){
                if (action.name == name){
                    return action
                }
            }else{
                return null
            }
        }
        return null
    }



}