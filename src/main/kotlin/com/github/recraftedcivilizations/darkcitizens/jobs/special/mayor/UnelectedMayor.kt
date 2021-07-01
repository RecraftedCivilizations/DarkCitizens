package com.github.recraftedcivilizations.darkcitizens.jobs.special.mayor

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.laws.LawManager
import com.github.recraftedcivilizations.darkcitizens.tasks.ITask
import org.bukkit.Material

class UnelectedMayor(lawManager: LawManager,
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
                     prefix: String,
                     dPlayerManager: DPlayerManager,
                     jobManager: JobManager,
                     bukkitWrapper: BukkitWrapper = BukkitWrapper()
) : GenericMayor(
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
    prefix,
    dPlayerManager,
    jobManager,
    bukkitWrapper
) {

}