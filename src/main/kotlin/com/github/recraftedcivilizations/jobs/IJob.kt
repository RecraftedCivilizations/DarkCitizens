package com.github.recraftedcivilizations.jobs

import com.github.recraftedcivilizations.dPlayer.DPlayer
import com.github.recraftedcivilizations.tasks.ITask
import org.bukkit.entity.Player

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


    fun removePlayer(player: DPlayer)
    fun removePlayer(player: Player)
    fun addPlayer(player: DPlayer)
    fun addPlayer(player: Player)
    fun canJoin(player: DPlayer): Boolean
    fun canJoin(player: Player): Boolean

}