package com.github.recraftedcivilizations.darkcitizens.gui

import com.github.darkvanityoflight.recraftedcore.gui.Clickable
import com.github.darkvanityoflight.recraftedcore.gui.GUIManager
import com.github.darkvanityoflight.recraftedcore.gui.InventoryGUI
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

private class YesItem(itemStack: ItemStack, val job: IJob) : Clickable(itemStack) {
    override fun onClick(player: Player) {
        job.leave(player)
        GUIManager.forceClose(player)
    }
}

private class NoItem(itemStack: ItemStack): Clickable(itemStack){
    override fun onClick(player: Player) {
        GUIManager.forceClose(player)
    }

}

class JobItem(itemStack: ItemStack, val job: IJob, val dPlayerManager: DPlayerManager, val jobManager: JobManager) : Clickable(itemStack) {
    val leaveJobGUI: InventoryGUI = InventoryGUI(9, "Do you want to leave your current job")

    init {
        leaveJobGUI.setSlot(YesItem(ItemStack(Material.GREEN_WOOL), job), 3)
        leaveJobGUI.setSlot(NoItem(ItemStack(Material.RED_WOOL)), 5)
    }


    override fun onClick(player: Player) {
        val dPlayer = dPlayerManager.getDPlayer(player)

        if (dPlayer != null) {
            if (job.isMember(dPlayer)){
                leaveJobGUI.show(player)
            }else{
                job.join(dPlayer)
            }
        }else{
            player.sendMessage("Yeah uhm this is kinda awkward, an unusual error occurred please try again or ask someone who has a clue")
            throw NullPointerException()
        }
    }

}