package com.github.recraftedcivilizations.darkcitizens.actions.actions


import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.actions.Action
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.dPlayer.inc
import com.github.recraftedcivilizations.darkcitizens.events.ActionCompleteEvent
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import kotlin.reflect.KClass

/**
 * @author DarkVanityOfLight
 */

/**
 * Mine a specific number of blocks of a specific type
 * @param block The block to mine
 * @param number The number of blocks to mine
 * @param description The description of this action
 * @param name The name of this action
 */
class MineBlock (override val name: String, override val description: String, val block: Material, val number: Int, private val dPlayerManager: DPlayerManager, private val bukkitWrapper: BukkitWrapper = BukkitWrapper()): Listener, Action() {
    private val storage: MutableMap<UUID, Int> = mutableMapOf()


    @EventHandler(ignoreCancelled = true)
    fun blockBreak(e: BlockBreakEvent) {
        val dPlayer = dPlayerManager.getDPlayer(e.player.uniqueId)!!
        if (e.block.type == block) {
            storage.inc(e.player.uniqueId)
        }

        if (storage[e.player.uniqueId]!! >= number){
            val actionCompleteEvent = ActionCompleteEvent(dPlayer, this)
            bukkitWrapper.getPluginManager().callEvent(actionCompleteEvent)
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