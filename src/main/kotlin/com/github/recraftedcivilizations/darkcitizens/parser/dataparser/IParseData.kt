package com.github.recraftedcivilizations.darkcitizens.parser.dataparser

import com.github.recraftedcivilizations.darkcitizens.recraftedcivilizations.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.recraftedcivilizations.dPlayer.DPlayerData1
import com.github.recraftedcivilizations.darkcitizens.recraftedcivilizations.dPlayer.DPlayerData2
import java.util.*

/**
 * @author DarkVanityOfLight
 */

/**
 * Represents a DataParser and all data it needs to parse
 */
interface IParseData {
    /**
     * Get a DPlayer from the DataSource
     * @param playerUUID The UUID of the player you want to get
     */
    fun getDPlayer(playerUUID: UUID): DPlayer?

    /**
     * Set a DPlayer in the DataSource
     * @param dPlayer The player to save to the DataSource
     */
    fun setDPlayer(dPlayer: DPlayer)

    /**
     * Set a DPlayer from dPlayerData of the form [DPlayerData1]
     * @param dData The dPlayerData of the player
     */
    fun setDPlayer(dData: DPlayerData1)

    /**
     * Set a DPlayer from dPlayerData of the for [DPlayerData2] and the player UUID
     * @param dData The dPlayerData of the player
     * @param playerUUID The UUID of the player
     */
    fun setDPlayer(dData: DPlayerData2, playerUUID: UUID)
}