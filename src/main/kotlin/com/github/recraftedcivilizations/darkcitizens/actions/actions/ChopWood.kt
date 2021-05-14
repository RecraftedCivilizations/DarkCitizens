package com.github.recraftedcivilizations.darkcitizens.actions.actions

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.actions.Action
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.dPlayer.inc
import com.github.recraftedcivilizations.darkcitizens.events.ActionCompleteEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

class ChopWood(override val name: String, override val description: String, val number: Int, private val dPlayerManager: DPlayerManager, private val bukkitWrapper: BukkitWrapper = BukkitWrapper()) : Action() {
    private val storage: MutableMap<UUID, Int> = emptyMap<UUID, Int>().toMutableMap()
    private val woodRegex = Regex("\\w+_WOOD\$")

    @EventHandler(ignoreCancelled = true)
    fun blockBreak(e: BlockBreakEvent) {

        val dPlayer = dPlayerManager.getDPlayer(e.player.uniqueId)!!

        val isWood = woodRegex.matches(e.block.type.name)

        if (isWood) {
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
        return storage[uuid] == number
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
}