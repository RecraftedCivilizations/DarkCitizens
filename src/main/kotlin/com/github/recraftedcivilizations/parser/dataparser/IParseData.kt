package com.github.recraftedcivilizations.parser.dataparser

import com.github.recraftedcivilizations.dPlayer.DPlayer
import com.github.recraftedcivilizations.dPlayer.DPlayerData1
import com.github.recraftedcivilizations.dPlayer.DPlayerData2
import java.util.*

interface IParseData {
    fun getDPlayer(playerUUID: UUID): DPlayer?
    fun setDPlayer(dPlayer: DPlayer)
    fun setDPlayer(dData: DPlayerData1)
    fun setDPlayer(dData: DPlayerData2, playerUUID: UUID)
}