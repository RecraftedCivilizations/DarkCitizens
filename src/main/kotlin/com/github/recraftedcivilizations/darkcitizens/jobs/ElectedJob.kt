package com.github.recraftedcivilizations.darkcitizens.jobs

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.tasks.ITask
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
    val candidateTime: Int,
    val voteTime: Int,
    val voteFee: Int,
    val candidateFee: Int,
    dPlayerManager: DPlayerManager,
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
    dPlayerManager,
    jobManager,
    bukkitWrapper
) {
    override fun join(dPlayer: DPlayer) {
        val player = bukkitWrapper.getPlayer(dPlayer.uuid)!!
        addPlayer(dPlayer)
        player.sendMessage("${ChatColor.GREEN}You are now a $name")
    }
}