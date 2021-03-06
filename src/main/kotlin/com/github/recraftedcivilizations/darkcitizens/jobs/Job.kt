package com.github.recraftedcivilizations.darkcitizens.jobs

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.tasks.ITask
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

/**
 * @author DarkVanityOfLight
 */



/**
 * The default implementation for a job,
 * @see IJob
 * @constructor Construct using a [name], a [group] the job belongs,
 * a [playerLimit] a [tasks] set consisting of [ITask], a [canDemote] set consisting
 * of other job names they can demote, a [baseIncome], a [baseXPGain], a [minLvl] to join the job, if
 * an election is required [electionRequired], if permissions are required to join the job([permissionRequired]) and a
 * [DPlayerManager]. The [bukkitWrapper] is only for testing purposes and should not be passed
 */
class Job(
    override val name: String,
    override val group: String,
    override val playerLimit: Int,
    override val tasks: Set<ITask>,
    override val canDemote: Set<String>,
    override val baseIncome: Int,
    override val baseXPGain: Int,
    override val minLvl: Int,
    override val permissionRequired: Boolean,
    override val icon: Material,
    override val leaveOnDeath: Boolean,
    private val dPlayerManager: DPlayerManager,
    private val jobManager: JobManager,
    private var bukkitWrapper: BukkitWrapper = BukkitWrapper(),
) : GenericJob(name,
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

    override fun setBukkitWrapper(bukkitWrapper: BukkitWrapper) {
        super.setBukkitWrapper(bukkitWrapper)
        this.bukkitWrapper = bukkitWrapper
    }

}
