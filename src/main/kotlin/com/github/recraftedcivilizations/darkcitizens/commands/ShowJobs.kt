package com.github.recraftedcivilizations.darkcitizens.commands

import com.github.darkvanityoflight.recraftedcore.gui.InventoryGUI
import com.github.darkvanityoflight.recraftedcore.gui.elements.CloseButtonFactory
import com.github.darkvanityoflight.recraftedcore.utils.itemutils.addLore
import com.github.darkvanityoflight.recraftedcore.utils.itemutils.getName
import com.github.darkvanityoflight.recraftedcore.utils.itemutils.setName
import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.election.ElectionManager
import com.github.recraftedcivilizations.darkcitizens.gui.JobItem
import com.github.recraftedcivilizations.darkcitizens.jobs.ElectedJob
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ShowJobs(val jobManager: JobManager, val dPlayerManager: DPlayerManager, val electionManager: ElectionManager): CommandExecutor {
    private val jobGUI: InventoryGUI

    init {

        val jobs = jobManager.getJobs()

        // Make the number of jobs dividable by 9
        // +1 Because of the Close button
        var invSize = jobs.size + 1
        if(invSize % 9 != 0){
            invSize += (9 - (invSize % 9))
        }

        // Create a new GUI
        jobGUI = InventoryGUI(invSize, "Jobs")

        // Add all jobs to the gui
        for (job in jobs){
            val jobItemStack = ItemStack(job.icon, 1)
            jobItemStack.setName(job.name)
            jobItemStack.addLore("Group: ${job.group}")
            jobItemStack.addLore("Base Income: ${job.baseIncome}")
            jobItemStack.addLore("Base XP: ${job.baseXPGain}")
            jobItemStack.addLore("Minimum lvl: ${job.minLvl}")
            if (job is ElectedJob){
                jobItemStack.addLore("You have to be elected to join this job")
            }
            val displayItem = JobItem(jobItemStack, job, dPlayerManager, jobManager, electionManager)
            jobGUI.addItem(displayItem)
        }

        // Add the close button
        val closeButton = CloseButtonFactory.getCloseButton()
       jobGUI.setSlot(closeButton, invSize-1)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player){ sender.sendMessage("Fuck off console man!!"); return false }
        val dPlayer = dPlayerManager.getDPlayer(sender)!!

        // Mark the job the player currently has
        // Oh god what have I done, look at this, I guess I should comment this
        val gui = jobGUI.clone()
        // Loop through all DisplayItems
        for(pos in 0 until gui.getSize()){
            var item = gui.getSlot(pos)

            // If the item is not null and has the same name as the job the player currently has
            if (item != null){
                val name = item.itemStack.getName()
                if (name != null) {
                    if(name == dPlayer.job){
                        // Clone the item so only the owner can see the enchanted item stack
                        item = item.clone()
                        // Enchant the item
                        item.itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                        // Set the current slot to the cloned item
                        gui.setSlot(item, pos)
                    }
                }
            }
        }


        gui.show(sender)
        return true
    }
}