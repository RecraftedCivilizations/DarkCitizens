package com.github.recraftedcivilizations.darkcitizens.gui

import com.github.darkvanityoflight.recraftedcore.gui.Clickable
import com.github.darkvanityoflight.recraftedcore.gui.DisplayItem
import com.github.recraftedcivilizations.darkcitizens.election.GUIElection
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author DarkVanityOfLight
 */

/**
 * Represents an [GUIElection] as Item in a GUI
 * @param election The election to represent
 * @param itemStack The ItemStack to show
 */
class ElectionItem(val election: GUIElection, itemStack: ItemStack) : Clickable(itemStack){
    override fun clone(): DisplayItem {
        return ElectionItem(election, itemStack)
    }

    /**
     * Show the gui of the election
     * @param player The player to show the GUI to
     */
    override fun onClick(player: Player) {
        election.display(player)
    }

}