package com.github.recraftedcivilizations.darkcitizens.groups

/**
 * @author DarkVanityOfLight
 */

/**
 * Holds all groups
 */
class GroupManager {
    companion object { const val defaultSteps = 50 }
    private val groups: MutableSet<Group> = emptySet<Group>().toMutableSet()

    /**
     * Get a group from its name
     * @param name The name of the group
     */
    fun getGroup(name: String?): Group? {
        for(group in groups){
            if (group.name == name){
                return group
            }
        }
        return null
    }

    /**
     * Create a new group
     * TODO("This belongs into a factory")
     * @param name The name of the new group
     * @param maxLvl The maximum lvl for this group
     * @param lvlThresholds A list with the XP amounts for the next lvl
     * @param friendlyFire Can members of the group damage each other
     * @param canBeCriminal Can members of the group be criminals
     */
    fun createGroup(name: String, maxLvl: Int, lvlThresholds: List<Int>, friendlyFire: Boolean, canBeCriminal: Boolean){

        val fixedThresholds = lvlThresholds.toMutableList()
        if(maxLvl > lvlThresholds.size){
            val newLvlsToCreate = maxLvl - lvlThresholds.size
            for (lvl in 0 until newLvlsToCreate){
                var last = 0
                try {
                    last = fixedThresholds.last()
                }catch (e: NoSuchElementException){ }
                fixedThresholds.add(defaultSteps + last)
            }

        }

        val newGroup = Group(name, maxLvl, fixedThresholds, friendlyFire, canBeCriminal)

        if (getGroup(newGroup.name) == null){
            groups.add(newGroup)
        }
    }

}