package com.github.recraftedcivilizations.groups

data class Group(val name: String, val maxLvl: Int, val lvlThreshold: List<Int>, val friendlyFire: Boolean, val canBeCriminal: Boolean)