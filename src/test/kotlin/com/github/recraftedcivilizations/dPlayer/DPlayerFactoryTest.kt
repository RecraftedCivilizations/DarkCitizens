package com.github.recraftedcivilizations.dPlayer

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerData1
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerFactory
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.bukkit.entity.Player
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

internal class DPlayerFactoryTest {
    val uuid = UUID.randomUUID()
    val dPlayerData =
        DPlayerData1(uuid, null, true, true, mapOf(Pair("Foo", 3), Pair("", 10)), mapOf(Pair("Foo", 3), Pair("", 10)))
    val player = mock<Player> { on { uniqueId } doReturn uuid }

    @Test
    fun createDPlayer() {
        val dPlayer = DPlayerFactory.createDPlayer(
            dPlayerData.uuid,
            dPlayerData.job,
            dPlayerData.wanted,
            dPlayerData.isCriminal,
            dPlayerData.groupLvls,
            dPlayerData.groupXps
        )
        assertEquals(dPlayerData.uuid, dPlayer.uuid)
        assertEquals(dPlayerData.job, dPlayer.job)
        assertEquals(dPlayerData.wanted, dPlayer.wanted)
        assertEquals(dPlayerData.isCriminal, dPlayer.isCriminal)
        assertEquals(dPlayerData.groupLvls, dPlayer.groupLvls)
        assertEquals(dPlayer.groupXps, dPlayer.groupXps)
    }

    @Test
    fun createDPLayer() {
        val dPlayer = DPlayerFactory.createDPlayer(
            dPlayerData.job,
            dPlayerData.wanted,
            dPlayerData.isCriminal,
            dPlayerData.groupLvls,
            dPlayerData.groupXps,
            player
        )
        assertEquals(dPlayerData.uuid, dPlayer.uuid)
        assertEquals(dPlayerData.job, dPlayer.job)
        assertEquals(dPlayerData.wanted, dPlayer.wanted)
        assertEquals(dPlayerData.isCriminal, dPlayer.isCriminal)
        assertEquals(dPlayerData.groupLvls, dPlayer.groupLvls)
        assertEquals(dPlayer.groupXps, dPlayer.groupXps)
    }

    @Test
    fun testCreateDPlayer() {
        val dPlayer = DPlayerFactory.createDPlayer(player)
    }
}