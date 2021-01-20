package com.github.recraftedcivilizations.jobs

import com.github.recraftedcivilizations.dPlayer.DPlayer
import com.github.recraftedcivilizations.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.tasks.ITask
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class Job(override val name: String,
          override val group: String,
          override val playerLimit: Int,
          override val currentMembers: Set<DPlayer>,
          override val tasks: Set<ITask>,
          override val canDemote: Set<String>,
          override val baseIncome: Int,
          override val baseXPGain: Int,
          override val minLvl: Int,
          override val electionRequired: Boolean,
          override val permissionRequired: Boolean,
          val dPlayerManager: DPlayerManager
          ): IJob {

    override fun removePlayer(player: DPlayer) {
        currentMembers.minus(player)
    }

    override fun removePlayer(player: Player) {
        removePlayer(dPlayerManager.getDPlayer(player)!!)
    }

    override fun addPlayer(player: DPlayer) {
        if (canJoin(player)){
            currentMembers.plus(player)
        }else{
            Bukkit.getPlayer(player.uuid)?.sendMessage("${ChatColor.RED}You lack the permissions to join this job!")
        }
    }

    override fun addPlayer(player: Player) {
        addPlayer(dPlayerManager.getDPlayer(player)!!)
    }

    override fun canJoin(player: DPlayer): Boolean {
        return canJoin(Bukkit.getPlayer(player.uuid)!!)
    }

    override fun canJoin(player: Player): Boolean {
        // This checks if the job  requires permissions if the player has these permissions and if the player limit is already reached
        return (!permissionRequired || permissionRequired && player.hasPermission("drp.job.join.$name")) && currentMembers.size < playerLimit
    }

}
