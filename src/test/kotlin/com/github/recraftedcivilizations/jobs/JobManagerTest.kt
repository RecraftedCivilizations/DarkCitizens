package com.github.recraftedcivilizations.jobs

import com.github.recraftedcivilizations.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.parser.dataparser.IParseData
import com.github.recraftedcivilizations.tasks.ITask
import com.github.recraftedcivilizations.tasks.TaskManager
import com.nhaarman.mockitokotlin2.*
import net.milkbowl.vault.economy.Economy
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
    val taskManager = TaskManager(econ, dPlayerManager)

    @BeforeAll
    fun init() {
    }

    @Test
    fun shouldConstruct(){
        val jobManager = JobManager(dPlayerManager)
        jobManager.setTaskManager(taskManager)
    }

    @Test
    fun createJob() {
        val tasks = setOf<String>("Foo", "Bar", "FooBar")

        val jobManager = JobManager(dPlayerManager)
        jobManager.setTaskManager(taskManager)
        val job = Job(randomString(), randomString(), Random.nextInt(), setOf(task1, task2), emptySet(), Random.nextInt(), Random.nextInt(), Random.nextInt(), Random.nextBoolean(), Random.nextBoolean(), dPlayerManager)
        jobManager.createJob(job.name, job.group, job.playerLimit, tasks, job.canDemote, job.baseIncome, job.baseXPGain, job.minLvl, job.electionRequired, job.permissionRequired)

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
        assertEquals(job.electionRequired, thatJob.electionRequired)
        assertEquals(job.permissionRequired, thatJob.permissionRequired)
    }

    @Test
    fun getJob() {
        val tasks = setOf<String>("Foo", "Bar", "FooBar")

        val jobManager = JobManager(dPlayerManager)
        jobManager.setTaskManager(taskManager)
        val job = Job(randomString(), randomString(), Random.nextInt(), setOf(task1, task2), emptySet(), Random.nextInt(), Random.nextInt(), Random.nextInt(), Random.nextBoolean(), Random.nextBoolean(), dPlayerManager)

        assertEquals(null, jobManager.getJob(randomString()))

        jobManager.createJob(job.name, job.group, job.playerLimit, tasks, job.canDemote, job.baseIncome, job.baseXPGain, job.minLvl, job.electionRequired, job.permissionRequired)
        val thatJob = jobManager.getJob(job.name)

        thatJob!!

        assertEquals(job.name, thatJob.name)
        assertEquals(job.group, thatJob.group)
        assertEquals(job.playerLimit, thatJob.playerLimit)
        assertEquals(job.canDemote, thatJob.canDemote)
        assertEquals(job.baseIncome, thatJob.baseIncome)
        assertEquals(job.baseXPGain, thatJob.baseXPGain)
        assertEquals(job.minLvl, thatJob.minLvl)
        assertEquals(job.electionRequired, thatJob.electionRequired)
        assertEquals(job.permissionRequired, thatJob.permissionRequired)

    }
}