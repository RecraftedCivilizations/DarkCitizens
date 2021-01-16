package com.github.recraftedcivilizations.DPlayer

import com.github.recraftedcivilizations.jobs.IJob
import org.bukkit.entity.Player
import java.util.*

data class DPlayerData1(val uuid: UUID, val job: IJob?, val wanted: Boolean, val isCriminal: Boolean, val groupLvls: MutableMap<String, Int>, val groupXps: MutableMap<String, Int>)
data class DPlayerData2(val job: IJob?, val wanted: Boolean, val isCriminal: Boolean, val groupLvls: MutableMap<String, Int>, val groupXps: MutableMap<String, Int>)

class DPlayer {
    val uuid: UUID
    var job: IJob? = null
    var wanted: Boolean = false
    var isCriminal: Boolean = false
    val groupLvls: MutableMap<String, Int> = emptyMap<String, Int>().toMutableMap()
    val groupXps: MutableMap<String, Int> = emptyMap<String, Int>().toMutableMap()

    constructor(player: Player){
        uuid = player.uniqueId
    }

    constructor(data: DPlayerData1){
        uuid = data.uuid
        job = data.job
        wanted = data.wanted
        isCriminal = data.isCriminal

        for (field in data.groupLvls.keys){
            groupLvls[field] = data.groupLvls[field]!!
        }

        for (field in data.groupXps.keys){
            groupXps[field] = data.groupXps[field]!!
        }

    }

    constructor(data: DPlayerData2, player: Player){
        uuid = player.uniqueId
        job = data.job
        wanted = data.wanted
        isCriminal = data.isCriminal

        for (field in data.groupLvls.keys){
            groupLvls[field] = data.groupLvls[field]!!
        }

        for (field in data.groupXps.keys){
            groupXps[field] = data.groupXps[field]!!
        }

    }

    fun serializeData(): String { return "Foo" }

    fun joinJob(job: IJob){}


    fun addXP(group: String, amount: Int){
        groupXps[group]?.plus(amount)

        //TODO Check if lvl increases
    }

}