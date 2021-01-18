package com.github.recraftedcivilizations.tasks

import com.github.darkvanityoflight.recraftedcore.utils.notifyutils.notify
import com.github.recraftedcivilizations.dPlayer.DPlayer
import com.github.recraftedcivilizations.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.tasks.actions.IAction
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player

class Task(
    override val name: String,
    override val income: Int,
    override val xp: Int,
    override val actions: List<IAction>,
    override val description: String,
    val dPlayerManager: DPlayerManager,
    val econ: Economy
) : ITask {


    override fun isCompletedForPlayer(player: DPlayer): Boolean {

        for (action in actions){
            if (!action.isCompletedForPlayer(player)){
                return false
            }
        }

        return true
    }

    override fun isCompletedForPlayer(player: Player): Boolean {
        return isCompletedForPlayer(dPlayerManager.getDPlayer(player)!!)
    }

    override fun completeForPlayer(player: DPlayer) {
        completeForPlayer(Bukkit.getPlayer(player.uuid)!!)
    }

    override fun completeForPlayer(player: Player) {
        val playerSet = setOf(player)
        notify("You completed the task $name", BarColor.GREEN, BarStyle.SOLID, 5, playerSet)
        pay(player)
    }

    override fun pay(player: DPlayer) {
        econ.depositPlayer(Bukkit.getPlayer(player.uuid), income.toDouble())
        player.addXP(player.job!!.group, xp)
    }

    override fun pay(player: Player) {
        pay(dPlayerManager.getDPlayer(player)!!)
    }
}