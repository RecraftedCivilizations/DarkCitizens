package com.github.recraftedcivilizations.darkcitizens.dPlayer

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.groups.Group
import com.github.recraftedcivilizations.darkcitizens.groups.GroupManager
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
    private var bukkitWrapper = BukkitWrapper()
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

    @Deprecated("The job manager is no longer required due to the deprecation of joinJob")
    fun setJobManager(jobManager: JobManager){
        this.jobManager = jobManager
    }

    fun setBukkitWrapper(bukkitWrapper: BukkitWrapper){
        this.bukkitWrapper = bukkitWrapper
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
    @Deprecated("Do not use this method to join a job. Use the join method in the job itself instead")
    fun joinJob(job: IJob){

        val canJoin = job.canJoin(this)
        if (canJoin){
            if(this.job != null) jobManager.getJob(this.job!!)?.removePlayer(this)
            job.addPlayer(this)
            this.job = job.name
            bukkitWrapper.getPlayer(uuid)?.sendMessage("You successfully joined the job ${job.name}")
        }
    }

    /**
     * Add XP to a player and check if his lvl improves
     * @param group The group to add the XP to
     * @param amount The amount of XP to add
     */
    fun addXP(group: Group, amount: Int){
        groupXps.inc(group.name, amount)

        var maxLvl = 0
        val currentXp = groupXps[group.name]!!

        for (lvlThreshold in group.lvlThreshold){
            if (currentXp >= lvlThreshold){
                maxLvl ++
                break
            }
        }

        groupLvls[group.name] = maxLvl
    }

}