package com.github.recraftedcivilizations.dPlayer

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
}