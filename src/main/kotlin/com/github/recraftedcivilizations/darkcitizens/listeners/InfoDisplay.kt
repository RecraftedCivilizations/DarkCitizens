package com.github.recraftedcivilizations.darkcitizens.listeners

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.events.XpGainEvent
import com.github.recraftedcivilizations.darkcitizens.groups.GroupManager
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class InfoDisplay(private val groupManager: GroupManager, private val bukkitWrapper: BukkitWrapper = BukkitWrapper()) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onXpGain(e: XpGainEvent){

        val player = bukkitWrapper.getPlayer(e.player)!!
        val group = groupManager.getGroup(e.group)!!

        var totalXp = e.player.groupXps[e.group]!! + e.amount

        var lvl = 0
        for (cost in group.lvlThreshold){

            if (totalXp - cost > 0){
                totalXp -= cost
            }else{
                break
            }
            lvl++
        }


        val xpNextLvl = group.lvlThreshold[lvl + 1]
        bukkitWrapper.notify("Xp: $totalXp/$xpNextLvl + ${e.amount}", BarColor.BLUE, BarStyle.SOLID, 5, setOf(player))
    }
}