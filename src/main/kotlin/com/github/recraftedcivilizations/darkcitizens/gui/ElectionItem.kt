package com.github.recraftedcivilizations.darkcitizens.gui

import com.github.darkvanityoflight.recraftedcore.gui.Clickable
import com.github.darkvanityoflight.recraftedcore.gui.DisplayItem
import com.github.recraftedcivilizations.darkcitizens.election.GUIElection
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ElectionItem(val election: GUIElection, itemStack: ItemStack) : Clickable(itemStack){
    override fun clone(): DisplayItem {
        return ElectionItem(election, itemStack)
    }

    override fun onClick(player: Player) {
        election.invGUI.show(player)
    }

}