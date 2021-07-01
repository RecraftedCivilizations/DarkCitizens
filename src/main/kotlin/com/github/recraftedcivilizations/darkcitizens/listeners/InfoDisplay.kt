package com.github.recraftedcivilizations.darkcitizens.listeners

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.events.LevelUpEvent
import com.github.recraftedcivilizations.darkcitizens.events.XpGainEvent
import com.github.recraftedcivilizations.darkcitizens.groups.GroupManager
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class InfoDisplay(private val groupManager: GroupManager, private val bukkitWrapper: BukkitWrapper = BukkitWrapper()) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onXpGain(e: XpGainEvent){

        val player = bukkitWrapper.getPlayer(e.player)?: return
        val group = groupManager.getGroup(e.group)!!

        val xp = e.player.groupXps[e.group]?:0
        var totalXp = xp + e.amount

        var lvl = 0
        while (totalXp > group.lvlThreshold[lvl]){
            lvl++
        }
        if (lvl != 0){
            totalXp -= group.lvlThreshold[lvl]
        }


        var xpNextLvl = group.lvlThreshold[lvl+1]
        for(lvlXp in group.lvlThreshold.subList(0, lvl)){
            xpNextLvl -= lvlXp
        }

        bukkitWrapper.notify("Xp: $xp/$xpNextLvl + ${e.amount}", BarColor.BLUE, BarStyle.SOLID, 5, setOf(player))
    }

    @EventHandler(ignoreCancelled = true)
    fun onLvlUp(e: LevelUpEvent){

        val player = bukkitWrapper.getPlayer(e.dPlayer)?: return
        val group = groupManager.getGroup(e.group)

        bukkitWrapper.notify("Level Up\n ${e.group} level: ${e.newLvl}", BarColor.RED, BarStyle.SOLID, 5, setOf(player))

    }
}