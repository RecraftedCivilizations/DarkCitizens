package com.github.recraftedcivilizations.darkcitizens.jobs.special.major

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.jobs.elected.ElectableJob
import com.github.recraftedcivilizations.darkcitizens.laws.LawManager
import com.github.recraftedcivilizations.darkcitizens.tasks.ITask
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material


class ElectedMajor(lawManager: LawManager,
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
                   leaveOnDeath: Boolean,
                   override val dPlayerManager: DPlayerManager,
                   jobManager: JobManager,
                   override val candidateTime: Int,
                   override val voteTime: Int,
                   override val voteFee: Int,
                   override val candidateFee: Int,
                   val bukkitWrapper: BukkitWrapper = BukkitWrapper(),
) : GenericMajor(
    lawManager,
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
), ElectableJob{
    /**
     * TODO("Maybe rework this")
     * Join this job, this does not check for any requirements
     * @param dPlayer The Player to join
     */
    override fun join(dPlayer: DPlayer) {
        val player = bukkitWrapper.getPlayer(dPlayer.uuid)!!
        addPlayer(dPlayer)
        player.sendMessage("${ChatColor.GREEN}You are now a $name")
    }

    /**
     * Leave the elected job, this will display a message to every player that a new election will take place
     */
    override fun leave(dPlayer: DPlayer) {
        val player = bukkitWrapper.getPlayer(dPlayer.uuid)!!
        super.leave(dPlayer)

        val players = Bukkit.getOnlinePlayers()
        for (i in players){
            i.sendMessage("${ChatColor.GREEN}The player ${player.name} left his job as ${name}. You can join the elections to become $name")
        }
    }
}