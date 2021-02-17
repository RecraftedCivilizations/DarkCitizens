package com.github.recraftedcivilizations.darkcitizens.dPlayer

import com.github.recraftedcivilizations.darkcitizens.parser.dataparser.IParseData
import org.bukkit.entity.Player
import java.util.*

/**
 * @author DarkVanityOfLight
 */

/**
 * This is used to get DPlayers from the data parser
 * @constructor Construct a new DPlayerManager using  [dataParser]
 */
class DPlayerManager(private val dataParser: IParseData) {

    /**
     * Get a DPlayer using an UUID
     * @param uuid The UUID of the player you want
     */
    fun getDPlayer(uuid: UUID): DPlayer?{
        return dataParser.getDPlayer(uuid)
    }

    /** Get a DPlayer using his Minecraft Player equivalent
     * @param player The player you want to get the DPlayer for
     */
    fun getDPlayer(player: Player): DPlayer?{
        return getDPlayer(player.uniqueId)
    }

    fun setDPlayer(dPlayer: DPlayer){
        dataParser.setDPlayer(dPlayer)
    }


}