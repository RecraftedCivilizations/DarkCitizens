package com.github.recraftedcivilizations.darkcitizens.gui

import com.github.darkvanityoflight.recraftedcore.gui.Clickable
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class JobItem(itemStack: ItemStack, val job: IJob, val dPlayerManager: DPlayerManager) : Clickable(itemStack) {
    override fun onClick(player: Player) {
        val dPlayer = dPlayerManager.getDPlayer(player)
        dPlayer?.joinJob(job)
    }

}