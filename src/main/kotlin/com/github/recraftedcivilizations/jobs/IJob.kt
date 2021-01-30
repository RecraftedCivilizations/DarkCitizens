package com.github.recraftedcivilizations.jobs

import com.github.recraftedcivilizations.dPlayer.DPlayer
import com.github.recraftedcivilizations.tasks.ITask
import org.bukkit.entity.Player

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
     * Add a player of type [DPlayer] to the job, should be called on joining a job.
     * @param player The player to add
     */
    fun addPlayer(player: DPlayer)

    /**
     * Add a player of type [Player] to the job, should be called on joining a job.
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

}