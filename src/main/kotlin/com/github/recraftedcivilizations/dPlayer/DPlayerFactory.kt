package com.github.recraftedcivilizations.dPlayer

import com.github.recraftedcivilizations.jobs.IJob
import org.bukkit.entity.Player
import java.util.*

object DPlayerFactory {

    fun createDPlayer(uuid: UUID, job: IJob?, wanted: Boolean,isCriminal: Boolean, groupLvls: MutableMap<String, Int>, groupXps: MutableMap<String, Int>): DPlayer{
        val dData = DPlayerData1(uuid, job, wanted ,isCriminal , groupLvls , groupXps)
        return DPlayer(dData)
    }

    fun createDPLayer(job: IJob?, wanted: Boolean,isCriminal: Boolean, groupLvls: MutableMap<String, Int>, groupXps: MutableMap<String, Int>, player: Player): DPlayer{
        val dData = DPlayerData2(job, wanted ,isCriminal , groupLvls , groupXps)
        return DPlayer(dData, player)
    }

    fun createDPlayer(player: Player): DPlayer{
        return DPlayer(player)
    }
}