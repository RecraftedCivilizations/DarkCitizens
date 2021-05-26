package com.github.recraftedcivilizations.darkcitizens.parser

import com.github.darkvanityoflight.recraftedcore.configparser.ARecraftedConfigParser
import com.github.recraftedcivilizations.darkcitizens.DarkCitizens
import com.github.recraftedcivilizations.darkcitizens.actions.IAction
import com.github.recraftedcivilizations.darkcitizens.actions.actions.ActionFactory
import com.github.recraftedcivilizations.darkcitizens.actions.actions.ActionType
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.groups.GroupManager
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.tasks.TaskManager
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import java.io.File

/**
 * @author DarkVanityOfLight
 */

fun ConfigurationSection.getMaterial(key: String, default: Material? = null): Material? {
    val name = this.getString(key, null) ?: return default
    return Material.getMaterial(name)
}
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
    private val groupManager: GroupManager,
    private val dPlayerManager: DPlayerManager,
    private val bukkitWrapper: com.github.recraftedcivilizations.darkcitizens.BukkitWrapper = com.github.recraftedcivilizations.darkcitizens.BukkitWrapper()
) : ARecraftedConfigParser(config) {
    val taskNames = emptySet<String>().toMutableSet()
    val jobNames = emptySet<String>().toMutableSet()
    val groupNames = emptySet<String>().toMutableSet()
    var baseIncomeTime: Int = 0


    /**
     * Call this method to read and parse the config
     */
    override fun read() {

        // Check that the action section exists
        if (!config.isSet(actionSectionName)) {
            bukkitWrapper.severe("Could not find the Actions section, please define it using the $actionSectionName tag, I created it for you, but it does not contain any groups")
            config.createSection(actionSectionName)
            save()
        }
        // Parse actions
        val actionSection = config.getConfigurationSection(actionSectionName)!!
        parseActions(actionSection)

        // Check that the group section exists
        if (!config.isSet(groupSectionName)) {
            bukkitWrapper.severe("Could not find the Groups section, please define it using the $groupSectionName tag, I created it for you, but it does not contain any groups")
            config.createSection(groupSectionName)
            save()
        }
        // Parse the group section
        val groupSection = config.getConfigurationSection(groupSectionName)!!
        parseGroups(groupSection)

        // Check that the Task section exists
        if (!config.isSet(jobSectionName)) {
            bukkitWrapper.severe("Could not find the Tasks section, please define it using the $taskSectionName tag, I created it for you, but it does not contain any tasks")
            config.createSection(taskSectionName)
            save()
        }
        // Parse the task section
        val taskSection = config.getConfigurationSection(taskSectionName)!!
        parseTasks(taskSection)

        // Check that the Jobs section exists
        if (!config.isSet(jobSectionName)) {
            bukkitWrapper.severe("Could not find the Jobs section, please define it using the $jobSectionName tag, I created it for you, but it does not contain any jobs")
            config.createSection(jobSectionName)
            save()
        }
        // Parse the job section
        val jobSection = config.getConfigurationSection(jobSectionName)!!
        parseJobs(jobSection)

        baseIncomeTime = config.getInt(baseIncomeTimeName)
        if (baseIncomeTime <= 0){
            bukkitWrapper.warning("Could not find the baseIncomeTime it will be defaulted to 5 minutes, please define it using the $baseIncomeTimeName tag")
            baseIncomeTime = 5
        }

        if(verify()){
            bukkitWrapper.info("Your config is valid, good job, now get a cookie and some hot choc and enjoy your server.")
        }else{
            bukkitWrapper.info("Your config is invalid at some point, it may work anyway, but do you really want to live with the knowledge that something may go wrong at any point?")
        }
    }

    /**
     * Parse all actions in the action section
     * @param actionsSection The action section to parse
     */
    private fun parseActions(actionsSection: ConfigurationSection){
        for (actionName in actionsSection.getKeys(false)){
            val actionSection = actionsSection.getConfigurationSection(actionName)!!
            configSectionToAction(actionName, actionSection)
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

        val iconName = configurationSection.getString(jobIconName)
        if (iconName == null){
            bukkitWrapper.warning("The job $taskName has no icon defined, I'll default it to a player head, but you should define it using the $taskIconName tag!")
        }else{
            if (Material.getMaterial(iconName) == null){
                bukkitWrapper.warning("The icon for the job $taskName does not exist!")
            }
        }
        val icon = iconName?.let { Material.getMaterial(it) } ?: Material.PLAYER_HEAD


        taskNames.add(taskName)
        taskManager.createTask(taskName, income, xp, actions, description, icon)
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

        val iconName = configurationSection.getString(jobIconName)
        if (iconName == null){
            bukkitWrapper.warning("The job $jobName has no icon defined, I'll default it to a player head, but you should define it using the $jobIconName tag!")
        }else{
            if (Material.getMaterial(iconName) == null){
                bukkitWrapper.warning("The icon for the job $jobName does not exist!")
            }
        }
        val icon = iconName?.let { Material.getMaterial(it) } ?: Material.PLAYER_HEAD

        var candidateTime = configurationSection.getInt(jobCandidateTimeName, -1)
        if (candidateTime == -1 && electionRequired){
            bukkitWrapper.warning("The job $jobName has no candidate time defined, I'll default it to 1 minute, but you should define it using the $jobCandidateTimeName")
            candidateTime = 1
        }

        var voteTime = configurationSection.getInt(jobVoteTimeName, -1)
        if (candidateTime == -1 && electionRequired){
            bukkitWrapper.warning("The job $jobName has no vote time defined, I'll default it to 1 minute, but you should define it using the $jobVoteTimeName")
            voteTime = 1
        }

        var candidateFee = configurationSection.getInt(jobCandidateFeeName, -1)
        if (candidateTime == -1 && electionRequired){
            bukkitWrapper.warning("The job $jobName has no candidate time defined, I'll default it to 100, but you should define it using the $jobCandidateFeeName")
            candidateFee = 100
        }

        var voteFee = configurationSection.getInt(jobVoteFeeName, -1)
        if (candidateTime == -1 && electionRequired){
            bukkitWrapper.warning("The job $jobName has no candidate time defined, I'll default it to 100, but you should define it using the $jobVoteFeeName")
            voteFee = 0
        }

        val leaveOnDeath = configurationSection.getBoolean(jobLeaveOnDeathName, false)
        val isMajor = configurationSection.getBoolean(jobIsMajorName, false)

        val prefix = configurationSection.getString(jobPrefixName, "")?: ""

        jobNames.add(jobName)
        jobManager.createJob(jobName, group, playerLimit, tasks.toSet(), canDemote.toSet(), baseIncome, baseXp, minLvl, electionRequired, permissionRequired, icon, leaveOnDeath, prefix, candidateTime, voteTime, candidateFee, voteFee, isMajor)
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
        val prefix = configurationSection.getString(groupPrefixName, "")?: ""

        groupNames.add(groupName)
        groupManager.createGroup(groupName, maxLvl, lvlThresholds, friendlyFire, canBeCriminal, prefix)
    }

    /**
     * Parse a config section to an action and register it at the [ActionManager]
     * @param actionName The name of this action
     * @param actionSection The section to parse as Action
     */
    private fun configSectionToAction(actionName: String, actionSection: ConfigurationSection){
        val type = ActionType.valueOf(actionSection.getString(actionTypeName)?: return)

        val name = actionSection.getString(actionNameName)?: ""
        val description = actionSection.getString(actionDescriptionName)?: ""
        val number = actionSection.getInt(actionNumberName)
        val itemType = actionSection.getMaterial(actionItemTypeName)
        val block = actionSection.getMaterial(actionBlockTypeName)

        // Create and register the new action
        ActionFactory.createNewAction(type, name, description, number, itemType, block, dPlayerManager)

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
                bukkitWrapper.warning("The job $jobName has ${job.group} defined as its group, but the group could not be found, please define the group!")
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
        const val jobIconName = "icon"
        const val jobCandidateTimeName = "candidateTime"
        const val jobVoteTimeName = "voteTime"
        const val jobCandidateFeeName = "candidateFee"
        const val jobVoteFeeName = "voteFee"
        const val jobLeaveOnDeathName = "leaveOnDeath"
        const val jobIsMajorName = "isMajor"
        const val jobPrefixName = "prefix"
        const val groupMaxLvlName = "maxLvl"
        const val groupLvlThresholdsName = "lvlThresholds"
        const val groupFriendlyFireName = "friendlyFire"
        const val groupCanBeCriminalName = "canBeCriminal"
        const val groupPrefixName ="prefix"
        const val baseIncomeTimeName = "baseIncomeTime"
        const val taskIconName = "icon"
        const val actionSectionName = "Actions"
        const val actionTypeName = "type"
        const val actionNameName = "name"
        const val actionDescriptionName = "description"
        const val actionBlockTypeName = "blockType"
        const val actionItemTypeName = "itemType"
        const val actionNumberName = "number"

    }
}