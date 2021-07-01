package com.github.recraftedcivilizations.darkcitizens.listeners

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.events.LevelUpEvent
import com.github.recraftedcivilizations.darkcitizens.events.XpGainEvent
import com.github.recraftedcivilizations.darkcitizens.groups.Group
import com.github.recraftedcivilizations.darkcitizens.groups.GroupManager
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import kotlin.random.Random
import kotlin.random.nextUInt

internal class InfoDisplayTest {

    private lateinit var dPlayerMock: DPlayer
    private lateinit var groupName: String
    private lateinit var groupManager: GroupManager
    private lateinit var bukkitWrapper: BukkitWrapper
    private lateinit var group: Group
    private lateinit var player: Player

    @BeforeEach
    fun refreshMocks(){

        dPlayerMock = mock<DPlayer> {
            on { groupXps } doReturn emptyMap<String, Int>().toMutableMap()
        }

        player = mock {  }

        groupName = "Foo"
        group = mock {
            on { lvlThreshold } doReturn (0..1000 step 50).toList()
        }
        groupManager = mock {
            on { getGroup(groupName) } doReturn group
        }
        bukkitWrapper = mock {
            on { getPlayer(dPlayerMock) } doReturn player
        }

    }


    @Test
    fun onXpGain() {
        val amount = 105
        val e = XpGainEvent(dPlayerMock, groupName, amount = amount)
        val l = InfoDisplay(groupManager, bukkitWrapper)

        l.onXpGain(e)

        verify(bukkitWrapper).notify("Xp: 0/50 + $amount", BarColor.BLUE, BarStyle.SOLID, 5, setOf(player))

    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun onLvlUp() {
        val newLvl = Random.nextUInt().toInt()
        val e = LevelUpEvent(dPlayerMock, groupName, newLvl)
        val l = InfoDisplay(groupManager, bukkitWrapper)

        l.onLvlUp(e)

        verify(bukkitWrapper).notify("Level Up\n $groupName level: $newLvl", BarColor.RED, BarStyle.SOLID, 5, setOf(player))

    }
}