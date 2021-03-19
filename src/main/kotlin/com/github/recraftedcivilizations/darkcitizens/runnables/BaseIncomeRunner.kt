package com.github.recraftedcivilizations.darkcitizens.runnables

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.groups.GroupManager
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import net.milkbowl.vault.economy.Economy
import org.bukkit.scheduler.BukkitRunnable

class BaseIncomeRunner(val jobManager: JobManager, private val playerManager: DPlayerManager, private val economy: Economy, private val groupManager: GroupManager, private val bukkitWrapper: BukkitWrapper = BukkitWrapper()) :
    BukkitRunnable() {
    override fun run() {

        for (job in jobManager.getJobs()){
            val money = job.baseIncome
            val xp = job.baseXPGain
            val players = job.currentMembers

            for(player in players){
                val group = groupManager.getGroup(job.group)
                player.addXP(group!!, xp)
                economy.depositPlayer(bukkitWrapper.getPlayer(player.uuid), money.toDouble())
                playerManager.setDPlayer(player)
            }
        }
    }
}