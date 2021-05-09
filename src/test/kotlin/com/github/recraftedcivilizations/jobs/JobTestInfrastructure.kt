package com.github.recraftedcivilizations.jobs

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import com.github.recraftedcivilizations.darkcitizens.jobs.Job
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.jobs.elected.GenericElectedJob
import org.bukkit.Material
import kotlin.random.Random
import kotlin.random.nextInt

fun createRandomJob(bukkitWrapper: BukkitWrapper, icon: Material, dPlayerManager: DPlayerManager, jobManager: JobManager) : IJob{
    return Job(randomString(), randomString(), Random.nextInt(1..10), emptySet(), emptySet(), Random.nextInt(), Random.nextInt(), 0, false, icon, Random.nextBoolean(), randomString(), dPlayerManager, jobManager, bukkitWrapper)
}

fun createRandomGenericElectedJob(bukkitWrapper: BukkitWrapper, icon: Material, dPlayerManager: DPlayerManager, jobManager: JobManager): GenericElectedJob{
    return GenericElectedJob(Random.nextBoolean(), Random.nextInt(), Random.nextInt(), Random.nextInt(), Random.nextInt(), dPlayerManager, randomString(), randomString(), Random.nextInt(), emptySet(), emptySet(), Random.nextInt(), Random.nextInt(), Random.nextInt(), Random.nextBoolean(), icon, randomString(), jobManager)
}