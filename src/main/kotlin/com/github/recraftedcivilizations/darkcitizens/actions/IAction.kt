package com.github.recraftedcivilizations.darkcitizens.actions

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import org.bukkit.entity.Player


/**
 * Represents an action for eg. mine 25 stone that a player
 * can finish.
 * A Simple Action consists of a name and a description,
 * the [isCompletedForPlayer] function should check if the player finished a task
 * If the player completed a task the action should emit an [ActionCompleteEvent],
 * the tasks will listen on this and check if all actions are complete to reward the player
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

    /**
     * Register this action at the [ActionManager]
     */
    fun register()

    /**
     * Reset this task for the given player
     * @param player The player to reset for
     */
    fun resetForPlayer(player: Player)

    /**
     * Reset this task for the given player
     * @param player The player to reset for
     */
    fun resetForPlayer(player: DPlayer)

    /**
     * Reset for one task completion
     * @param player The player to reset for
     */
    fun resetOneForPlayer(player: Player)

    /**
     * Reset for one task completion
     * @param player The player to reset for
     */
    fun resetOneForPlayer(player: DPlayer)
}