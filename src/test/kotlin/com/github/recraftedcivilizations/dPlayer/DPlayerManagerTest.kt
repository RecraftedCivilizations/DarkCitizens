package com.github.recraftedcivilizations.dPlayer


import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerData1
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.parser.dataparser.IParseData
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.bukkit.entity.Player
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.util.*

internal class DPlayerManagerTest {
    val uuid = UUID.randomUUID()
    val mockPlayer = mock<Player> {on { getUniqueId() } doReturn uuid}
    val dPlayerData = DPlayerData1(
        uuid,
        null,
        true,
        true,
        mapOf(Pair("Foo", 3), Pair("", 10)),
        mapOf(Pair("Foo", 3), Pair("", 10))
    )
    val mockDPlayer = DPlayer(dPlayerData)
    val dataParser = mock<IParseData> { on { getDPlayer(uuid) } doReturn mockDPlayer}

    @Test
    fun getDPlayer() {
        val dPlayerManager = DPlayerManager(dataParser)
        val dplayer = dPlayerManager.getDPlayer(uuid)
        assertEquals(mockDPlayer, dplayer)
    }

    @Test
    fun testGetDPlayer() {
        val dPlayerManager = DPlayerManager(dataParser)
        val dplayer = dPlayerManager.getDPlayer(mockPlayer)
        assertEquals(mockDPlayer, dplayer)
    }

    @Test
    fun shouldConstruct(){
        val dPlayerManager = DPlayerManager(dataParser)
    }
}