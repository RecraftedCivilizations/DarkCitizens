package com.github.recraftedcivilizations.tasks

import com.github.recraftedcivilizations.dPlayer.DPlayer
import com.github.recraftedcivilizations.tasks.actions.IAction
import org.bukkit.entity.Player

interface ITask {
    val name: String
    val income: Int
    val xp: Int
    val actions: List<IAction>
    val description: String

    fun isCompletedForPlayer(player: DPlayer): Boolean
    fun isCompletedForPlayer(player: Player): Boolean
    fun completeForPlayer(player: DPlayer)
    fun completeForPlayer(player: Player)
    fun pay(player: DPlayer)
    fun pay(player: Player)
}