package com.github.recraftedcivilizations.groups

import com.github.recraftedcivilizations.darkcitizens.groups.Group
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GroupTest{

    @Test
    fun mockTest(){
        //There is nothing to test so this is a mock for coverage
        val group = Group("Foo", 10, (1..10).toList(), false, canBeCriminal = false)
        assertEquals(Group("Foo", 10, (1..10).toList(), false, canBeCriminal = false), group)
    }
}