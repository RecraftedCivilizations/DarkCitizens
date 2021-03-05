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

        if (dPlayer != null) {
            job.join(dPlayer)
        }else{
            player.sendMessage("Yeah uhm this is kinda awkward, an unusual error occurred please try again or ask someone who has a clue")
            throw NullPointerException()
        }
    }

}