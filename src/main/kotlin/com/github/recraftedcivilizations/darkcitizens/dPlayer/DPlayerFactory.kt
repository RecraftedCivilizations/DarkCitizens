package com.github.recraftedcivilizations.darkcitizens.dPlayer

import org.bukkit.entity.Player
import java.util.*

/**
 * @author DarkVanityOfLight
 */

/**
 * Create a new DPlayer according to the given arguments
 */
object DPlayerFactory {

    /**
     * Create a new player with arguments of the form of [DPlayerData1]
     * For the arguments take a look at DPlayerData1
     * @see [DPlayerData1]
     */
    fun createDPlayer(uuid: UUID, job: String?, wanted: Boolean,isCriminal: Boolean, groupLvls: Map<String, Int>, groupXps: Map<String, Int>): DPlayer {
        val dData = DPlayerData1(uuid, job, wanted ,isCriminal , groupLvls , groupXps)
        return DPlayer(dData)
    }

    /**
     * Create a new player with arguments of the form of [DPlayerData2]
     * For the arguments take a look at DPlayerData2
     * @see [DPlayerData2]
     */
    fun createDPlayer(job: String?, wanted: Boolean,isCriminal: Boolean, groupLvls: Map<String, Int>, groupXps: Map<String, Int>, player: Player): DPlayer {
        val dData = DPlayerData2(job, wanted ,isCriminal , groupLvls , groupXps)
        return DPlayer(dData, player)
    }

    /**
     * Create a new empty DPlayer based on a player
     * @param player The player to create the DPlayer for
     */
    fun createDPlayer(player: Player): DPlayer {
        return DPlayer(player)
    }
}