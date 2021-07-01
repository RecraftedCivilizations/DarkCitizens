package com.github.recraftedcivilizations.groups

import com.github.recraftedcivilizations.darkcitizens.groups.Group
import com.github.recraftedcivilizations.darkcitizens.groups.GroupManager
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class GroupManagerTest {
    val group =  Group("Foo", 10, (1..10).toList(), false, canBeCriminal = false, "Foo")

    @Test
    fun getGroup() {
        val groupManager = GroupManager()
        groupManager.createGroup("Foo", 10, (1..10).toList(), false,false, "Foo")
        assertEquals(group, groupManager.getGroup("Foo"))
    }

    @Test
    fun createGroup() {
        val groupManager = GroupManager()
        val group =  Group("Foo", 10, (50..500 step 50).toList(), false, canBeCriminal = false, "Foo")

        groupManager.createGroup("Foo", 10, (50..500 step 50).toList(),false,false, "Foo")

        assertEquals(group, groupManager.getGroup("Foo"))

        // Check that groups with same name aren't added
        groupManager.createGroup("Foo", 10, (50..500 step 50).toList(),false,false, "Foo")
        // Make private groups field accessible
        val groupsField = GroupManager::class.java.getDeclaredField("groups")
        groupsField.isAccessible = true
        val groups = groupsField.get(groupManager) as Set<*>
        // Check the size of the group set, should be 1 since we only added one group + one duplicate
        assertEquals(1, groups.size)

        // Check that the old group didn't get overridden (they differ in maxLvl)
        assertEquals(group, groupManager.getGroup("Foo"))
    }

    @Test
    fun shouldExpandLvlThresholds(){
        val groupManager = GroupManager()
        var shouldLookLike = Group("Foo", 15, (1..10).toList().plus((60..260 step 50).toList()) , false, canBeCriminal = false, "Foo")

        groupManager.createGroup("Foo", 15, (1..10).toList(), false, canBeCriminal = false, "Foo")

        assertEquals(shouldLookLike, groupManager.getGroup("Foo"))


        shouldLookLike = Group("Bar", 10, (50..500).toList(), false, canBeCriminal = false, "Foo")
        groupManager.createGroup("Bar", 10, (50..500).toList(), false, canBeCriminal = false, "Foo")

        assertEquals(shouldLookLike, groupManager.getGroup("Bar"))
    }
}