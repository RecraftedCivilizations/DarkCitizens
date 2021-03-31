package com.github.recraftedcivilizations.darkcitizens.jobs

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.tasks.ITask
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material

class ElectedJob(
    name: String,
    group: String,
    playerLimit: Int,
    tasks: Set<ITask>,
    canDemote: Set<String>,
    baseIncome: Int,
    baseXPGain: Int,
    minLvl: Int,
    permissionRequired: Boolean,
    icon: Material,
    override val leaveOnDeath: Boolean,
    val candidateTime: Int,
    val voteTime: Int,
    val voteFee: Int,
    val candidateFee: Int,
    val dPlayerManager: DPlayerManager,
    jobManager: JobManager,
    private val bukkitWrapper: BukkitWrapper = BukkitWrapper()
) : GenericJob(
    name,
    group,
    playerLimit,
    tasks,
    canDemote,
    baseIncome,
    baseXPGain,
    minLvl,
    permissionRequired,
    icon,
    leaveOnDeath,
    dPlayerManager,
    jobManager,
    bukkitWrapper
) {
    override fun join(dPlayer: DPlayer) {
        val player = bukkitWrapper.getPlayer(dPlayer.uuid)!!
        addPlayer(dPlayer)
        player.sendMessage("${ChatColor.GREEN}You are now a $name")
    }

    override fun leave(dPlayer: DPlayer) {
        val player = bukkitWrapper.getPlayer(dPlayer.uuid)!!
        super.leave(dPlayer)

        val players = Bukkit.getOnlinePlayers()
        for (i in players){
            i.sendMessage("${ChatColor.GREEN}The player ${player.name} left his job as ${name}. You can join the elections to become $name")
        }
    }
}