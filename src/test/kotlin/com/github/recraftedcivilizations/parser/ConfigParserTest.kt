package com.github.recraftedcivilizations.parser

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.groups.Group
import com.github.recraftedcivilizations.darkcitizens.groups.GroupManager
import com.github.recraftedcivilizations.darkcitizens.jobs.ElectedJob
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import com.github.recraftedcivilizations.darkcitizens.jobs.Job
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.parser.ConfigParser
import com.github.recraftedcivilizations.darkcitizens.parser.dataparser.IParseData
import com.github.recraftedcivilizations.darkcitizens.tasks.ITask
import com.github.recraftedcivilizations.darkcitizens.tasks.TaskManager
import com.github.recraftedcivilizations.jobs.randomString
import com.nhaarman.mockitokotlin2.*
import net.milkbowl.vault.economy.Economy
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.random.Random

// The ConfigParser has way to much possibilities then I could effort to test
// So I will just test the cases that result in a coverage ofm 100% even if not everything is tested

inline fun <reified T> T.callPrivateFunc(name: String, vararg args: Any?): Any? {
    val classArray: Array<Class<*>> = args.map { it!!::class.java}.toTypedArray()
    return T::class.java.getDeclaredMethod(name, *classArray)
        .apply { isAccessible = true }
        .invoke(this, *args)
}

const val filePath = "config.yml"
const val dataDir = "."

internal class ConfigParserTest {
    val dataParser = mock<IParseData>{}
    val economy = mock<Economy>{}
    val dPlayerManager = DPlayerManager(dataParser)
    val jobManager = JobManager(dPlayerManager)
    val groupManager = GroupManager()
    val taskManager = TaskManager(economy, dPlayerManager, groupManager)
    val bukkitWrapper = mock<com.github.recraftedcivilizations.darkcitizens.BukkitWrapper>{}
    lateinit var fileConfig: YamlConfiguration
    val icon = Material.PLAYER_HEAD

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
        val baseIncomeDelay = Random.nextInt(100)

        // Create the Jobs section
        val jobsSection = fileConfig.createSection(ConfigParser.jobSectionName)
        val tasksSection = fileConfig.createSection(ConfigParser.taskSectionName)
        val groupsSection = fileConfig.createSection(ConfigParser.groupSectionName)

        // Create new jobs
        val jobArgs = createRandomJob(setOf(taskName1), groupName1).toMutableMap()
        jobArgs[ConfigParser.jobIconName] = "PLAYER_HEAD"
        var jobSection = jobsSection.createSection(jobName1, jobArgs)

        // Create new Tasks
        val taskArgs = createRandomTask()
        var taskSection = tasksSection.createSection(taskName1, taskArgs)

        // Create new Groups
        val groupArgs = createRandomGroup()
        var groupSection = groupsSection.createSection(groupName1, groupArgs)

        fileConfig.set(ConfigParser.baseIncomeTimeName, baseIncomeDelay)

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

        assertEquals(baseIncomeDelay, configParser.baseIncomeTime)
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
        verify(bukkitWrapper, times(1)).info("Your config is valid, good job, now get a cookie and some hot choc and enjoy your server.")
        val task = taskManager.getTask(taskName)
        assertTask(task, mapOf(Pair(ConfigParser.taskIncomeName, 100), Pair(ConfigParser.taskXpName, 100), Pair(
            ConfigParser.taskDescriptionName, "")))
    }

    @Test
    fun shouldFixJob(){
        val jobName = randomString()
        val group = ""

        val jobsSection = fileConfig.createSection(ConfigParser.jobSectionName)
        val tasksSection = fileConfig.createSection(ConfigParser.taskSectionName)
        val groupsSection = fileConfig.createSection(ConfigParser.groupSectionName)

        jobsSection.createSection(jobName)
        
        val configParser = ConfigParser(fileConfig, dataDir, taskManager, jobManager, groupManager, bukkitWrapper)
        configParser.read()
        verify(bukkitWrapper, times(1)).warning("The job $jobName has no group defined, this may lead to severe errors later on, please define the group using the ${ConfigParser.jobGroupName} tag!")
        verify(bukkitWrapper, times(1)).warning("The job $jobName has no player limit defined, I'll default it to 10 but you should define it using the ${ConfigParser.jobPlayerLimitName}")
        verify(bukkitWrapper, times(1)).info("The job $jobName has no tasks defined, you can define tasks for the Job using the tasks name in the ${ConfigParser.jobTasksName} section")
        verify(bukkitWrapper, times(1)).warning("The job $jobName has no base income defined, I'll default it to 10, but please define it using the ${ConfigParser.jobBaseIncomeName} tag!")
        verify(bukkitWrapper, times(1)).warning("The job $jobName has no base Xp gain defined, I'll default it to 10, but you should define it using the ${ConfigParser.jobBaseXpName} tag!")
        verify(bukkitWrapper, times(1)).warning("The job $jobName has $group defined as its group, but the group could not be found, please define the group!")
        verify(bukkitWrapper, times(1)).info("Your config is invalid at some point, it may work anyway, but do you really want to live with the knowledge that something may go wrong at any point?")
        verify(bukkitWrapper).warning("Could not find the baseIncomeTime it will be defaulted to 5 minutes, please define it using the ${ConfigParser.baseIncomeTimeName} tag")
        verify(bukkitWrapper).warning("The job $jobName has no icon defined, I'll default it to a player head, but you should define it using the ${ConfigParser.jobIconName} tag!")
        verifyNoMoreInteractions(bukkitWrapper)

        val job = jobManager.getJob(jobName)
        val jobArgs = mapOf<Any, Any>(Pair(ConfigParser.jobGroupName, ""), Pair(ConfigParser.jobPlayerLimitName, 10),
            Pair(ConfigParser.jobTasksName, emptySet<String>()), Pair(ConfigParser.jobBaseIncomeName, 10),
            Pair(ConfigParser.jobBaseXpName, 10), Pair(ConfigParser.jobMinLvlName, 0),
            Pair(ConfigParser.jobElectionRequiredName, false), Pair(ConfigParser.jobPermissionRequiredName, false),
            Pair(ConfigParser.jobCanDemoteName, emptySet<String>())
            )
        assertJob(job, jobArgs)
    }

    @Test
    fun shouldFixGroup(){
        val groupName = randomString()

        val jobsSection = fileConfig.createSection(ConfigParser.jobSectionName)
        val tasksSection = fileConfig.createSection(ConfigParser.taskSectionName)
        val groupsSection = fileConfig.createSection(ConfigParser.groupSectionName)

        groupsSection.createSection(groupName)

        val configParser = ConfigParser(fileConfig, dataDir, taskManager, jobManager, groupManager, bukkitWrapper)
        configParser.read()

        verify(bukkitWrapper).warning("The group $groupName has no maximum lvl defined, I'll default it to 50, but you should define it using the ${ConfigParser.groupMaxLvlName} tag!")
        verify(bukkitWrapper).warning("The group $groupName has no or not enough level thresholds defined, I'll fill them in for you, but you should define them using the ${ConfigParser.groupLvlThresholdsName} tag!")
        verify(bukkitWrapper).info("Your config is valid, good job, now get a cookie and some hot choc and enjoy your server.")
        verify(bukkitWrapper).warning("Could not find the baseIncomeTime it will be defaulted to 5 minutes, please define it using the ${ConfigParser.baseIncomeTimeName} tag")
        verifyNoMoreInteractions(bukkitWrapper)


        val fixedThresholds = emptyList<Int>().toMutableList()
        val newLvlsToCreate = 50
        for (lvl in 0 until newLvlsToCreate){
            var last = 0
            try {
                last = fixedThresholds.last()
            }catch (e: NoSuchElementException){ }
            fixedThresholds.add(GroupManager.defaultSteps + last)
        }

        val group = groupManager.getGroup(groupName)
        assertGroup(group, mapOf(
            Pair(ConfigParser.groupMaxLvlName, 50),
            Pair(ConfigParser.groupLvlThresholdsName, fixedThresholds),
            Pair(ConfigParser.groupCanBeCriminalName, false),
            Pair(ConfigParser.groupFriendlyFireName, false)
            )
        )
    }

    @Test
    fun shouldVerify(){
        val configParser = ConfigParser(fileConfig, dataDir, taskManager, jobManager, groupManager, bukkitWrapper)

        val groupName = randomString()
        val jobName1 = randomString()
        val taskName = randomString()
        val jobName2 = randomString(11)
        val groupName2 = randomString(11)

        configParser.groupNames.add(groupName)
        configParser.jobNames.add(jobName1)
        configParser.taskNames.add(taskName)
        configParser.jobNames.add(jobName2)

        val jobArgs = createRandomJob(emptySet(), groupName2)

        jobManager.createJob(jobName2, jobArgs[ConfigParser.jobGroupName] as String, jobArgs[ConfigParser.jobPlayerLimitName] as Int,
            jobArgs[ConfigParser.jobTasksName ]as Set<String>, jobArgs[ConfigParser.jobCanDemoteName] as Set<String>,
            jobArgs[ConfigParser.jobBaseIncomeName] as Int, jobArgs[ConfigParser.jobBaseXpName] as Int,
            jobArgs[ConfigParser.jobMinLvlName] as Int, jobArgs[ConfigParser.jobElectionRequiredName] as Boolean,
            jobArgs[ConfigParser.jobPermissionRequiredName] as Boolean,
            jobArgs[ConfigParser.jobIconName] as Material,
            jobArgs[ConfigParser.jobLeaveOnDeathName] as Boolean,
            )

        val res = configParser.callPrivateFunc("verify") as Boolean

        verify(bukkitWrapper).warning("The job $jobName1 was parsed but not created, please check your config again!")
        verify(bukkitWrapper).warning("The task $taskName was parsed but not created, please check your config again!")
        verify(bukkitWrapper).warning("The group $groupName was parsed but nit created, please check your config again!")
        verify(bukkitWrapper).warning("The job $jobName2 has $groupName2 defined as its group, but the group could not be found, please define the group!")
        verifyNoMoreInteractions(bukkitWrapper)

        assertEquals(false, res)
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
        assertEquals((jobArgs[ConfigParser.jobCanDemoteName] as Set<*>), job.canDemote)
        assertEquals(jobArgs[ConfigParser.jobMinLvlName], job.minLvl)
        assertEquals(jobArgs[ConfigParser.jobPermissionRequiredName], job.permissionRequired)
        if (jobArgs[ConfigParser.jobElectionRequiredName] as Boolean){
            assertEquals(true, job is ElectedJob)
        }else{
            assertEquals(true, job is Job)
        }
    }

    private fun assertTask(task: ITask?, taskArgs: Map<Any, Any>){
        task!!
        assertEquals(taskArgs[ConfigParser.taskIncomeName], task.income)
        assertEquals(taskArgs[ConfigParser.taskXpName], task.xp)
        assertEquals(taskArgs[ConfigParser.taskDescriptionName], task.description)
    }

    private fun createRandomJob(tasks: Set<String>, group: String): Map<Any, Any> {
        return mapOf(
            Pair(ConfigParser.jobGroupName, group),
            Pair(ConfigParser.jobPlayerLimitName, Random.nextInt()),
            Pair(ConfigParser.jobTasksName, tasks),
            Pair(ConfigParser.jobCanDemoteName, emptySet<String>()),
            Pair(ConfigParser.jobMinLvlName, Random.nextInt()),
            Pair(ConfigParser.jobElectionRequiredName, Random.nextBoolean()),
            Pair(ConfigParser.jobPermissionRequiredName, Random.nextBoolean()),
            Pair(ConfigParser.jobBaseIncomeName, Random.nextInt()),
            Pair(ConfigParser.jobBaseXpName, Random.nextInt()),
            Pair(ConfigParser.jobIconName, mock<Material>{}),
            Pair(ConfigParser.jobLeaveOnDeathName, Random.nextBoolean())
        )
    }

    private fun createRandomTask(valid: Boolean = true): Map<Any, Any>{
        return mapOf(
            Pair(ConfigParser.taskIncomeName, Random.nextInt()),
            Pair(ConfigParser.taskXpName, Random.nextInt()),
            Pair(ConfigParser.taskActionName, if(valid){ listOf("DEBUG") }else{ listOf(randomString())}),
            Pair(ConfigParser.taskDescriptionName, randomString()),
            Pair(ConfigParser.taskIconName, icon.name)
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