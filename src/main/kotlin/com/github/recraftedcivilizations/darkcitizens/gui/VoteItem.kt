package com.github.recraftedcivilizations.darkcitizens.gui

import com.github.darkvanityoflight.recraftedcore.gui.Clickable
import com.github.darkvanityoflight.recraftedcore.gui.DisplayItem
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.election.IElect
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class VoteItem(itemStack: ItemStack, private val election: IElect, private val candidate: DPlayer, private val dPlayerManager: DPlayerManager) : Clickable(itemStack) {
    override fun clone(): DisplayItem {
        return VoteItem(itemStack, election, candidate, dPlayerManager)
    }

    override fun onClick(player: Player) {
       election.vote(candidate.uuid, dPlayerManager.getDPlayer(player)!!)
    }
}