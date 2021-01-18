package com.github.recraftedcivilizations.dPlayer

import com.github.recraftedcivilizations.parser.dataparser.IParseData
import org.bukkit.entity.Player
import java.util.*

class DPlayerManager(val dataParser: IParseData) {

    fun getDPlayer(uuid: UUID): DPlayer?{
        return dataParser.getDPlayer(uuid)
    }

    fun getDPlayer(player: Player): DPlayer?{
        return getDPlayer(player.uniqueId)
    }


}