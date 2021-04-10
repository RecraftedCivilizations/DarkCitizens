package com.github.recraftedcivilizations.darkcitizens.election

/**
 * @author DarkVanityOfLight
 */

/**
 * All state an election can be in
 */
enum class ElectionStates {
    /**
     * Candidate state means that people can join the election as candidate
     */
    CANDIDATE,

    /**
     * Vote state means that people can vote right now
     */
    VOTE,

    /**
     * Ended the election has ended, there is no usage for this yet
     */
    ENDED,
}