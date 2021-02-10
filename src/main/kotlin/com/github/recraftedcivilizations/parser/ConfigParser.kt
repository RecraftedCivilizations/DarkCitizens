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
        // Check that the group section exists
        if (config.isSet(groupSectionName)) {
            bukkitWrapper.severe("Could not find the Groups section, please define it using the $groupSectionName tag, I created it for you, but it does not contain any groups")
            config.createSection(groupSectionName)
            save()
        }
        // Parse the group section
        val groupSection = config.getConfigurationSection(groupSectionName)!!
        parseGroups(groupSection)

        // Check that the Task section exists
        if (config.isSet(jobSectionName)) {
            bukkitWrapper.severe("Could not find the Tasks section, please define it using the $taskSectionName tag, I created it for you, but it does not contain any tasks")
        }
        // Parse the task section
        val taskSection = config.getConfigurationSection(taskSectionName)!!
        parseTasks(taskSection)

        // Check that the Jobs section exists
        if (config.isSet(jobSectionName)) {
            bukkitWrapper.severe("Could not find the Jobs section, please define it using the $jobSectionName tag, I created it for you, but it does not contain any jobs")
        }
        // Parse the job section
        val jobSection = config.getConfigurationSection(jobSectionName)!!
        parseJobs(jobSection)

        verify()
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
        config.save(config.currentPath)
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