package com.github.recraftedcivilizations.darkcitizens.tasks

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.actions.IAction
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

/**
 * @author DarkVanityOfLight
 */

/**
 * Represents a task consisting of different actions of the type
 * [IAction], a name, a money reward and XP reward for completing and a
 * description
 */
interface ITask {
    val name: String
    val income: Int
    val xp: Int
    val actions: List<IAction>
    val description: String
    val icon: Material

    /**
     * Complete the task for a player and pay him out
     * This function will notify the player and call the [pay] function afterwards
     * @param player The [DPlayer] to complete for
     */
    fun isCompletedForPlayer(player: DPlayer): Boolean

    /**
     * Check if all actions are completed for a given player
     * @param player The player to check for
     */
    fun isCompletedForPlayer(player: Player): Boolean

    /**
     * Complete the task for a player and pay him out
     * This function will notify the player and call the [pay] function afterwards
     * @param player The [DPlayer] to complete for
     */
    fun completeForPlayer(player: DPlayer)

    /**
     * Complete the task for a player and pay him out
     * This function will notify the player and call the [pay] function afterwards
     * @param player The player to complete for
     */
    fun completeForPlayer(player: Player)

    /**
     * Pay out a player with XP and Money
     * @param player The [DPlayer] to pay out
     */
    fun pay(player: DPlayer)

    /**
     * Pay out a player with XP and Money
     * @param player The player to pay out
     */
    fun pay(player: Player)

    /**
     * Reset all actions in this task for a certain player
     * @param dPlayer The dPlayer to reset for
     */
    fun resetForPlayer(dPlayer: DPlayer)

    /**
     * Reset all actions in this task for a certain player
     * @param player The player to reset for
     */
    fun resetForPlayer(player: Player)

}