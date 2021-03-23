package com.github.recraftedcivilizations.darkcitizens.election

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import java.util.*
import kotlin.collections.HashMap

/**
 * @author DarkVanityOfLight
 */

/**
 * Represents an election that is held
 */
interface IElect{
    val electTime: Int
    val candidates: MutableSet<DPlayer>
    val votes: MutableMap<UUID, Int>
    val hasVoted: MutableSet<UUID>
    val job: IJob
    val voteFee: Int
    val candidateFee: Int

    /**
     * Gets called when the vote ends,
     * this should determine the winner and add him to the job he
     * was elected for
     * @return The [DPlayer] that was elected through the vote
     */
    fun evaluateVotes(): DPlayer

    /**
     * Add a vote for a candidate
     * @param uuid The UUID of the player to vote for
     */
    fun addVote(uuid: UUID)

    /**
     * Try voting for a player, remove fees and check if you are eligible to vote here
     * @param uuid The player to vote for
     * @param dPlayer The player who votes
     */
    fun vote(uuid: UUID, dPlayer: DPlayer)

    /**
     * Check if a player is eligible to vote
     * @param dPlayer The dPlayer to check for
     * @return true if the player is allowed to vote false if not
     */
    fun canVote(dPlayer: DPlayer): Boolean

    /**
     * Add a candidate to the election
     * @param dPlayer The candidate to add to the vote
     */
    fun addCandidate(dPlayer: DPlayer)

    /**
     * Check if a [DPlayer] is eligible for the job
     * @param dPlayer The [DPlayer] to check for
     * @return True if the player can be elected false if he lacks somethin
     */
    fun canCandidate(dPlayer: DPlayer): Boolean

    /**
     * Checks if the player can run for the job election, and add him to the election
     * @param dPlayer The dPlayer to add to the election
     */
    fun runFor(dPlayer: DPlayer)

}