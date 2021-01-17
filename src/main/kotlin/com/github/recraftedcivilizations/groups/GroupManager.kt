package com.github.recraftedcivilizations.groups


class GroupManager {
    companion object { val defaultSteps = 50 }
    val groups: MutableSet<Group> = emptySet<Group>().toMutableSet()

    fun getGroup(name: String): Group? {
        for(group in groups){
            if (group.name == name){
                return group
            }
        }
        return null
    }

    fun createGroup(name: String, maxLvl: Int, lvlThresholds: List<Int>, friendlyFire: Boolean, canBeCriminal: Boolean){
        val newGroup = Group(name, maxLvl, lvlThresholds, friendlyFire, canBeCriminal)

        if(maxLvl > lvlThresholds.size){
            val newLvlsToCreate = maxLvl - lvlThresholds.size
            for (lvl in 0..newLvlsToCreate){
                lvlThresholds.plus(defaultSteps)
            }

        }

        groups.plus(newGroup)
    }

}