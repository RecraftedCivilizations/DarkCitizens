package com.github.recraftedcivilizations.darkcitizens.election

import com.github.darkvanityoflight.recraftedcore.gui.InventoryGUI
import com.github.darkvanityoflight.recraftedcore.gui.elements.CloseButtonFactory
import com.github.darkvanityoflight.recraftedcore.utils.itemutils.setName
import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.gui.VoteItem
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import net.milkbowl.vault.economy.Economy
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

class GUIElection(
    electTime: Int,
    candidates: MutableSet<DPlayer> = emptySet<DPlayer>().toMutableSet(),
    votes: MutableMap<UUID, Int>,
    hasVoted: MutableSet<UUID>,
    job: IJob,
    voteFee: Int,
    candidateFee: Int,
    dPlayerManager: DPlayerManager,
    economy: Economy,
    val bukkitWrapper: BukkitWrapper
) : GenericElection(electTime, candidates, votes, hasVoted, job, voteFee, candidateFee, dPlayerManager, economy, bukkitWrapper) {
    var invGUI = InventoryGUI(9,"Election for ${job.name}")

    override fun addCandidate(dPlayer: DPlayer) {
        super.addCandidate(dPlayer)
        updateGUI()
    }

    /**
     * Display the vote GUI that enables player to vote
     * @param player The player to show the gui to
     */
    fun display(player: Player){
        invGUI.show(player)
    }

    @EventHandler
    override fun onLeave(e: PlayerQuitEvent) {
        super.onLeave(e)
        updateGUI()
    }

    /**
     * Update the GUI size and all candidates
     */
    private fun updateGUI(){

        // Make sure our inv size is dividable by 9
        // + 1 because of the close button
        var invSize = candidates.size + 1
        if(invSize % 9 != 0){
            invSize += (9 - (invSize % 9))
        }

        // Create the new GUI with the updated inv size
        invGUI = InventoryGUI(invSize, "Election for ${job.name}")

        // Add all candidates to the GUI
        for(candidate in candidates){
            val candidatePlayer = bukkitWrapper.getPlayer(candidate)

            // Set the head skin
            val displayItemStack = ItemStack(Material.PLAYER_HEAD, 1)
            val skullMeta = displayItemStack.itemMeta as SkullMeta
            skullMeta.owningPlayer = candidatePlayer
            displayItemStack.itemMeta = skullMeta

            displayItemStack.setName(candidatePlayer?.name)

            val displayItem = VoteItem(displayItemStack, this, candidate, dPlayerManager)
            invGUI.addItem(displayItem)
        }

        val closeButton = CloseButtonFactory.getCloseButton()
        invGUI.setSlot(closeButton, invSize-1)
        invGUI.updateInventory()
    }

}