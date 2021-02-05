package com.github.recraftedcivilizations.tasks

import com.github.darkvanityoflight.recraftedcore.utils.notifyutils.notify
import com.github.recraftedcivilizations.BukkitWrapper
import com.github.recraftedcivilizations.dPlayer.DPlayer
import com.github.recraftedcivilizations.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.tasks.actions.IAction
import net.milkbowl.vault.economy.Economy
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player

/**
 * @author DarkVanityOfLight
 */


/**
 * A standard Task
 * @see [ITask]
 * @param dPlayerManager The player manager
 * @param econ The economy to pay out too
 */
class Task(
    override val name: String,
    override val income: Int,
    override val xp: Int,
    override val actions: List<IAction>,
    override val description: String,
    val dPlayerManager: DPlayerManager,
    val econ: Economy,
    private val bukkitWrapper: BukkitWrapper = BukkitWrapper()
) : ITask {

    init {
        if (actions.isEmpty()){
            bukkitWrapper.warning("The Task $name has no actions assigned!")
        }
    }

    /**
     * Check if all actions are completed for a given player
     * @param player The [DPlayer] to check for
     */
    override fun isCompletedForPlayer(player: DPlayer): Boolean {

        for (action in actions) {
            if (!action.isCompletedForPlayer(player)) {
                return false
            }
        }

        return actions.isNotEmpty()
    }

    /**
     * Check if all actions are completed for a given player
     * @param player The player to check for
     */
    override fun isCompletedForPlayer(player: Player): Boolean {
        return isCompletedForPlayer(dPlayerManager.getDPlayer(player)!!)
    }

    /**
     * Complete the task for a player and pay him out
     * This function will notify the player and call the [pay] function afterwards
     * @param player The [DPlayer] to complete for
     */
    override fun completeForPlayer(player: DPlayer) {
        completeForPlayer(bukkitWrapper.getPlayer(player)!!)
    }

    /**
     * Complete the task for a player and pay him out
     * This function will notify the player and call the [pay] function afterwards
     * @param player The player to complete for
     */
    override fun completeForPlayer(player: Player) {
        val playerSet = setOf(player)
        notify("You completed the task $name", BarColor.GREEN, BarStyle.SOLID, 5, playerSet)
        pay(player)
    }

    /**
     * Pay out a player with XP and Money
     * @param player The [DPlayer] to pay out
     */
    override fun pay(player: DPlayer) {
        econ.depositPlayer(bukkitWrapper.getPlayer(player.uuid), income.toDouble())
        player.addXP(player.job!!.group, xp)
    }

    /**
     * Pay out a player with XP and Money
     * @param player The player to pay out
     */
    override fun pay(player: Player) {
        pay(dPlayerManager.getDPlayer(player)!!)
    }
}