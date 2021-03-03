package com.github.recraftedcivilizations.darkcitizens.dPlayer

import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import org.bukkit.entity.Player
import java.util.*

/**
 * @author DarkVanityOfLight
 */

fun <T> MutableMap<T, Int>.inc(key: T, more: Int = 1) = merge(key, more, Int::plus)

/**
 * This data class represents the data of a DPlayer,
 * This is used to parse a DPlayer
 * @param uuid The UUID of the player
 * @param job The current Job
 * @param wanted If the player is wanted
 * @param isCriminal If the player is criminal
 * @param groupLvls All groups and their current lvl
 * @param groupXps All groups and their current XP
 */
data class DPlayerData1(val uuid: UUID, val job: String?, val wanted: Boolean, val isCriminal: Boolean, val groupLvls: Map<String, Int>, val groupXps: Map<String, Int>){
    fun toDPlayerData2(): DPlayerData2 {
        return DPlayerData2(job, wanted, isCriminal, groupLvls, groupXps)
    }
}
/**
 * This data class represents the data of a DPlayer,
 * This is used to parse a DPlayer
 * @param job The current Job
 * @param wanted If the player is wanted
 * @param isCriminal If the player is criminal
 * @param groupLvls All groups and their current lvl
 * @param groupXps All groups and their current XP
 */
data class DPlayerData2(val job: String?, val wanted: Boolean, val isCriminal: Boolean, val groupLvls: Map<String, Int>, val groupXps: Map<String, Int>){
    fun toDPlayerData1(uuid: UUID): DPlayerData1 {
        return DPlayerData1(uuid, job, wanted, isCriminal, groupLvls, groupXps)
    }
}


/**
 * Represents a DarkRP player, it is used to store data associated with a player
 * like its job or lvls and to perform actions on this data
 */
class DPlayer {
    val uuid: UUID
    var job: String? = null
    var wanted: Boolean = false
    var isCriminal: Boolean = false
    val groupLvls: MutableMap<String, Int> = emptyMap<String, Int>().toMutableMap()
    val groupXps: MutableMap<String, Int> = emptyMap<String, Int>().toMutableMap()
    private lateinit var jobManager: JobManager

    /**
     * Constructs an empty new DPlayer this should only be used
     * the first time a player joins
     * @param player The player to construct for
     */
    constructor(player: Player){
        uuid = player.uniqueId
    }

    /**
     * Constructs a DPlayer from parsed data in the form of [DPlayerData1]
     * @param data DPlayer data in the form of [DPlayerData1]
     */
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

    /**
     * Constructs a DPlayer from parsed data in the form of [DPlayerData2] and a player
     * @param data DPlayer data in the form of [DPlayerData2]
     * @param player The player to construct for
     */
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

    fun setJobManager(jobManager: JobManager){
        this.jobManager = jobManager
    }

    /**
     * Serialize this DPlayer to a [DPlayerData1]
     */
    fun serializeData(): DPlayerData1 {
        return DPlayerData1(uuid, job, wanted, isCriminal, groupLvls, groupXps)
    }

    /**
     * Join a new job, before calling this method make sure you called [setJobManager]
     * @param job The Job to join
     */
    fun joinJob(job: IJob){

        val canJoin = job.canJoin(this)
        if (canJoin){
            if(this.job != null) jobManager.getJob(this.job!!)?.removePlayer(this)
            job.addPlayer(this)
        }
    }

    /**
     * Add XP to a player and check if his lvl improves
     * @param group The group to add the XP to
     * @param amount The amount of XP to add
     */
    fun addXP(group: String, amount: Int){
        groupXps.inc(group, amount)
        //TODO Check if lvl increases
    }

}