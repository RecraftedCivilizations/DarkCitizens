package com.github.recraftedcivilizations.darkcitizens.jobs.special.mayor


import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.GenericJob
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.laws.LawManager
import com.github.recraftedcivilizations.darkcitizens.tasks.ITask
import org.bukkit.Material

abstract class GenericMayor(override val lawManager: LawManager,
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
                            dPlayerManager: DPlayerManager,
                            jobManager: JobManager,
                            bukkitWrapper: BukkitWrapper = BukkitWrapper()
): IMayor, GenericJob(
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

    override fun createLaw(name: String, description: String) {
        lawManager.createLaw(name, description)
    }

    override fun removeLaw(name: String) {
        lawManager.removeLaw(name)
    }

    override fun setTaxLaw(amount: Int) {
        lawManager.setTaxAmount(amount)
    }
}