package com.github.recraftedcivilizations.darkcitizens.tasks.actions

abstract class Action: IAction {

    override fun register(){
        ActionManager.registerAction(this)
    }
}