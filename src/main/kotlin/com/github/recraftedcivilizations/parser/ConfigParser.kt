package com.github.recraftedcivilizations.parser

import com.github.darkvanityoflight.recraftedcore.configparser.ARecraftedConfigParser
import com.github.recraftedcivilizations.BukkitWrapper
import com.github.recraftedcivilizations.groups.GroupManager
import com.github.recraftedcivilizations.jobs.JobManager
import com.github.recraftedcivilizations.tasks.TaskManager
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration

class ConfigParser(
    config: FileConfiguration,
    private val taskManager: TaskManager,
    private val jobManager: JobManager,
    private val groupManager: GroupManager, private val bukkitWrapper: BukkitWrapper = BukkitWrapper()
) : ARecraftedConfigParser(config) {
    val taskNames = emptySet<String>().toMutableSet()
    val jobNames = emptySet<String>().toMutableSet()
    val groupNames = emptySet<String>().toMutableSet()


    override fun read() {
    }

    private fun parseTasks(tasksSection: ConfigurationSection) {
    }

    private fun parseJobs(jobsSection: ConfigurationSection) {
    }

    private fun parseGroups(groupSection: ConfigurationSection) {
    }

    private fun configSectionToTask(taskName: String, configurationSection: ConfigurationSection) {
    }

    private fun configSectionToJob(jobName: String, configurationSection: ConfigurationSection) {
        TODO("Not yet implemented")
    }

    private fun configSectionToGroup(groupName: String, configurationSection: ConfigurationSection) {
        TODO("Not yet implemented")
    }

    private fun verify(): Boolean {
        TODO("Not yet implemented")
    }


    private fun save() {
    }

    companion object {
        const val jobSectionName = "Jobs"
        const val taskSectionName = "Tasks"
        const val groupSectionName = "Groups"
        const val taskIncomeName = "income"
        const val taskXpName = "xp"
        const val taskActionName = "actions"
        const val taskDescriptionName = "description"
    }
}