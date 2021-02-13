package com.github.recraftedcivilizations.parser

import com.github.darkvanityoflight.recraftedcore.configparser.ARecraftedConfigParser
import com.github.recraftedcivilizations.BukkitWrapper
import com.github.recraftedcivilizations.groups.GroupManager
import com.github.recraftedcivilizations.jobs.JobManager
import com.github.recraftedcivilizations.tasks.TaskManager
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import java.io.File

/**
 * @author DarkVanityOfLight
 */

/**
 * Parse, create and store all values form the config file.
 * Store all task, group and job names in [taskNames], [jobNames] and [groupNames]
 * @constructor Construct using a [config], a [taskManager], a [jobManager], a [groupManager] and an optional [bukkitWrapper] for debugging purposes
 */
class ConfigParser(
    config: FileConfiguration,
    private val dataDir: String,
    private val taskManager: TaskManager,
    private val jobManager: JobManager,
    private val groupManager: GroupManager, private val bukkitWrapper: BukkitWrapper = BukkitWrapper()
) : ARecraftedConfigParser(config) {
    val taskNames = emptySet<String>().toMutableSet()
    val jobNames = emptySet<String>().toMutableSet()
    val groupNames = emptySet<String>().toMutableSet()


    /**
     * Call this method to read and parse the config
     */
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
            config.createSection(taskSectionName)
            save()
        }
        // Parse the task section
        val taskSection = config.getConfigurationSection(taskSectionName)!!
        parseTasks(taskSection)

        // Check that the Jobs section exists
        if (config.isSet(jobSectionName)) {
            bukkitWrapper.severe("Could not find the Jobs section, please define it using the $jobSectionName tag, I created it for you, but it does not contain any jobs")
            config.createSection(jobSectionName)
            save()
        }
        // Parse the job section
        val jobSection = config.getConfigurationSection(jobSectionName)!!
        parseJobs(jobSection)

        if(verify()){
            bukkitWrapper.info("Your config is valid, good job, now get a cookie and some hot choc and enjoy your server.")
        }else{
            bukkitWrapper.info("Your config is invalid at some point, it may work anyway, but do you really want to live with the knowledge that something may go wrong at any point?")
        }
    }

    /**
     * Parse all tasks in the task section
     * @param tasksSection The task section to parse
     */
    private fun parseTasks(tasksSection: ConfigurationSection) {
        for (taskName in tasksSection.getKeys(false)) {
            val taskSection = tasksSection.getConfigurationSection(taskName)!!
            configSectionToTask(taskName, taskSection)
        }
    }

    /**
     * Parse all jobs in the jobs section
     * @param jobsSection The jobs section to parse
     */
    private fun parseJobs(jobsSection: ConfigurationSection) {
        for (jobName in jobsSection.getKeys(false)) {
            val jobSection = jobsSection.getConfigurationSection(jobName)!!
            configSectionToJob(jobName, jobSection)
        }
    }

    /**
     * Parse all groups in the groups section
     * @param groupSection The group section to parse
     */
    private fun parseGroups(groupSection: ConfigurationSection) {
        for (groupName in groupSection.getKeys(false)) {
            val jobSection = groupSection.getConfigurationSection(groupName)!!
            configSectionToGroup(groupName, jobSection)
        }
    }

    /**
     * Parse a config section to a task and add it to the [taskManager], this is called for every task in the task section
     * @param taskName The name of the task
     * @param configurationSection The config section to parse
     */
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

    /**
     * Parse a config section to a job and add it to the [jobManager], this is called for every job in the jobs section
     * @param jobName The name of the task
     * @param configurationSection The config section to parse
     */
    private fun configSectionToJob(jobName: String, configurationSection: ConfigurationSection) {

        var group = configurationSection.getString(jobGroupName)
        if (group == null){
            bukkitWrapper.warning("The job $jobName has no group defined, this may lead to severe errors later on, please define the group using the $jobGroupName tag!")
            group = ""
        }

        var playerLimit = configurationSection.getInt(jobPlayerLimitName, -1)
        if (playerLimit == -1){
            bukkitWrapper.warning("The job $jobName has no player limit defined, I'll default it to 10 but you should define it using the $jobPlayerLimitName")
            playerLimit = 10
        }

        val tasks = configurationSection.getStringList(jobTasksName)
        if (tasks.isEmpty()){
            bukkitWrapper.info("The job $jobName has no tasks defined, you can define tasks for the Job using the tasks name in the $jobTasksName section")
        }

        val canDemote = configurationSection.getStringList(jobCanDemoteName)

        var baseIncome = configurationSection.getInt(jobBaseIncomeName, -1)
        if (baseIncome == -1){
            bukkitWrapper.warning("The job $jobName has no base income defined, I'll default it to 10, but please define it using the $jobBaseIncomeName tag!")
            baseIncome = 10
        }
        var baseXp = configurationSection.getInt(jobBaseXpName, -1)
        if (baseXp == -1){
            bukkitWrapper.warning("The job $jobName has no base Xp gain defined, I'll default it to 10, but you should define it using the $jobBaseXpName tag!")
            baseXp = 10
        }
        val minLvl = configurationSection.getInt(jobMinLvlName, 0)
        val electionRequired = configurationSection.getBoolean(jobElectionRequiredName, false)
        val permissionRequired = configurationSection.getBoolean(jobPermissionRequiredName, false)

        jobNames.add(jobName)
        jobManager.createJob(jobName, group, playerLimit, tasks.toSet(), canDemote.toSet(), baseIncome, baseXp, minLvl, electionRequired, permissionRequired)
    }

    /**
     * Parse a config section to a group and add it to the [groupManager], this is called for every group in the groups section
     * @param groupName The name of the task
     * @param configurationSection The config section to parse
     */
    private fun configSectionToGroup(groupName: String, configurationSection: ConfigurationSection) {

        var maxLvl = configurationSection.getInt(groupMaxLvlName, -1)
        if (maxLvl == -1){
            bukkitWrapper.warning("The group $groupName has no maximum lvl defined, I'll default it to 50, but you should define it using the $groupMaxLvlName tag!")
            maxLvl = 50
        }
        val lvlThresholds = configurationSection.getIntegerList(groupLvlThresholdsName)
        if (lvlThresholds.size < maxLvl || lvlThresholds.isEmpty()){
            bukkitWrapper.warning("The group $groupName has no or not enough level thresholds defined, I'll fill them in for you, but you should define them using the $groupLvlThresholdsName tag!")
        }

        val friendlyFire = configurationSection.getBoolean(groupFriendlyFireName, false)
        val canBeCriminal = configurationSection.getBoolean(groupCanBeCriminalName, false)

        groupNames.add(groupName)
        groupManager.createGroup(groupName, maxLvl, lvlThresholds, friendlyFire, canBeCriminal)
    }

    /**
     * Verify that the config is parsed correctly
     */
    private fun verify(): Boolean {

        var valid = true

        for (jobName in jobNames){
            val job = jobManager.getJob(jobName)
            if (job == null){
                bukkitWrapper.warning("The job $jobName was parsed but not created, please check your config again!")
                valid = false
                continue
            }

            val group = groupManager.getGroup(job.group)
            if (group == null){
                bukkitWrapper.warning("The job $jobName has $group defined as its group, but the group could not be found, please define the group!")
                valid = false
                continue
            }
        }

        for(taskName in taskNames){
            val task = taskManager.getTask(taskName)
            if (task == null){
                bukkitWrapper.warning("The task $taskName was parsed but not created, please check your config again!")
                valid = false
                continue
            }
        }

        for (groupName in groupNames){
            val group = groupManager.getGroup(groupName)
            if (group == null){
                bukkitWrapper.warning("The group $groupName was parsed but nit created, please check your config again!")
            }
        }

        return valid
    }

    /**
     * Save the config file
     */
    private fun save() {
        config.save(File(dataDir, "config.yml"))
    }

    companion object {
        const val jobSectionName = "Jobs"
        const val taskSectionName = "Tasks"
        const val groupSectionName = "Groups"
        const val taskIncomeName = "income"
        const val taskXpName = "xp"
        const val taskActionName = "actions"
        const val taskDescriptionName = "description"
        const val jobGroupName = "group"
        const val jobPlayerLimitName = "playerLimit"
        const val jobTasksName = "tasks"
        const val jobCanDemoteName = "canDemote"
        const val jobBaseIncomeName = "baseIncome"
        const val jobBaseXpName = "baseXp"
        const val jobMinLvlName = "minLvl"
        const val jobElectionRequiredName = "electionRequired"
        const val jobPermissionRequiredName = "permissionRequired"
        const val groupMaxLvlName = "maxLvl"
        const val groupLvlThresholdsName = "lvlThresholds"
        const val groupFriendlyFireName = "friendlyFire"
        const val groupCanBeCriminalName = "canBeCriminal"
    }
}