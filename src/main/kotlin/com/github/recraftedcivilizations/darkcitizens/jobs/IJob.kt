package com.github.recraftedcivilizations.darkcitizens.jobs

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.tasks.ITask
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

/**
 * @author DarkVanityOfLight
 *
 */
/**
 * Represents a job consisting of a [group], a [name], a [playerLimit],
 * a set of [DPlayer] representing [currentMembers], a the [tasks] set containing [ITask],
 * a [canDemote] set of other job names they can demote player from, a [baseIncome], a [baseXPGain],
 * a [minLvl] required to join the job, if an election is required([electionRequired]) and if you need a permission
 * to join the job([permissionRequired])
 */
interface IJob {
    val group: String
    val name: String
    val playerLimit: Int
    val currentMembers: Set<DPlayer>
    val tasks: Set<ITask>
    val canDemote: Set<String>
    val baseIncome: Int
    val baseXPGain: Int
    val minLvl: Int
    val electionRequired: Boolean
    val permissionRequired: Boolean
    val icon: Material

    fun setBukkitWrapper(bukkitWrapper: BukkitWrapper)

    /**
     * Remove a player of type [DPlayer] from the job,
     * should be called on demote or leaving the job.
     * @param player The player to be removed
     */
    fun removePlayer(player: DPlayer)

    /**
     * Remove a player of type [Player] from the job,
     * should be called on demote or leaving the job.
     * @param player The player to be removed
     */
    fun removePlayer(player: Player)

    /**
     * Add a player of type [DPlayer] to the job, should only be called by the [join] method or if you want to force
     * join a player.
     * @param player The player to add
     */
    fun addPlayer(player: DPlayer)

    /**
     * Add a player of type [Player] to the job, should only be called by the [join] method or if you want to force
     * join a player.
     * @param player The player to add
     */
    fun addPlayer(player: Player)

    /**
     * Check if a player of type [DPlayer] can join a job, this should check for permissions and whether the job is
     * already full
     * @param player The player to check for
     */
    fun canJoin(player: DPlayer): Boolean

    /**
     * Check if a player of type [Player] can join a job, this should check for permissions and whether the job is
     * already full
     * @param player The player to check for
     */
    fun canJoin(player: Player): Boolean

    /**
     * Check if a player of type [DPlayer] is a member of this job
     * @param player The player to check for
     */
    fun isMember(player: DPlayer): Boolean

    /**
     * Check if a player of type [Player] is a member of this job
     * @param player The player to check for
     */
    fun isMember(player: Player): Boolean

    /**
     * Check if a player represented through his UUID is a member of this job
     * @param uuid The uuid to check for
     */
    fun isMember(uuid: UUID): Boolean

    /**
     * Handles the leaving of this job
     * @param dPlayer The dPlayer to leave this job
     */
    fun leave(dPlayer: DPlayer)

    /**
     * Handles the leaving of this job
     * @param player The player to leave this job
     */
    fun leave(player: Player)

    /**
     * Handles the joining of this job, use this to let a player join a job,
     * all necessary steps will be taken such as leaving the old job or checking if the
     * player is actually able to join this job
     * @param dPlayer The dPlayer to join this job
     */
    fun join(dPlayer: DPlayer)


    /**
     * Handles the joining of this job, use this to let a player join a job,
     * all necessary steps will be taken such as leaving the old job or checking if the
     * player is actually able to join this job
     * @param player The player to join this job
     */
    fun join(player: Player)
}