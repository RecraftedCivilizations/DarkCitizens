package com.github.recraftedcivilizations.darkcitizens.commands


import com.github.darkvanityoflight.recraftedcore.gui.InventoryGUI
import com.github.darkvanityoflight.recraftedcore.utils.itemutils.setName
import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.gui.DemoteItem
import com.github.recraftedcivilizations.darkcitizens.gui.VoteItem
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

class Demote(private val dPlayerManager: DPlayerManager, private val jobManager: JobManager, private val bukkitWrapper: BukkitWrapper = BukkitWrapper()): CommandExecutor {

    /**
     * Demote a member from a job
     * <br></br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true if a valid command, otherwise false
     */
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Fuck off console man!!"); return false
        }

        // Check that there is a target name
        if (args.isEmpty()) {
            sender.sendMessage("${ChatColor.RED}You need to define a player to demote!")
            return false
        }
        val senderJob = jobManager.getJob(dPlayerManager.getDPlayer(sender)?.job)

        // Check if the demote arg is a job if yes display all members and make it
        // possible to demote them
        val demoteJob = jobManager.getJob(args[0])
        if (demoteJob != null){
            if (senderJob != null){
                if (demoteJob.name in senderJob.canDemote){

                    val invGui = InventoryGUI(demoteJob.currentMembers.size, "Demote")

                    for (member in demoteJob.currentMembers){
                        val toDemotePlayer = bukkitWrapper.getPlayer(member)
                        // Set the head skin
                        val displayItemStack = ItemStack(Material.PLAYER_HEAD, 1)
                        val skullMeta = displayItemStack.itemMeta as SkullMeta
                        skullMeta.owningPlayer = toDemotePlayer
                        displayItemStack.itemMeta = skullMeta

                        displayItemStack.setName(toDemotePlayer?.name)

                        val displayItem = DemoteItem(displayItemStack, member, demoteJob)
                        invGui.addItem(displayItem)
                    }

                    invGui.show(sender)
                    return true
                }
            }
        }


        // Get the target
        val target = bukkitWrapper.getPlayer(args[0])

        // Check that there is a target
        if (target == null) {
            sender.sendMessage("${ChatColor.RED}Could not find player ${args[0]}")
            return true
        }

        // Get the jobs
        val targetJob = dPlayerManager.getDPlayer(target)?.job


        // Demote if the permissions are okay
        if (targetJob != null) {
            if (senderJob != null) {
                if (targetJob in senderJob.canDemote) {
                    target.sendMessage("${ChatColor.RED}You got demoted by ${sender.name}")
                    jobManager.getJob(targetJob)?.leave(target)
                    return true
                }
            }
        }
        sender.sendMessage("${ChatColor.RED}You can't demote ${args[0]}")
        return true
    }
}