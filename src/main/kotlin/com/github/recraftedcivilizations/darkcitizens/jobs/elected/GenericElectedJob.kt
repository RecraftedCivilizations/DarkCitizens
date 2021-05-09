package com.github.recraftedcivilizations.darkcitizens.jobs.elected

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.GenericJob
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.tasks.ITask
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material

open class GenericElectedJob(
    override val leaveOnDeath: Boolean,
    override val candidateTime: Int,
    override val voteTime: Int,
    override val voteFee: Int,
    override val candidateFee: Int,
    override val dPlayerManager: DPlayerManager,
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
    prefix: String,
    private val jobManager: JobManager,
    val bukkitWrapper: BukkitWrapper = BukkitWrapper()
): ElectableJob, GenericJob(
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
    prefix,
    dPlayerManager,
    jobManager,
    bukkitWrapper
) {
    /**
     * Join this job, this does not check for any requirements
     * @param dPlayer The Player to join
     */
    override fun join(dPlayer: DPlayer) {
        // Leave the old job, ugly ik
        dPlayer.job?.let { jobManager.getJob(it) }?.leave(dPlayer)
        this.addPlayer(dPlayer)
        dPlayer.job = name
        bukkitWrapper.getPlayer(dPlayer)?.sendMessage("${ChatColor.GREEN}You successfully joined the job $name")
        dPlayerManager.setDPlayer(dPlayer)
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