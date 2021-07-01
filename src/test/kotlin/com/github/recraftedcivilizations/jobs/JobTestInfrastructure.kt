package com.github.recraftedcivilizations.jobs

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import com.github.recraftedcivilizations.darkcitizens.jobs.Job
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.jobs.elected.GenericElectedJob
import com.github.recraftedcivilizations.darkcitizens.jobs.special.mayor.ElectedMayor
import com.github.recraftedcivilizations.darkcitizens.jobs.special.mayor.UnelectedMayor
import com.github.recraftedcivilizations.darkcitizens.laws.LawManager
import org.bukkit.Material
import kotlin.random.Random
import kotlin.random.nextInt

fun createRandomJob(bukkitWrapper: BukkitWrapper, icon: Material, dPlayerManager: DPlayerManager, jobManager: JobManager) : IJob{
    return Job(randomString(), randomString(), Random.nextInt(1..10), emptySet(), emptySet(), Random.nextInt(), Random.nextInt(), 0, false, icon, Random.nextBoolean(), randomString(), dPlayerManager, jobManager, bukkitWrapper)
}

fun createRandomGenericElectedJob(bukkitWrapper: BukkitWrapper, icon: Material, dPlayerManager: DPlayerManager, jobManager: JobManager): GenericElectedJob{
    return GenericElectedJob(Random.nextBoolean(), Random.nextInt(), Random.nextInt(), Random.nextInt(), Random.nextInt(), dPlayerManager, randomString(), randomString(), Random.nextInt(), emptySet(), emptySet(), Random.nextInt(), Random.nextInt(), Random.nextInt(), Random.nextBoolean(), icon, randomString(), jobManager)
}

fun  createRandomElectedMajorJob(bukkitWrapper: BukkitWrapper, icon: Material, dPlayerManager: DPlayerManager, jobManager: JobManager, lawManager: LawManager): ElectedMayor{
    return ElectedMayor(lawManager, randomString(), randomString(), Random.nextInt(1..10), emptySet(), emptySet(), Random.nextInt(), Random.nextInt(), 0, false, icon, Random.nextBoolean(), randomString(), dPlayerManager, jobManager, Random.nextInt(), Random.nextInt(), Random.nextInt(), Random.nextInt(), bukkitWrapper)
}

fun createRandomUnelectedMajorJob(bukkitWrapper: BukkitWrapper, icon: Material, dPlayerManager: DPlayerManager, jobManager: JobManager, lawManager: LawManager): UnelectedMayor{
    return UnelectedMayor(lawManager, randomString(), randomString(), Random.nextInt(1..10), emptySet(), emptySet(), Random.nextInt(), Random.nextInt(), 0, false, icon, Random.nextBoolean(), randomString(), dPlayerManager, jobManager, bukkitWrapper)
}