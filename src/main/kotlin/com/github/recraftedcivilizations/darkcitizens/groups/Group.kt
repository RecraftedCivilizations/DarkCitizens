package com.github.recraftedcivilizations.darkcitizens.groups

/**
 * @author DarkVanityOfLight
 */

/**
 * A group groups one or more jobs together,
 * it also defines rules for player in the group
 * @param name The name of the group
 * @param maxLvl The maximum lvl you can reach in this group
 * @param lvlThreshold A list with all xp amounts for the next lvl
 * @param friendlyFire Should the group be able to damage each other
 * @param canBeCriminal Can members of the group be criminals
 */
data class Group(val name: String, val maxLvl: Int, val lvlThreshold: List<Int>, val friendlyFire: Boolean, val canBeCriminal: Boolean, val prefix: String)