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
        for (taskName in tasksSection.getKeys(false)) {
            val taskSection = tasksSection.getConfigurationSection(taskName)!!
            configSectionToTask(taskName, taskSection)
        }
    }

    private fun parseJobs(jobsSection: ConfigurationSection) {
        for (jobName in jobsSection.getKeys(false)) {
            val jobSection = jobsSection.getConfigurationSection(jobName)!!
            configSectionToJob(jobName, jobSection)
        }
    }

    private fun parseGroups(groupSection: ConfigurationSection) {
        for (groupName in groupSection.getKeys(false)) {
            val jobSection = groupSection.getConfigurationSection(groupName)!!
            configSectionToJob(groupName, jobSection)
        }
    }

    private fun configSectionToTask(taskName: String, configurationSection: ConfigurationSection) {

        var income = configurationSection.getInt(taskIncomeName, -1)
        if (income == -1) {
            bukkitWrapper.warning("The task $taskName has no income defined, I ll default it to 100, but you should define it using the $taskIncomeName tag!")
            income = 100
        }

        var xp = configurationSection.getInt(taskXpName, -1)
        if (xp == -1) {
            bukkitWrapper.warning("The task $taskName has no xp defined, I ll default it to 100, but you should define it using the $taskXpName tag!")
            xp = 100
        }

        val actions = configurationSection.getStringList(taskActionName)
        if (actions.isEmpty()) {
            bukkitWrapper.info("The task $taskName has no defined actions, define them using the $taskActionName tag.")
        }
        var description = configurationSection.getString(taskDescriptionName)
        if (description == null) {
            bukkitWrapper.warning("The task $taskName has no description defined, define it using the $taskDescriptionName!")
            description = ""
        }

        taskNames.add(taskName)
        taskManager.createTask(taskName, income, xp, actions, description)
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