package com.github.recraftedcivilizations.darkcitizens.tasks.actions

annotation class Experimental(val message: String = "This feature is experimental, use with caution, this may be removed without any notice in the future")

fun <T: IAction>wrapAction(action: T): ActionWrapper<T>{
    val a = ActionWrapper<T>(action.name)
    a.setAction(action)
    return a
}

/**
 * Wraps around an action making it possible to initialize a action later
 */
@Experimental
class ActionWrapper<T: IAction>(val name: String) {
    var initialized = false
        private set

    private lateinit var action: T

    fun setAction(action: T){
        if (!initialized){
            this.action = action
        }else{
            throw IllegalStateException("The action is already initialized")
        }
    }

    fun getAction(): T?{
        return if (initialized){
            action
        }else{
            null
        }
    }
}