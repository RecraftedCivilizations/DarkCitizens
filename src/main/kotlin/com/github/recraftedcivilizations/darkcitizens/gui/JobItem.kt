package com.github.recraftedcivilizations.darkcitizens.gui

import com.github.darkvanityoflight.recraftedcore.gui.Clickable
import com.github.darkvanityoflight.recraftedcore.gui.DisplayItem
import com.github.darkvanityoflight.recraftedcore.gui.GUIManager
import com.github.darkvanityoflight.recraftedcore.gui.InventoryGUI
import com.github.darkvanityoflight.recraftedcore.utils.itemutils.setName
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.election.ElectionManager
import com.github.recraftedcivilizations.darkcitizens.jobs.ElectedJob
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

private class YesItem(itemStack: ItemStack, val job: IJob) : Clickable(itemStack) {
    override fun clone(): DisplayItem {
        return YesItem(itemStack, job)
    }

    override fun onClick(player: Player) {
        job.leave(player)
        GUIManager.forceClose(player)
        player.sendMessage("You left the job ${job.name}")
    }
}

private class NoItem(itemStack: ItemStack): Clickable(itemStack){
    override fun clone(): DisplayItem {
        return NoItem(itemStack)
    }

    override fun onClick(player: Player) {
        GUIManager.forceClose(player)
    }

}

class JobItem(itemStack: ItemStack, val job: IJob, val dPlayerManager: DPlayerManager, val jobManager: JobManager, val electionManager: ElectionManager) : Clickable(itemStack) {
    val leaveJobGUI: InventoryGUI = InventoryGUI(9, "Do you want to leave your job?")

    init {
        val greenWool = ItemStack(Material.GREEN_WOOL)
        greenWool.setName("Yes")
        val redWool = ItemStack(Material.RED_WOOL)
        redWool.setName("No")
        leaveJobGUI.setSlot(YesItem(greenWool, job), 3)
        leaveJobGUI.setSlot(NoItem(redWool), 5)
    }

    override fun clone(): DisplayItem {
        return JobItem(itemStack.clone(), job, dPlayerManager, jobManager, electionManager)
    }


    override fun onClick(player: Player) {
        val dPlayer = dPlayerManager.getDPlayer(player)

        if (dPlayer != null) {
            if (job.isMember(dPlayer)){
                leaveJobGUI.show(player)
            }else{
                if(job !is ElectedJob){
                    job.join(dPlayer)
                }else{
                    // Check if an election is running
                    if (!electionManager.isRunningElection(job)){
                        // If not create one
                        if(job.canJoin(dPlayer)){
                            electionManager.createElection(job)
                        }
                    }
                    // Try to add the player to the election
                    val election = electionManager.getElection(job)!!
                    election.runFor(dPlayer)
                }
            }
        }else{
            player.sendMessage("Yeah uhm this is kinda awkward, an unusual error occurred please try again or ask someone who has a clue")
            throw NullPointerException()
        }
    }

}