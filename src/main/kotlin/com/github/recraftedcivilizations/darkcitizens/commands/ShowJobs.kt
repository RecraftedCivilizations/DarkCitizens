package com.github.recraftedcivilizations.darkcitizens.commands

import com.github.darkvanityoflight.recraftedcore.gui.InventoryGUI
import com.github.darkvanityoflight.recraftedcore.gui.elements.CloseButtonFactory
import com.github.darkvanityoflight.recraftedcore.utils.itemutils.addLore
import com.github.darkvanityoflight.recraftedcore.utils.itemutils.setName
import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.gui.JobItem
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ShowJobs(val jobManager: JobManager, val dPlayerManager: DPlayerManager, bukkitWrapper: BukkitWrapper = BukkitWrapper()): CommandExecutor {
    private val jobGUI: InventoryGUI

    init {

        val jobs = jobManager.getJobs()

        // Make the number of jobs dividable by 9
        // +1 Because of the Close button
        var invSize = jobs.size + 1
        if(invSize % 9 != 0){
            invSize += (9 - (jobs.size % 9))
        }

        // Create a new GUI
        jobGUI = InventoryGUI(invSize, "Jobs")

        // Add all jobs to the gui
        for (job in jobs){
            val jobItemStack = ItemStack(Material.PLAYER_HEAD, 1)
            jobItemStack.setName(job.name)
            jobItemStack.addLore("Group: ${job.group}")
            jobItemStack.addLore("Base Income: ${job.baseIncome}")
            jobItemStack.addLore("Base XP: ${job.baseXPGain}")
            jobItemStack.addLore("Minimum lvl: ${job.minLvl}")
            jobItemStack.addLore("Elected: ${job.electionRequired}")
            val displayItem = JobItem(jobItemStack, job, dPlayerManager)
            jobGUI.addItem(displayItem)
        }

        // Add the close button
        val closeButton = CloseButtonFactory.getCloseButton()
       jobGUI.setSlot(closeButton, invSize)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player){ sender.sendMessage("Fuck off console man!!"); return false }
        jobGUI.show(sender)
        return true
    }
}