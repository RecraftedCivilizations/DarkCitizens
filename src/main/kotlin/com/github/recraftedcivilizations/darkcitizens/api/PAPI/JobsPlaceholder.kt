package com.github.recraftedcivilizations.darkcitizens.api.PAPI

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.laws.LawManager
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class JobsPlaceholder(private val jobManager: JobManager, private val dPlayerManager: DPlayerManager, private val authors: String, private val version: String) : PlaceholderExpansion() {


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

    override fun onPlaceholderRequest(player: Player?, identifier: String): String? {
        val dPlayer = player?.let { dPlayerManager.getDPlayer(it) }
        val job = jobManager.getJob(dPlayer?.job)

        return if (identifier == "job_prefix"){
            ChatColor.translateAlternateColorCodes('&', job?.prefix?: "")
        }else{
            null
        }
    }
}