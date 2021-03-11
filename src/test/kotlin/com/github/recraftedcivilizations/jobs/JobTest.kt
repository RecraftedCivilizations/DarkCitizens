package com.github.recraftedcivilizations.jobs

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerFactory
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.Job
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.parser.dataparser.IParseData
import com.github.recraftedcivilizations.darkcitizens.tasks.ITask
import com.nhaarman.mockitokotlin2.*
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

@TestInstance(Lifecycle.PER_CLASS)
internal class JobTest {
    // UUID's
    private val uuid1 = UUID.randomUUID()
    private val uuid2 = UUID.randomUUID()
    private val uuid3 = UUID.randomUUID()

    // PLayer Mocks
    val playerMock1 =
        mock<Player> {
            on { uniqueId } doReturn uuid1
            on { hasPermission("drp.job.join.Foo") } doReturn true
        }
    val playerMock2 =
        mock<Player> {
            on { uniqueId } doReturn uuid2;
            on { hasPermission("drp.job.join.Foo") } doReturn false
        }

    val playerMock3 =
        mock<Player> {
            on { uniqueId } doReturn uuid3
            on { hasPermission("drp.job.join.Foo") } doReturn true
        }

    // DPlayers
    private val dPlayerMock1 =
        DPlayerFactory.createDPlayer(uuid1, null, false, false, mapOf(Pair("Bar", 10)), emptyMap())
    private val dPlayerMock2 = DPlayerFactory.createDPlayer(uuid2, null, false, false, emptyMap(), emptyMap())
    private val dPlayerMock3 =
        DPlayerFactory.createDPlayer(uuid3, null, false, false, mapOf(Pair("Bar", 10)), emptyMap())

    // Infrastructure mocks
    private val dataParserMock = mock<IParseData> { }

    private val dPlayerManager = DPlayerManager(dataParserMock)

    private val bukkitWrapper = mock<BukkitWrapper> { }

    val jobManager = mock<JobManager>{}
    // Job Mocks
    private val icon = mock<Material>{}
    private val job =
        Job("Foo", "Bar", 5, emptySet<ITask>(), emptySet(), 10, 10, 10, false, false, icon, dPlayerManager, jobManager, bukkitWrapper)

    @BeforeAll
    fun init() {
        // Get DPlayers by their uuid
        whenever(dataParserMock.getDPlayer(any())).doAnswer {
            when (it.getArgument<UUID>(0)) {
                uuid1 -> {
                    return@doAnswer dPlayerMock1
                }
                uuid2 -> {
                    return@doAnswer dPlayerMock2
                }
                uuid3 -> {
                    return@doAnswer dPlayerMock3
                }
                else -> {
                    return@doAnswer null
                }
            }
        }

        // Get player mocks by UUID/DPlayer obj
        whenever(bukkitWrapper.getPlayer(any<DPlayer>())).doAnswer {
            val arg = it.getArgument<DPlayer>(0)
            when (arg.uuid) {
                uuid1 -> playerMock1
                uuid2 -> playerMock2
                uuid3 -> playerMock3
                else -> {
                    return@doAnswer null
                }
            }
        }

        whenever(bukkitWrapper.getPlayer(any<UUID>())).doAnswer {
            when (it.getArgument<UUID>(0)) {
                uuid1 -> playerMock1
                uuid2 -> playerMock2
                uuid3 -> playerMock3
                else -> null
            }
        }
    }

    @AfterEach
    fun setUp() {
        for (member in job.currentMembers) {
            job.currentMembers.remove(member)
        }
    }

    @Test
    fun canJoin() {
        val icon = mock<Material>{}
        // Group lvls
        assertEquals(false, job.canJoin(dPlayerMock2))
        verify(playerMock2).sendMessage("${ChatColor.RED}You don't have the required level to join this job!")

        // Permissions
        var job = Job("Foo", "Bar", 5, emptySet<ITask>(), emptySet(), 10, 10, 10, false, true, icon, dPlayerManager, jobManager, bukkitWrapper)
        dPlayerMock2.groupLvls[job.group] = job.minLvl

        assertEquals(false, job.canJoin(dPlayerMock2))
        verify(playerMock2).sendMessage("${ChatColor.RED}You don't have the required level to join this job!")

        dPlayerMock2.groupLvls[job.group] = 0

        // Player limit
        job = Job("Foo", "Bar", 1, emptySet<ITask>(), emptySet(), 10, 10, 10, false, false, icon, dPlayerManager, jobManager, bukkitWrapper)
        job.addPlayer(dPlayerMock1)
        job.canJoin(dPlayerMock3)
        verify(playerMock3).sendMessage("${ChatColor.RED}There are too many players in this job")

        // Already in the job
        job = Job("Foo", "Bar", 2, emptySet<ITask>(), emptySet(), 10, 10, 10, false, false, icon, dPlayerManager, jobManager, bukkitWrapper)
        job.addPlayer(dPlayerMock1)
        assertEquals(false, job.canJoin(dPlayerMock1))
        verify(playerMock1).sendMessage("${ChatColor.RED}You are already in this job")
    }

    @Test
    fun addPlayer() {
        job.addPlayer(playerMock1)
        assertEquals(true, job.isMember(playerMock1))
    }

    @Test
    fun removePlayer() {
        job.addPlayer(dPlayerMock1)
        job.removePlayer(dPlayerMock1)
        assertEquals(false, job.isMember(playerMock1))
    }

    @Test
    fun testRemovePlayer() {
        job.addPlayer(playerMock1)
        job.removePlayer(playerMock1)
        assertEquals(false, job.isMember(playerMock1))
    }

    @Test
    fun isMember(){
        job.addPlayer(dPlayerMock1)
        assertEquals(true, job.isMember(dPlayerMock1))
        job.removePlayer(dPlayerMock1)
        assertEquals(false, job.isMember(dPlayerMock1))
    }
    @Test
    fun isMemberWithPlayer(){
        job.addPlayer(playerMock1)
        assertEquals(true, job.isMember(playerMock1))
        job.removePlayer(playerMock1)
        assertEquals(false, job.isMember(playerMock1))
    }

    @Test
    fun isMemberWithUUID(){
        job.addPlayer(playerMock1)
        assertEquals(true, job.isMember(uuid1))
        job.removePlayer(playerMock1)
        assertEquals(false, job.isMember(uuid1))
    }

    @Test
    fun shouldConstruct(){
        val job =
            Job("Foo", "Bar", 5, emptySet<ITask>(), emptySet(), 10, 10, 10, false, false, icon, dPlayerManager, jobManager)

        assertEquals("Foo", job.name)
        assertEquals("Bar", job.group)
        assertEquals(5, job.playerLimit)
        assertEquals(emptySet<ITask>(), job.tasks)
        assertEquals(emptySet<String>(), job.canDemote)
        assertEquals(10, job.baseIncome)
        assertEquals(10, job.baseXPGain)
        assertEquals(10, job.minLvl)
        assertEquals(false, job.electionRequired)
        assertEquals(false, job.permissionRequired)
        // Make the dPlayer field public
        val dPlayerField = Job::class.java.getDeclaredField("dPlayerManager")
        dPlayerField.isAccessible = true
        assertEquals(dPlayerManager, dPlayerField.get(job))
    }

    @Test
    fun join(){
        val icon = mock<Material>{}
        val jobManager = JobManager(dPlayerManager)
        val job1 = Job(randomString(), randomString(), Random.nextInt(1..10), emptySet(), emptySet(), Random.nextInt(), Random.nextInt(), 0, false, false, icon, dPlayerManager, jobManager)
        val job2 = Job(randomString(), randomString(), Random.nextInt(1..10), emptySet(), emptySet(), Random.nextInt(), Random.nextInt(), 0, false, true, icon, dPlayerManager, jobManager, bukkitWrapper)
        jobManager.createJob(job1.name, job1.group, job1.playerLimit, emptySet(), job1.canDemote, job1.baseIncome, job1.baseXPGain, job1.minLvl, job1.electionRequired, job1.permissionRequired, job1.icon)
        jobManager.createJob(job2.name, job2.group, job2.playerLimit, emptySet(), job2.canDemote, job2.baseIncome, job2.baseXPGain, job2.minLvl, job2.electionRequired, job2.permissionRequired, job2.icon)

        var toJoin = jobManager.getJob(job1.name)!!
        toJoin.setBukkitWrapper(bukkitWrapper)

        toJoin.join(playerMock1)
        verify(playerMock1).sendMessage("${ChatColor.GREEN}You successfully joined the job ${job1.name}")
        verify(dataParserMock).setDPlayer(dPlayerMock1)
        assertEquals(dPlayerMock1.job, job1.name)

        toJoin.removePlayer(dPlayerMock1)
        dPlayerMock1.job = null

        dPlayerMock2.job = null
        toJoin = jobManager.getJob(job2.name)!!
        toJoin.setBukkitWrapper(bukkitWrapper)
        toJoin.join(playerMock2)
        verify(playerMock2).sendMessage("${ChatColor.RED}You don't have the required permissions to join this job!")
        assertEquals(null, dPlayerMock2.job)

        dPlayerMock3.job = job2.name
        job2.addPlayer(dPlayerMock3)

        toJoin = jobManager.getJob(job1.name)!!
        toJoin.setBukkitWrapper(bukkitWrapper)

        toJoin.join(playerMock3)
        assertEquals(job1.name, dPlayerMock3.job)

    }

    @Test
    fun leave(){
        val jobManager = JobManager(dPlayerManager)
        val job1 = Job(randomString(), randomString(), Random.nextInt(1..10), emptySet(), emptySet(), Random.nextInt(), Random.nextInt(), 0, false, false, icon, dPlayerManager, jobManager)
        val job2 = Job(randomString(), randomString(), Random.nextInt(1..10), emptySet(), emptySet(), Random.nextInt(), Random.nextInt(), 0, false, true, icon, dPlayerManager, jobManager, bukkitWrapper)
        jobManager.createJob(job1.name, job1.group, job1.playerLimit, emptySet(), job1.canDemote, job1.baseIncome, job1.baseXPGain, job1.minLvl, job1.electionRequired, job1.permissionRequired, icon)
        jobManager.createJob(job2.name, job2.group, job2.playerLimit, emptySet(), job2.canDemote, job2.baseIncome, job2.baseXPGain, job2.minLvl, job2.electionRequired, job2.permissionRequired, icon)

        val toLeave = jobManager.getJob(job1.name)!!
        toLeave.setBukkitWrapper(bukkitWrapper)
        toLeave.join(dPlayerMock1)
        toLeave.leave(playerMock1)
        assertEquals(null, dPlayerMock1.job)
    }
}