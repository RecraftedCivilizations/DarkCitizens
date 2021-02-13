package com.github.recraftedcivilizations.parser

import com.github.recraftedcivilizations.BukkitWrapper
import com.github.recraftedcivilizations.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.groups.Group
import com.github.recraftedcivilizations.groups.GroupManager
import com.github.recraftedcivilizations.jobs.IJob
import com.github.recraftedcivilizations.jobs.JobManager
import com.github.recraftedcivilizations.jobs.randomString
import com.github.recraftedcivilizations.parser.dataparser.IParseData
import com.github.recraftedcivilizations.tasks.ITask
import com.github.recraftedcivilizations.tasks.TaskManager
import com.nhaarman.mockitokotlin2.*
import net.milkbowl.vault.economy.Economy
import org.bukkit.configuration.file.YamlConfiguration
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.random.Random

// The ConfigParser has way to much possibilities then I could effort to test
// So I will just test the cases that result in a coverage ofm 100% even if not everything is tested

const val filePath = "config.yml"
const val dataDir = "."

internal class ConfigParserTest {
    val dataParser = mock<IParseData>{}
    val economy = mock<Economy>{}
    val dPlayerManager = DPlayerManager(dataParser)
    val jobManager = JobManager(dPlayerManager)
    val taskManager = TaskManager(economy, dPlayerManager)
    val groupManager = GroupManager()
    val bukkitWrapper = mock<BukkitWrapper>{}
    lateinit var fileConfig: YamlConfiguration

    private val jobsField = JobManager::class.java.getDeclaredField("jobs")
    private val groupsField = GroupManager::class.java.getDeclaredField("groups")
    private val tasksField = TaskManager::class.java.getDeclaredField("tasks")

    init {
        jobManager.setTaskManager(taskManager)
        taskManager.setJobManager(jobManager)

        doNothing().whenever(bukkitWrapper).info(any())
        doNothing().whenever(bukkitWrapper).warning(any())
        doNothing().whenever(bukkitWrapper).severe(any())

        // Make the manager list fields accessible
        jobsField.isAccessible = true
        groupsField.isAccessible = true
        tasksField.isAccessible = true
    }

    @BeforeEach
    fun setUp(){
        fileConfig = YamlConfiguration()
    }

    @AfterEach
    fun tearDown(){

        val clean = { it:MutableSet<*> -> it.removeAll(it) }

        // Clean out the job manager
        clean((jobsField.get(jobManager) as MutableSet<*>))
        clean(groupsField.get(groupManager) as MutableSet<*>)
        clean(tasksField.get(taskManager) as MutableSet<*>)

        // Clean out possible created files
        File(filePath).delete()

    }

    @Test
    fun readJobsGroupsAndTaskAllValid() {
        val jobName1 = randomString()
        val groupName1 = randomString()
        val taskName1 = randomString()

        // Create the Jobs section
        val jobsSection = fileConfig.createSection(ConfigParser.jobSectionName)
        val tasksSection = fileConfig.createSection(ConfigParser.taskSectionName)
        val groupsSection = fileConfig.createSection(ConfigParser.groupSectionName)

        // Create new jobs
        val jobArgs = createRandomJob(listOf(taskName1), groupName1)
        var jobSection = jobsSection.createSection(jobName1, jobArgs)

        // Create new Tasks
        val taskArgs = createRandomTask()
        var taskSection = tasksSection.createSection(taskName1, taskArgs)

        // Create new Groups
        val groupArgs = createRandomGroup()
        var groupSection = groupsSection.createSection(groupName1, groupArgs)

        // Start the testing
        val configParser = ConfigParser(fileConfig, dataDir, taskManager, jobManager, groupManager, bukkitWrapper)
        configParser.read()

        // Check that we got no errors
        verify(bukkitWrapper, times(1)).info("Your config is valid, good job, now get a cookie and some hot choc and enjoy your server.")
        verify(bukkitWrapper, times(0)).warning(any())
        verify(bukkitWrapper, times(0)).severe(any())

        // Check that the job got parsed right
        val job = jobManager.getJob(jobName1)
        assertJob(job, jobArgs)

        // Check that the group is parsed right
        val group = groupManager.getGroup(groupName1)
        assertGroup(group, groupArgs)

        // Check that the task is parsed right
        val task = taskManager.getTask(taskName1)
        assertTask(task, taskArgs)
    }

    @Test
    fun readWithMissingSections(){

        val configParser = ConfigParser(fileConfig, dataDir, taskManager, jobManager, groupManager, bukkitWrapper)
        configParser.read()
        verify(bukkitWrapper, times(1)).severe("Could not find the Groups section, please define it using the ${ConfigParser.groupSectionName} tag, I created it for you, but it does not contain any groups")
        verify(bukkitWrapper, times(1)).severe("Could not find the Tasks section, please define it using the ${ConfigParser.taskSectionName} tag, I created it for you, but it does not contain any tasks")
        verify(bukkitWrapper, times(1)).severe("Could not find the Jobs section, please define it using the ${ConfigParser.jobSectionName} tag, I created it for you, but it does not contain any jobs")
        verify(bukkitWrapper, times(1)).info("Your config is valid, good job, now get a cookie and some hot choc and enjoy your server.")
    }

    @Test
    fun shouldFixTask(){
        val taskName = randomString()

        val jobsSection = fileConfig.createSection(ConfigParser.jobSectionName)
        val tasksSection = fileConfig.createSection(ConfigParser.taskSectionName)
        val groupsSection = fileConfig.createSection(ConfigParser.groupSectionName)

        tasksSection.createSection(taskName)

        val configParser = ConfigParser(fileConfig, dataDir, taskManager, jobManager, groupManager, bukkitWrapper)
        configParser.read()
        verify(bukkitWrapper, times(1)).warning("The task $taskName has no income defined, I ll default it to 100, but you should define it using the ${ConfigParser.taskIncomeName} tag!")
        verify(bukkitWrapper, times(1)).warning("The task $taskName has no xp defined, I ll default it to 100, but you should define it using the ${ConfigParser.taskXpName} tag!")
        verify(bukkitWrapper, times(1)).info("The task $taskName has no defined actions, define them using the ${ConfigParser.taskActionName} tag.")
        verify(bukkitWrapper, times(1)).warning("The task $taskName has no description defined, define it using the ${ConfigParser.taskDescriptionName}!")
        val task = taskManager.getTask(taskName)
        assertTask(task, mapOf(Pair(ConfigParser.taskIncomeName, 100), Pair(ConfigParser.taskXpName, 100), Pair(ConfigParser.taskDescriptionName, "")))
    }

    private fun assertGroup(group: Group?, groupArgs: Map<Any, Any>){
        group!!
        assertEquals(groupArgs[ConfigParser.groupMaxLvlName], group.maxLvl)
        assertEquals(groupArgs[ConfigParser.groupCanBeCriminalName], group.canBeCriminal)
        assertEquals(groupArgs[ConfigParser.groupFriendlyFireName], group.friendlyFire)
        assertEquals(groupArgs[ConfigParser.groupLvlThresholdsName], group.lvlThreshold)

    }

    private fun assertJob(job: IJob?, jobArgs: Map<Any, Any>){
        job!!
        assertEquals(jobArgs[ConfigParser.jobGroupName], job.group)
        assertEquals(jobArgs[ConfigParser.jobPlayerLimitName], job.playerLimit)
        assertEquals((jobArgs[ConfigParser.jobCanDemoteName] as List<*>).toSet(), job.canDemote)
        assertEquals(jobArgs[ConfigParser.jobMinLvlName], job.minLvl)
        assertEquals(jobArgs[ConfigParser.jobElectionRequiredName], job.electionRequired)
        assertEquals(jobArgs[ConfigParser.jobPermissionRequiredName], job.permissionRequired)
    }

    private fun assertTask(task: ITask?, taskArgs: Map<Any, Any>){
        task!!
        assertEquals(taskArgs[ConfigParser.taskIncomeName], task.income)
        assertEquals(taskArgs[ConfigParser.taskXpName], task.xp)
        assertEquals(taskArgs[ConfigParser.taskDescriptionName], task.description)
    }

    private fun createRandomJob(tasks: List<String>, group: String): Map<Any, Any> {
        return mapOf(
            Pair(ConfigParser.jobGroupName, group),
            Pair(ConfigParser.jobPlayerLimitName, Random.nextInt()),
            Pair(ConfigParser.jobTasksName, tasks),
            Pair<String, List<String>>(ConfigParser.jobCanDemoteName, emptyList()),
            Pair(ConfigParser.jobMinLvlName, Random.nextInt()),
            Pair(ConfigParser.jobElectionRequiredName, Random.nextBoolean()),
            Pair(ConfigParser.jobPermissionRequiredName, Random.nextBoolean()),
            Pair(ConfigParser.jobBaseIncomeName, Random.nextInt()),
            Pair(ConfigParser.jobBaseXpName, Random.nextInt())
        )
    }

    private fun createRandomTask(valid: Boolean = true): Map<Any, Any>{
        return mapOf(
            Pair(ConfigParser.taskIncomeName, Random.nextInt()),
            Pair(ConfigParser.taskXpName, Random.nextInt()),
            Pair(ConfigParser.taskActionName, if(valid){ listOf("DEBUG") }else{ listOf(randomString())}),
            Pair(ConfigParser.taskDescriptionName, randomString())
        )
    }

    private fun createRandomGroup(): Map<Any, Any>{
        val maxLvl = Random.nextDouble(100.toDouble()).toInt()
        return mapOf(
            Pair(ConfigParser.groupMaxLvlName, maxLvl),
            Pair(ConfigParser.groupCanBeCriminalName, Random.nextBoolean()),
            Pair(ConfigParser.groupFriendlyFireName, Random.nextBoolean()),
            Pair(ConfigParser.groupLvlThresholdsName, (1..maxLvl+1).toList()),
        )
    }
}