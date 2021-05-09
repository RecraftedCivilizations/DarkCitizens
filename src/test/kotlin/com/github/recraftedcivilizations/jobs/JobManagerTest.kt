package com.github.recraftedcivilizations.jobs


import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.groups.GroupManager
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import com.github.recraftedcivilizations.darkcitizens.jobs.Job
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.jobs.elected.GenericElectedJob
import com.github.recraftedcivilizations.darkcitizens.laws.LawManager
import com.github.recraftedcivilizations.darkcitizens.parser.dataparser.IParseData
import com.github.recraftedcivilizations.darkcitizens.tasks.ITask
import com.github.recraftedcivilizations.darkcitizens.tasks.TaskManager
import com.nhaarman.mockitokotlin2.*
import net.milkbowl.vault.economy.Economy
import org.bukkit.Material
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import kotlin.random.Random


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class JobManagerTest {
    val task1 =  mock<ITask> {}

    val task2 = mock<ITask>{}

    val dataParser = mock<IParseData>{}
    val econ = mock<Economy>{}

    val dPlayerManager = DPlayerManager(dataParser)
    val groupManager = GroupManager()
    val taskManager = TaskManager(econ, dPlayerManager, groupManager)
    val lawManager = mock<LawManager>{}

    @BeforeAll
    fun init() {
    }

    @Test
    fun shouldConstruct(){
        val jobManager = JobManager(dPlayerManager, lawManager)
        jobManager.setTaskManager(taskManager)
    }

    @Test
    fun createJob() {
        val tasks = setOf<String>("Foo", "Bar", "FooBar")
        val icon = mock<Material>{}

        val jobManager = JobManager(dPlayerManager, lawManager)
        jobManager.setTaskManager(taskManager)
        val job = createRandomJob(mock{}, icon, dPlayerManager, jobManager)
        jobManager.createJob(job.name, job.group, job.playerLimit, tasks, job.canDemote, job.baseIncome, job.baseXPGain, job.minLvl, false, job.permissionRequired, job.icon, job.leaveOnDeath, job.prefix)

        val jobsField = JobManager::class.java.getDeclaredField("jobs")
        jobsField.isAccessible = true
        val jobs = jobsField.get(jobManager) as Set<IJob>
        var thatJob: IJob? = null
        for (accessedJob in jobs){
            if (accessedJob.name == job.name){
                thatJob = accessedJob
            }
        }
        thatJob!!

        assertEquals(job.name, thatJob.name)
        assertEquals(job.group, thatJob.group)
        assertEquals(job.playerLimit, thatJob.playerLimit)
        assertEquals(job.canDemote, thatJob.canDemote)
        assertEquals(job.baseIncome, thatJob.baseIncome)
        assertEquals(job.baseXPGain, thatJob.baseXPGain)
        assertEquals(job.minLvl, thatJob.minLvl)
        assertEquals(job.permissionRequired, thatJob.permissionRequired)
        assertEquals(true, thatJob is Job)
    }

    @Test
    fun createElectedJob() {
        val tasks = setOf<String>("Foo", "Bar", "FooBar")
        val icon = mock<Material>{}

        val jobManager = JobManager(dPlayerManager, lawManager)
        jobManager.setTaskManager(taskManager)
        val job = createRandomGenericElectedJob(mock{}, icon, dPlayerManager, jobManager)
        jobManager.createJob(job.name, job.group, job.playerLimit, tasks, job.canDemote, job.baseIncome, job.baseXPGain, job.minLvl, true, job.permissionRequired, job.icon, job.leaveOnDeath, job.prefix, job.candidateTime, job.voteTime, job.voteFee, job.candidateFee, false)

        val jobsField = JobManager::class.java.getDeclaredField("jobs")
        jobsField.isAccessible = true
        val jobs = jobsField.get(jobManager) as Set<IJob>
        var thatJob: IJob? = null
        for (accessedJob in jobs){
            if (accessedJob.name == job.name){
                thatJob = accessedJob
            }
        }
        thatJob!!

        assertEquals(job.name, thatJob.name)
        assertEquals(job.group, thatJob.group)
        assertEquals(job.playerLimit, thatJob.playerLimit)
        assertEquals(job.canDemote, thatJob.canDemote)
        assertEquals(job.baseIncome, thatJob.baseIncome)
        assertEquals(job.baseXPGain, thatJob.baseXPGain)
        assertEquals(job.minLvl, thatJob.minLvl)
        assertEquals(job.permissionRequired, thatJob.permissionRequired)
        assertEquals(true, thatJob is GenericElectedJob)
    }

    @Test
    fun getJob() {
        val tasks = setOf<String>("Foo", "Bar", "FooBar")
        val icon = mock<Material>{}

        val jobManager = JobManager(dPlayerManager, lawManager)
        jobManager.setTaskManager(taskManager)
        val job = createRandomJob(mock{}, icon, dPlayerManager, jobManager)

        assertEquals(null, jobManager.getJob(randomString()))

        jobManager.createJob(job.name, job.group, job.playerLimit, tasks, job.canDemote, job.baseIncome, job.baseXPGain, job.minLvl, false, job.permissionRequired, job.icon, job.leaveOnDeath, job.prefix)
        val thatJob = jobManager.getJob(job.name)

        thatJob!!

        assertEquals(job.name, thatJob.name)
        assertEquals(job.group, thatJob.group)
        assertEquals(job.playerLimit, thatJob.playerLimit)
        assertEquals(job.canDemote, thatJob.canDemote)
        assertEquals(job.baseIncome, thatJob.baseIncome)
        assertEquals(job.baseXPGain, thatJob.baseXPGain)
        assertEquals(job.minLvl, thatJob.minLvl)
        assertEquals(job.permissionRequired, thatJob.permissionRequired)

    }
}