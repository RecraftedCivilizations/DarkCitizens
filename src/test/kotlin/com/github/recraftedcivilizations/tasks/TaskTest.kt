package com.github.recraftedcivilizations.tasks

import com.github.recraftedcivilizations.BukkitWrapper
import com.github.recraftedcivilizations.dPlayer.DPlayer
import com.github.recraftedcivilizations.dPlayer.DPlayerFactory
import com.github.recraftedcivilizations.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.jobs.IJob
import com.github.recraftedcivilizations.jobs.JobManager
import com.github.recraftedcivilizations.jobs.randomString
import com.github.recraftedcivilizations.parser.dataparser.IParseData
import com.github.recraftedcivilizations.tasks.actions.IAction
import com.nhaarman.mockitokotlin2.*
import net.milkbowl.vault.economy.Economy
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import java.util.*
import kotlin.random.Random

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class TaskTest {

    val economy = mock<Economy>{}
    val action1 = mock<IAction>{}
    val action2 = mock<IAction>{}

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

    val jobMock = "Foo"

    // DPlayers
    private val dPlayerMock1 =
        DPlayerFactory.createDPlayer(uuid1, jobMock, false, false, mapOf(Pair("Bar", 10)), emptyMap())
    private val dPlayerMock2 = DPlayerFactory.createDPlayer(uuid2, jobMock, false, false, emptyMap(), emptyMap())
    private val dPlayerMock3 =
        DPlayerFactory.createDPlayer(uuid3, jobMock, false, false, mapOf(Pair("Bar", 10)), emptyMap())

    // Infrastructure mocks
    private val dataParserMock = mock<IParseData> { }

    private val dPlayerManager = DPlayerManager(dataParserMock)

    private val bukkitWrapper = mock<BukkitWrapper> {}

    private val jobManager = JobManager(dPlayerManager)
    private val taskManager = TaskManager(economy, dPlayerManager)

    @BeforeAll
    fun init(){

        jobManager.setTaskManager(taskManager)
        taskManager.setJobManager(jobManager)

        jobManager.createJob(jobMock, "Bar", 10, emptySet(), emptySet(), 10, 10,10, false, false)

        doNothing().whenever(bukkitWrapper).warning(any())
        doNothing().whenever(bukkitWrapper).notify(any(), any(), any(), any(), any())

        whenever(action1.isCompletedForPlayer(any<Player>())).doAnswer {
            val pl = it.getArgument<Player>(0)

            return@doAnswer if (pl == playerMock1|| pl == playerMock2) {
                true
            } else return@doAnswer false
        }

        whenever(action2.isCompletedForPlayer(any<Player>())).doAnswer {
            val pl = it.getArgument<Player>(0)

            return@doAnswer pl == playerMock1
        }

        whenever(action1.isCompletedForPlayer(any<DPlayer>())).doAnswer {
            val pl = it.getArgument<DPlayer>(0)

            return@doAnswer if (pl == dPlayerMock1|| pl == dPlayerMock2) {
                true
            } else return@doAnswer false
        }

        whenever(action2.isCompletedForPlayer(any<DPlayer>())).doAnswer {
            val pl = it.getArgument<DPlayer>(0)

            return@doAnswer pl == dPlayerMock1
        }

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
    fun cleanup(){
        for(group in dPlayerMock1.groupXps.keys){
            dPlayerMock1.groupXps[group] = 0
        }
    }

    @Test
    fun shouldConstruct(){
        val bukkitWrapperField = Task::class.java.getDeclaredField("bukkitWrapper")
        bukkitWrapperField.isAccessible = true

        var task = Task(randomString(), Random.nextInt(), Random.nextInt(), emptyList(), randomString(), dPlayerManager, economy, jobManager, bukkitWrapper)
        // Dummy tests
        assertEquals(task.name, task.name)
        assertEquals(task.income, task.income)
        assertEquals(task.xp, task.xp)
        assertEquals(0, task.actions.size)
        assertEquals(task.description, task.description)
        assertEquals(bukkitWrapper, bukkitWrapperField.get(task))

        task = Task(randomString(), Random.nextInt(), Random.nextInt(), listOf(action1, action2), randomString(), dPlayerManager, economy, jobManager)
        assertEquals(listOf(action1, action2), task.actions)
        assertEquals(bukkitWrapperField.get(task), bukkitWrapperField.get(task))

    }

    @Test
    fun isCompletedForPlayer() {
        val task = Task(randomString(), Random.nextInt(), Random.nextInt(), listOf<IAction>(action1, action2), randomString(), dPlayerManager, economy, jobManager, bukkitWrapper)

        assertEquals(true, task.isCompletedForPlayer(dPlayerMock1))
        assertEquals(false, task.isCompletedForPlayer(dPlayerMock2))
        assertEquals(false, task.isCompletedForPlayer(dPlayerMock3))

    }

    @Test
    fun testIsCompletedForPlayer() {
        val task = Task(randomString(), Random.nextInt(), Random.nextInt(), listOf<IAction>(action1, action2), randomString(), dPlayerManager, economy, jobManager, bukkitWrapper)

        assertEquals(true, task.isCompletedForPlayer(playerMock1))
        assertEquals(false, task.isCompletedForPlayer(playerMock2))
        assertEquals(false, task.isCompletedForPlayer(playerMock2))
    }

    @Test
    fun testIsCompletedForPlayerWithEmptyActions(){
        val task = Task(randomString(), Random.nextInt(), Random.nextInt(), emptyList(), randomString(), dPlayerManager, economy, jobManager, bukkitWrapper)
        assertEquals(false, task.isCompletedForPlayer(playerMock1))
    }

    @Test
    fun isCompletedForPlayerWithEmptyActions(){
        val task = Task(randomString(), Random.nextInt(), Random.nextInt(), emptyList(), randomString(), dPlayerManager, economy, jobManager, bukkitWrapper)
        assertEquals(false, task.isCompletedForPlayer(dPlayerMock1))
    }

    @Test
    fun completeForPlayer() {
        val playerSet = setOf<Player>(playerMock1)
        val task = Task(randomString(), Random.nextInt(), Random.nextInt(), listOf<IAction>(action1, action2), randomString(), dPlayerManager, economy, jobManager, bukkitWrapper)
        task.completeForPlayer(dPlayerMock1)
        verify(bukkitWrapper).notify("You completed the task ${task.name}", BarColor.GREEN, BarStyle.SOLID, 5, playerSet)
        verify(economy).depositPlayer(playerMock1, task.income.toDouble())
        assertEquals(task.xp, dPlayerMock1.groupXps[jobManager.getJob(jobMock)?.group])
    }

    @Test
    fun testCompleteForPlayer() {
        val playerSet = setOf<Player>(playerMock1)
        val task = Task(randomString(), Random.nextInt(), Random.nextInt(), listOf<IAction>(action1, action2), randomString(), dPlayerManager, economy, jobManager, bukkitWrapper)
        task.completeForPlayer(playerMock1)
        verify(bukkitWrapper).notify("You completed the task ${task.name}", BarColor.GREEN, BarStyle.SOLID, 5, playerSet)
        verify(economy).depositPlayer(playerMock1, task.income.toDouble())
        assertEquals(task.xp, dPlayerMock1.groupXps[jobManager.getJob(jobMock)?.group])
    }

    @Test
    fun pay() {
        val task = Task(randomString(), Random.nextInt(), Random.nextInt(), listOf<IAction>(action1, action2), randomString(), dPlayerManager, economy, jobManager, bukkitWrapper)
        task.pay(dPlayerMock1)
        verify(economy).depositPlayer(playerMock1, task.income.toDouble())
        assertEquals(task.xp, dPlayerMock1.groupXps[jobManager.getJob(jobMock)?.group])
    }

    @Test
    fun testPay() {
        val task = Task(randomString(), Random.nextInt(), Random.nextInt(), listOf<IAction>(action1, action2), randomString(), dPlayerManager, economy, jobManager, bukkitWrapper)
        task.pay(playerMock1)
        verify(economy).depositPlayer(playerMock1, task.income.toDouble())
        assertEquals(task.xp, dPlayerMock1.groupXps[jobManager.getJob(jobMock)?.group])
    }
}