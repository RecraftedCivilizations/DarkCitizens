package com.github.recraftedcivilizations.darkcitizens.api.PAPI

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.groups.GroupManager
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

class GroupsPlaceholder(private val groupManager: GroupManager, private val dPlayerManager: DPlayerManager, private val jobManager: JobManager, private val authors: String, private val version: String) : PlaceholderExpansion() {
    override fun persist(): Boolean {
        return true
    }

    override fun canRegister(): Boolean {
        return true
    }

    override fun getAuthor(): String {
        return authors
    }

    override fun getIdentifier(): String {
        return "darkcitizens"
    }

    override fun getVersion(): String {
        return version
    }

    override fun onPlaceholderRequest(player: Player?, params: String): String {
        val dPlayer = player?.let { dPlayerManager.getDPlayer(it) }
        val groupName = jobManager.getJob(dPlayer?.job)?.group
        val group = groupManager.getGroup(groupName)

        return group?.prefix?: ""
    }
}