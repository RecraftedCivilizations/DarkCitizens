package com.github.recraftedcivilizations.jobs


import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.Job
import com.github.recraftedcivilizations.darkcitizens.jobs.JobFactory
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.nhaarman.mockitokotlin2.mock
import org.bukkit.Material
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random

fun randomString(outputStrLength: Int = 10): String{
    val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    val randomString = (1..outputStrLength)
        .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
    return randomString
}

internal class JobFactoryTest {

    val dPlayerManager = mock<DPlayerManager>{}
    val jobManager = mock<JobManager>{}
    //val jobManager = JobManager(dPlayerManager)

    @Test
    fun createJob() {
        val icon = mock<Material>{}
        val job = Job(randomString(), randomString(), Random.nextInt(), emptySet(), emptySet(), Random.nextInt(), Random.nextInt(), Random.nextInt(), Random.nextBoolean(), Random.nextBoolean(), icon, dPlayerManager, jobManager)
        val thatJob = JobFactory.createJob(job.name, job.group, job.playerLimit, job.tasks, job.canDemote, job.baseIncome, job.baseXPGain, job.minLvl, job.electionRequired, job.permissionRequired, job.icon, dPlayerManager, jobManager)
        assertEquals(job.name, thatJob.name)
        assertEquals(job.group, thatJob.group)
        assertEquals(job.playerLimit, thatJob.playerLimit)
        assertEquals(job.canDemote, thatJob.canDemote)
        assertEquals(job.baseIncome, thatJob.baseIncome)
        assertEquals(job.baseXPGain, thatJob.baseXPGain)
        assertEquals(job.minLvl, thatJob.minLvl)
        assertEquals(job.electionRequired, thatJob.electionRequired)
        assertEquals(job.permissionRequired, thatJob.permissionRequired)
        assertEquals(job.icon, thatJob.icon)
    }
}