package com.github.recraftedcivilizations.tasks.actions

import com.github.recraftedcivilizations.dPlayer.DPlayer
import org.bukkit.entity.Player

interface IAction {
    val name: String
    val description: String

    fun isCompletedForPlayer(player: DPlayer): Boolean
    fun isCompletedForPlayer(player: Player): Boolean
}