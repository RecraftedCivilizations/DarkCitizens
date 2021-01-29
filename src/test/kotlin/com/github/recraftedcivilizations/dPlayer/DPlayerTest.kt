package com.github.recraftedcivilizations.dPlayer

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.bukkit.entity.Player
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import java.util.*

internal class DPlayerTest {

    @Test
    fun serializeData() {
        val uuid = UUID(23423483952389, 3909432134)
        val dPlayerData = DPlayerData1(uuid, null, true, true, mapOf(Pair("Foo", 3), Pair("", 10)), mapOf(Pair("Foo", 3), Pair("", 10)))
        val dPlayer = DPlayer(dPlayerData)

        //TODO("Write an actual test if this is implemented")
        assertEquals("Foo", dPlayer.serializeData())
    }

    @Disabled("There is nothing to test here, (yet)")
    @Test
    fun joinJob() {
        TODO("Write test if implemented")
    }

    @Test
    fun addXP() {
        val uuid = UUID(23423483952389, 3909432134)
        val dPlayerData = DPlayerData1(uuid, null, true, true, mapOf(Pair("Foo", 3), Pair("", 10)), mapOf(Pair("Foo", 3), Pair("", 10)))
        val dPlayer = DPlayer(dPlayerData)

        dPlayer.addXP("Foo", 10)

        assertEquals(13, dPlayer.groupXps["Foo"])
    }

    @Test
    fun addXpToNonExistingGroup(){
        val uuid = UUID(23423483952389, 3909432134)
        val dPlayerData = DPlayerData1(uuid, null, true, true, mapOf(Pair("Foo", 3), Pair("", 10)), mapOf(Pair("Foo", 3), Pair("", 10)))
        val dPlayer = DPlayer(dPlayerData)

        dPlayer.addXP("FooBar", 10)

        assertEquals(10, dPlayer.groupXps["FooBar"])
    }
    fun shouldConstruct(){
        val uuid = UUID.randomUUID()

        // With DPlayerData1
        var dPlayerData : Any = DPlayerData1(uuid, null, true, true, mapOf(Pair("Foo", 3), Pair("", 10)), mapOf(Pair("Foo", 3), Pair("", 10)))
        var dPlayer = DPlayer(dPlayerData as DPlayerData1)
        assertEquals(dPlayerData.uuid, dPlayer.uuid)
        assertEquals(dPlayerData.job, dPlayer.job)
        assertEquals(dPlayerData.wanted, dPlayer.wanted )
        assertEquals(dPlayerData.isCriminal, dPlayer.isCriminal)
        assertEquals(dPlayerData.groupLvls, dPlayer.groupLvls)
        assertEquals(dPlayer.groupXps, dPlayer.groupXps)

        // With DPlayerData2
        val player: Player = mock(){ on { getUniqueId() } doReturn uuid }
        dPlayerData = DPlayerData2(null, true, true, mapOf(Pair("Foo", 3), Pair("", 10)), mapOf(Pair("Foo", 3), Pair("", 10)))
        dPlayer = DPlayer(dPlayerData as DPlayerData2, player)
        assertEquals(uuid, dPlayer.uuid)
        assertEquals(dPlayerData.job, dPlayer.job)
        assertEquals(dPlayerData.wanted, dPlayer.wanted )
        assertEquals(dPlayerData.isCriminal, dPlayer.isCriminal)
        assertEquals(dPlayerData.groupLvls, dPlayer.groupLvls)
        assertEquals(dPlayer.groupXps, dPlayer.groupXps)
    }
}