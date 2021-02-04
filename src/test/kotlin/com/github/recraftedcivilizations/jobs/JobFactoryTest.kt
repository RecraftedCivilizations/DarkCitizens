package com.github.recraftedcivilizations.jobs

import com.github.recraftedcivilizations.dPlayer.DPlayerManager
import com.nhaarman.mockitokotlin2.mock
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

    @Test
    fun createJob() {
        val job = Job(randomString(), randomString(), Random.nextInt(), emptySet(), emptySet(), Random.nextInt(), Random.nextInt(), Random.nextInt(), Random.nextBoolean(), Random.nextBoolean(), dPlayerManager)
        val thatJob = JobFactory.createJob(job.name, job.group, job.playerLimit, job.tasks, job.canDemote, job.baseIncome, job.baseXPGain, job.minLvl, job.electionRequired, job.permissionRequired, dPlayerManager)
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