package com.github.recraftedcivilizations.darkcitizens.commands

import com.github.darkvanityoflight.recraftedcore.gui.InventoryGUI
import com.github.darkvanityoflight.recraftedcore.gui.elements.CloseButtonFactory
import com.github.darkvanityoflight.recraftedcore.utils.itemutils.addLore
import com.github.darkvanityoflight.recraftedcore.utils.itemutils.setName
import com.github.recraftedcivilizations.darkcitizens.election.ElectionManager
import com.github.recraftedcivilizations.darkcitizens.election.GUIElection
import com.github.recraftedcivilizations.darkcitizens.gui.ElectionItem
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack

class Vote(private val electionManager: ElectionManager): CommandExecutor {

    private fun createGui(): InventoryGUI{
        val elections = electionManager.getElections()


        // Make the number of jobs dividable by 9
        // +1 Because of the Close button
        var invSize = elections.size + 1
        if(invSize % 9 != 0){
            invSize += (9 - (invSize % 9))
        }

        val gui = InventoryGUI(invSize, "Elections")

        for (election in elections){
            if (election is GUIElection){
                val icon = election.job.icon
                val itemStack = ItemStack(icon, 1)

                itemStack.setName(election.job.name)
                itemStack.addLore("Vote Fee: ${election.voteFee}")
                itemStack.addLore("Candidate Fee: ${election.candidateFee}")

                val displayItem = ElectionItem(election , itemStack)
                gui.addItem(displayItem)
            }
        }

        gui.setSlot(CloseButtonFactory.getCloseButton(), invSize-1)
        return gui
    }

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {

        return true
    }
}