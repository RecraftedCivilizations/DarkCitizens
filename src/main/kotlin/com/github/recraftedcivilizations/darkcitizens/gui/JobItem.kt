package com.github.recraftedcivilizations.darkcitizens.gui

import com.github.darkvanityoflight.recraftedcore.gui.Clickable
import com.github.darkvanityoflight.recraftedcore.gui.DisplayItem
import com.github.darkvanityoflight.recraftedcore.gui.GUIManager
import com.github.darkvanityoflight.recraftedcore.gui.InventoryGUI
import com.github.darkvanityoflight.recraftedcore.utils.itemutils.setName
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.election.ElectionManager
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.jobs.elected.GenericElectedJob
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author DarkVanityOfLight
 */

/**
 * Represents an Yes answer when leaving the job
 * @param itemStack The ItemStack this should be represented as
 * @param job The job this leave question is for
 */
private class YesItem(itemStack: ItemStack, val job: IJob) : Clickable(itemStack) {
    override fun clone(): DisplayItem {
        return YesItem(itemStack, job)
    }

    /**
     * Leave the job
     */
    override fun onClick(player: Player) {
        job.leave(player)
        GUIManager.forceClose(player)
        player.sendMessage("You left the job ${job.name}")
    }
}

/**
 * Represents an No answer when leaving the job
 * @param itemStack The ItemStack this should be represented as
 */
private class NoItem(itemStack: ItemStack): Clickable(itemStack){
    override fun clone(): DisplayItem {
        return NoItem(itemStack)
    }

    /**
     * Just close the current GUI for the player
     * @param player The player to close the gui for
     */
    override fun onClick(player: Player) {
        GUIManager.forceClose(player)
    }

}

/**
 * Represents an [IJob] as an Item in an InventoryGUI
 * @param itemStack The ItemStack this job should be represented as
 * @param job The job this should represent
 * @param dPlayerManager The DPlayerManager to get data from
 * @param jobManager The JobManager this job belongs to
 * @param electionManager The Election manager to trigger a new election if necessary
 */
class JobItem(itemStack: ItemStack, val job: IJob, val dPlayerManager: DPlayerManager, val jobManager: JobManager, val electionManager: ElectionManager) : Clickable(itemStack) {
    private val leaveJobGUI: InventoryGUI = InventoryGUI(9, "Do you want to leave your job?")

    // Create the Leave Job GUI
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


    /**
     * Join/leave the job
     * @see Clickable
     */
    override fun onClick(player: Player) {
        val dPlayer = dPlayerManager.getDPlayer(player)

        if (dPlayer != null) {
            // If the player is already a member show the leave gui
            if (job.isMember(dPlayer)){
                leaveJobGUI.show(player)
            }else{
                // If not and the job doesn't require an election
                // Join the job the job.join method will check if the player can actually join the job
                if(job !is GenericElectedJob){
                    job.join(dPlayer)
                }else{
                    // If the Job requires an election
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