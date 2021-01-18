package com.github.recraftedcivilizations.tasks

import com.github.recraftedcivilizations.dPlayer.DPlayerManager
import net.milkbowl.vault.economy.Economy

class TaskManager(val econ: Economy, val dPlayerManager: DPlayerManager) {
    val tasks: MutableSet<ITask> = emptySet<ITask>().toMutableSet()

    fun createTask(name: String, income: Int, xp: Int, action: List<String>, description: String){
        val task = TaskFactory.createTask(name, income, xp, action, description, dPlayerManager, econ)
        tasks.add(task)
    }
}