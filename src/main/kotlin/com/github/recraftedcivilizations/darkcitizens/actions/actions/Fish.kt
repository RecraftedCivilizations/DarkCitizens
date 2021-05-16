package com.github.recraftedcivilizations.darkcitizens.actions.actions

import com.github.recraftedcivilizations.darkcitizens.actions.Action
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.inc
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

class Fish(val number: Int, override val name: String, override val description: String) : Action(), Listener {
    private val storage: MutableMap<UUID, Int> = emptyMap<UUID, Int>().toMutableMap()

    @EventHandler(ignoreCancelled = true)
    fun onFishing(e: PlayerFishEvent){
        if (e.state == PlayerFishEvent.State.CAUGHT_FISH){
            storage.inc(e.player.uniqueId)
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
}