package com.github.recraftedcivilizations.darkcitizens.commands

import com.github.darkvanityoflight.recraftedcore.gui.DisplayItem
import com.github.darkvanityoflight.recraftedcore.gui.InventoryGUI
import com.github.darkvanityoflight.recraftedcore.gui.elements.CloseButtonFactory
import com.github.darkvanityoflight.recraftedcore.utils.itemutils.addLore
import com.github.darkvanityoflight.recraftedcore.utils.itemutils.setName
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.gui.TaskItem
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ShowTasks(val jobManager: JobManager, val dPlayerManager: DPlayerManager): CommandExecutor {
    private val taskGUIS = emptyMap<String, InventoryGUI>().toMutableMap()

    init {

        for (job in jobManager.getJobs()){
            val tasks = emptyList<DisplayItem>().toMutableList()
            for(task in job.tasks){
                val taskItemStack = ItemStack(Material.DIAMOND_PICKAXE)
                taskItemStack.setName(task.name)
                taskItemStack.addLore(task.description)
                taskItemStack.addLore("Income: ${task.income}")
                taskItemStack.addLore("Xp: ${task.xp}")
                val taskItem = TaskItem(taskItemStack, task)
                tasks.add(taskItem)
            }

            var invSize = jobManager.getJobs().size + 1
            if(invSize % 9 != 0){
                invSize += (9 - (invSize % 9))
            }

            val taskGUI = InventoryGUI(invSize, "Tasks")

            for (taskItem in tasks){
                taskGUI.addItem(taskItem)
            }
            taskGUI.addItem(CloseButtonFactory.getCloseButton())

            taskGUIS[job.name] = taskGUI

        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player){ sender.sendMessage("Fuck off console man!!"); return false }
        val dPlayer = dPlayerManager.getDPlayer(sender)

        if(dPlayer?.job != null){
            val gui = taskGUIS[dPlayer.job]
            if (gui != null) {
                gui.show(sender)
            }else{
                sender.sendMessage("${ChatColor.RED}This is awkward, the Task GUI for your job wasn't found, please report this error to someone who know what to do")
            }
        }else{
            sender.sendMessage("${ChatColor.RED}Get yourself a job before running this command!!")
        }
        return true
    }


}