package com.github.recraftedcivilizations.darkcitizens.gui

import com.github.darkvanityoflight.recraftedcore.gui.Clickable
import com.github.darkvanityoflight.recraftedcore.gui.DisplayItem
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class DemoteItem(itemStack: ItemStack, val owner: DPlayer, val job: IJob) : Clickable(itemStack) {
    override fun clone(): DisplayItem {
        return DemoteItem(itemStack, owner, job)
    }

    override fun onClick(player: Player) {
        job.leave(this.owner)
    }
}