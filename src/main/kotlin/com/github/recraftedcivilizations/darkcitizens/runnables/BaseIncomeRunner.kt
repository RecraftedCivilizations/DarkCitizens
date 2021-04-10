package com.github.recraftedcivilizations.darkcitizens.runnables

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.groups.GroupManager
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.laws.LawManager
import net.milkbowl.vault.economy.Economy
import org.bukkit.scheduler.BukkitRunnable

/**
 * @author DarkVanityOfLight
 */

/**
 * Pay out the base income and xp after a specific time span
 * @param jobManager The JobManager to get all jobs to payout
 * @param playerManager The DPlayerManager to save the payout too
 * @param economy The economy to pay out too
 * @param groupManager The GroupManager to get the groups to pay out to
 * @param bukkitWrapper Optional bukkit wrapper debugging purposes only
 */
class BaseIncomeRunner(val jobManager: JobManager, private val playerManager: DPlayerManager, private val economy: Economy, private val groupManager: GroupManager, private val lawManager: LawManager, private val bukkitWrapper: BukkitWrapper = BukkitWrapper()) :
    BukkitRunnable() {

    /**
     * Pay out
     */
    override fun run() {
        val taxAmount = lawManager.getTaxAmount()
        // For every job
        for (job in jobManager.getJobs()){
            // Get Base money xp and all players that are in this job
            val money = job.baseIncome
            val xp = job.baseXPGain
            val players = job.currentMembers

            // For every player in the job
            for(player in players){
                // Pay that sucker
                val group = groupManager.getGroup(job.group)
                player.addXP(group!!, xp)
                // Calculate tax money and netto income
                val taxes = money * (taxAmount/100)
                val netto = money - taxes
                economy.depositPlayer(bukkitWrapper.getPlayer(player.uuid), netto.toDouble())
                economy.bankDeposit("CITYBANK", taxes.toDouble())
                playerManager.setDPlayer(player)
            }
        }
    }
}