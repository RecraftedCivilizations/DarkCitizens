package com.github.recraftedcivilizations.dPlayer

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerData1
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerData2
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.bukkit.entity.Player
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.*

internal class DPlayerTest {

    @Test
    fun serializeData() {
        val uuid = UUID(23423483952389, 3909432134)
        val dPlayerData = DPlayerData1(
            uuid,
            null,
            true,
            true,
            mapOf(Pair("Foo", 3), Pair("", 10)),
            mapOf(Pair("Foo", 3), Pair("", 10))
        )
        val dPlayer = DPlayer(dPlayerData)

        assertEquals(dPlayerData, dPlayer.serializeData())
    }

    @Disabled("There is nothing to test here, (yet)")
    @Test
    fun joinJob() {
        TODO("Write test if implemented")
    }

    @Test
    fun addXP() {
        val uuid = UUID(23423483952389, 3909432134)
        val dPlayerData = DPlayerData1(
            uuid,
            null,
            true,
            true,
            mapOf(Pair("Foo", 3), Pair("", 10)),
            mapOf(Pair("Foo", 3), Pair("", 10))
        )
        val dPlayer = DPlayer(dPlayerData)

        dPlayer.addXP("Foo", 10)

        assertEquals(13, dPlayer.groupXps["Foo"])
    }

    @Test
    fun addXpToNonExistingGroup() {
        val uuid = UUID(23423483952389, 3909432134)
        val dPlayerData = DPlayerData1(
            uuid,
            null,
            true,
            true,
            mapOf(Pair("Foo", 3), Pair("", 10)),
            mapOf(Pair("Foo", 3), Pair("", 10))
        )
        val dPlayer = DPlayer(dPlayerData)

        dPlayer.addXP("FooBar", 10)

        assertEquals(10, dPlayer.groupXps["FooBar"])
    }

    @Test
    fun shouldConstruct() {
        val uuid = UUID.randomUUID()

        // With DPlayerData1
        var dPlayerData: Any = DPlayerData1(
            uuid,
            null,
            true,
            true,
            mapOf(Pair("Foo", 3), Pair("", 10)),
            mapOf(Pair("Foo", 3), Pair("", 10))
        )
        var dPlayer = DPlayer(dPlayerData as DPlayerData1)
        assertEquals(dPlayerData.uuid, dPlayer.uuid)
        assertEquals(dPlayerData.job, dPlayer.job)
        assertEquals(dPlayerData.wanted, dPlayer.wanted)
        assertEquals(dPlayerData.isCriminal, dPlayer.isCriminal)
        assertEquals(dPlayerData.groupLvls, dPlayer.groupLvls)
        assertEquals(dPlayer.groupXps, dPlayer.groupXps)

        // With DPlayerData2
        val player: Player = mock { on { uniqueId } doReturn uuid }
        dPlayerData =
            DPlayerData2(null, true, true, mapOf(Pair("Foo", 3), Pair("", 10)), mapOf(Pair("Foo", 3), Pair("", 10)))
        dPlayer = DPlayer(dPlayerData, player)
        assertEquals(uuid, dPlayer.uuid)
        assertEquals(dPlayerData.job, dPlayer.job)
        assertEquals(dPlayerData.wanted, dPlayer.wanted)
        assertEquals(dPlayerData.isCriminal, dPlayer.isCriminal)
        assertEquals(dPlayerData.groupLvls, dPlayer.groupLvls)
        assertEquals(dPlayer.groupXps, dPlayer.groupXps)

        // With player
        dPlayer = DPlayer(player)
        assertEquals(uuid, dPlayer.uuid)
        assertEquals(false, dPlayer.wanted)
        assertEquals(false, dPlayer.isCriminal)
        assertEquals(emptyMap<String, Int>().toMutableMap(), dPlayer.groupLvls)
        assertEquals(emptyMap<String, Int>().toMutableMap(), dPlayer.groupXps)
    }
}