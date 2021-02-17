package com.github.recraftedcivilizations.darkcitizens.tasks.actions

import com.github.recraftedcivilizations.darkcitizens.recraftedcivilizations.dPlayer.DPlayer
import org.bukkit.entity.Player


/**
 * Represents an action for eg. mine 25 stone that a player
 * can finish.
 * A Simple Action consists of a name and a description,
 * the [isCompletedForPlayer] function should check if the player finished a task
 */
interface IAction {
    val name: String
    val description: String

    /**
     * Check if a player completed this task
     * @param player The [DPlayer] to check for
     */
    fun isCompletedForPlayer(player: DPlayer): Boolean

    /**
     * Check if a player completed this task
     * @param player The player to check for
     */
    fun isCompletedForPlayer(player: Player): Boolean
}