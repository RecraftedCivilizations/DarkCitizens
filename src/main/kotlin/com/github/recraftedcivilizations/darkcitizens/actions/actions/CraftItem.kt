package com.github.recraftedcivilizations.darkcitizens.actions.actions

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.actions.Action
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.dPlayer.inc
import com.github.recraftedcivilizations.darkcitizens.events.ActionCompleteEvent
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

/**
 * @author DarkVanity
 * TODO("Reset only the required number of items")
 */

class CraftItem(override val name: String, override val description: String, val number: Int, val itemType: Material, private val dPlayerManager: DPlayerManager, private val bukkitWrapper: BukkitWrapper = BukkitWrapper()) : Action(), Listener {
    private val storage: MutableMap<UUID, Int> = emptyMap<UUID, Int>().toMutableMap()

    @EventHandler(ignoreCancelled = true)
    fun onCrafting(e: CraftItemEvent){
        val crafter = e.whoClicked
        if (crafter !is Player) return

        val dPlayer = dPlayerManager.getDPlayer(crafter)!!

        if (e.slotType == InventoryType.SlotType.RESULT){
            val result = e.recipe.result
            if (result.type == itemType) {
                storage.inc(crafter.uniqueId, result.amount)
                val surplus = (storage[crafter.uniqueId] ?: 0) - number
                if (surplus >= 0) {
                    val completeEvent = ActionCompleteEvent(dPlayer, this)
                    bukkitWrapper.getPluginManager().callEvent(completeEvent)
                }
            }
        }

    }

    @EventHandler(ignoreCancelled = true)
    fun playerLeave(e: PlayerQuitEvent) {
        storage.remove(e.player.uniqueId)
    }


    override fun isCompletedForPlayer(player: DPlayer): Boolean {
        return isCompletedForPlayer(player.uuid)
    }

    override fun isCompletedForPlayer(player: Player): Boolean {
        return isCompletedForPlayer(player.uniqueId)
    }

    private fun isCompletedForPlayer(uuid: UUID): Boolean{
        return storage[uuid]?:0 >= number
    }

    override fun resetForPlayer(player: Player) {
        resetForPlayer(player.uniqueId)
    }

    override fun resetForPlayer(player: DPlayer) {
        resetForPlayer(player.uuid)
    }

    private fun resetForPlayer(uuid: UUID){
        storage[uuid] = 0
    }

    override fun resetOneForPlayer(player: DPlayer) {
        resetOneForPlayer(player.uuid)
    }

    override fun resetOneForPlayer(player: Player) {
        resetOneForPlayer(player.uniqueId)
    }

    private fun resetOneForPlayer(uuid: UUID){

        if (storage[uuid]?:0 >= number){
            storage[uuid]!!.minus(number)
        }
    }
}