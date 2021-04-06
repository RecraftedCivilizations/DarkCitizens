package com.github.recraftedcivilizations.darkcitizens.jobs.special.major

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.GenericJob
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.laws.LawManager
import com.github.recraftedcivilizations.darkcitizens.tasks.ITask
import org.bukkit.Material

class UnelectedMajor(lawManager: LawManager,
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
) {

}