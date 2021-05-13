package com.github.recraftedcivilizations.darkcitizens.tasks

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.events.ActionCompleteEvent
import com.github.recraftedcivilizations.darkcitizens.events.TaskCompleteEvent
import com.github.recraftedcivilizations.darkcitizens.groups.GroupManager
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.tasks.actions.IAction
import net.milkbowl.vault.economy.Economy
import org.bukkit.Material
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

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
    override val icon: Material,
    private val dPlayerManager: DPlayerManager,
    private val econ: Economy,
    private val jobManager: JobManager,
    private val groupManager: GroupManager,
    private val bukkitWrapper: BukkitWrapper = BukkitWrapper(),
) : ITask, Listener {

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
        bukkitWrapper.notify("You completed the task $name", BarColor.GREEN, BarStyle.SOLID, 5, playerSet)
        pay(player)

        for (action in this.actions){
            action.resetForPlayer(player)
        }
    }

    /**
     * Pay out a player with XP and Money
     * @param player The [DPlayer] to pay out
     */
    override fun pay(player: DPlayer) {
        econ.depositPlayer(bukkitWrapper.getPlayer(player.uuid), income.toDouble())
        // Yeahh... get the group obj
        val group = jobManager.getJob(player.job!!)?.group?.let { groupManager.getGroup(it) }
        player.addXP(group!!, xp)
    }

    /**
     * Pay out a player with XP and Money
     * @param player The player to pay out
     */
    override fun pay(player: Player) {
        pay(dPlayerManager.getDPlayer(player)!!)
    }

    @EventHandler(ignoreCancelled = true)
    fun onActionComplete(e: ActionCompleteEvent){
        if (e.action in this.actions){

            if (isCompletedForPlayer(e.dPlayer)){
                val taskEvent = TaskCompleteEvent(e.dPlayer, this)
                bukkitWrapper.getPluginManager().callEvent(taskEvent)
            }

        }
    }
}