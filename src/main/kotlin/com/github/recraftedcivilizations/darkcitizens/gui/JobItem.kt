package com.github.recraftedcivilizations.darkcitizens.gui

import com.github.darkvanityoflight.recraftedcore.gui.Clickable
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class JobItem(itemStack: ItemStack, val job: IJob, val dPlayerManager: DPlayerManager, val jobManager: JobManager) : Clickable(itemStack) {
    override fun onClick(player: Player) {
        val dPlayer = dPlayerManager.getDPlayer(player)

        dPlayer?.setJobManager(jobManager)
        dPlayer?.joinJob(job)
        //dPlayer?.let { dPlayerManager.setDPlayer(it) }
    }

}