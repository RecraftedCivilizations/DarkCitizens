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
    val candidates: Set<DPlayer>
    val votes: HashMap<UUID, Int>
    val hasVoted: Set<UUID>
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
     * Vote for a certain candidate, this should check if the player is eligible to vote
     * and remove fees for the vote
     * @param uuid The UUID of the player to vote for
     */
    fun vote(uuid: UUID)

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