package com.github.recraftedcivilizations.darkcitizens.actions

abstract class Action: IAction {

    override fun register(){
        ActionManager.registerAction(this)
    }
}