package com.github.recraftedcivilizations.jobs


import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.Job
import com.github.recraftedcivilizations.darkcitizens.jobs.JobFactory
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.jobs.elected.GenericElectedJob
import com.github.recraftedcivilizations.darkcitizens.jobs.special.mayor.ElectedMayor
import com.github.recraftedcivilizations.darkcitizens.jobs.special.mayor.UnelectedMayor
import com.github.recraftedcivilizations.darkcitizens.laws.LawManager
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
        val job = createRandomJob(mock{}, icon, dPlayerManager, jobManager)
        val thatJob = JobFactory.createJob(job.name, job.group, job.playerLimit, job.tasks, job.canDemote, job.baseIncome, job.baseXPGain, job.minLvl, false, job.permissionRequired, job.icon, job.leaveOnDeath, job.prefix, dPlayerManager, jobManager)
        assertEquals(job.name, thatJob.name)
        assertEquals(job.group, thatJob.group)
        assertEquals(job.playerLimit, thatJob.playerLimit)
        assertEquals(job.canDemote, thatJob.canDemote)
        assertEquals(job.baseIncome, thatJob.baseIncome)
        assertEquals(job.baseXPGain, thatJob.baseXPGain)
        assertEquals(job.minLvl, thatJob.minLvl)
        assertEquals(job.permissionRequired, thatJob.permissionRequired)
        assertEquals(job.icon, thatJob.icon)
        assertEquals(true, thatJob is Job)
    }

    @Test
    fun createElectedJob() {
        val icon = mock<Material>{}
        val job = createRandomGenericElectedJob(mock{}, icon, dPlayerManager, jobManager)
        val thatJob = JobFactory.createJob(job.name, job.group, job.playerLimit, job.tasks, job.canDemote, job.baseIncome, job.baseXPGain, job.minLvl, true, job.permissionRequired, job.icon, job.leaveOnDeath, job.prefix, dPlayerManager, jobManager, job.candidateTime, job.voteTime, job.candidateFee, job.voteFee)
        assertEquals(job.name, thatJob.name)
        assertEquals(job.group, thatJob.group)
        assertEquals(job.playerLimit, thatJob.playerLimit)
        assertEquals(job.canDemote, thatJob.canDemote)
        assertEquals(job.baseIncome, thatJob.baseIncome)
        assertEquals(job.baseXPGain, thatJob.baseXPGain)
        assertEquals(job.minLvl, thatJob.minLvl)
        assertEquals(job.permissionRequired, thatJob.permissionRequired)
        assertEquals(job.icon, thatJob.icon)
        assertEquals(true, thatJob is GenericElectedJob)
    }

    @Test
    fun createElectedMajorJob(){

        val lawManager = mock<LawManager>{}

        val icon = mock<Material>{}
        val job = createRandomGenericElectedJob(mock{}, icon, dPlayerManager, jobManager)
        val thatJob = JobFactory.createJob(job.name, job.group, job.playerLimit, job.tasks, job.canDemote, job.baseIncome, job.baseXPGain, job.minLvl, true, job.permissionRequired, job.icon, job.leaveOnDeath, job.prefix, dPlayerManager, jobManager, job.candidateTime, job.voteTime, job.candidateFee, job.voteFee, true, lawManager)
        assertEquals(job.name, thatJob.name)
        assertEquals(job.group, thatJob.group)
        assertEquals(job.playerLimit, thatJob.playerLimit)
        assertEquals(job.canDemote, thatJob.canDemote)
        assertEquals(job.baseIncome, thatJob.baseIncome)
        assertEquals(job.baseXPGain, thatJob.baseXPGain)
        assertEquals(job.minLvl, thatJob.minLvl)
        assertEquals(job.permissionRequired, thatJob.permissionRequired)
        assertEquals(job.icon, thatJob.icon)
        assertEquals(true, thatJob is ElectedMayor)

    }

    @Test
    fun createUnelectedMajorJob(){

        val lawManager = mock<LawManager>{}

        val icon = mock<Material>{}
        val job = createRandomGenericElectedJob(mock{}, icon, dPlayerManager, jobManager)
        val thatJob = JobFactory.createJob(job.name, job.group, job.playerLimit, job.tasks, job.canDemote, job.baseIncome, job.baseXPGain, job.minLvl, false, job.permissionRequired, job.icon, job.leaveOnDeath, job.prefix, dPlayerManager, jobManager, job.candidateTime, job.voteTime, job.candidateFee, job.voteFee, true, lawManager)
        assertEquals(job.name, thatJob.name)
        assertEquals(job.group, thatJob.group)
        assertEquals(job.playerLimit, thatJob.playerLimit)
        assertEquals(job.canDemote, thatJob.canDemote)
        assertEquals(job.baseIncome, thatJob.baseIncome)
        assertEquals(job.baseXPGain, thatJob.baseXPGain)
        assertEquals(job.minLvl, thatJob.minLvl)
        assertEquals(job.permissionRequired, thatJob.permissionRequired)
        assertEquals(job.icon, thatJob.icon)
        assertEquals(true, thatJob is UnelectedMayor)
    }
}