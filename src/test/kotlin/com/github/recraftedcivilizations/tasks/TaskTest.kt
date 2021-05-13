package com.github.recraftedcivilizations.tasks

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.groups.Group
import com.github.recraftedcivilizations.darkcitizens.groups.GroupManager
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.parser.dataparser.IParseData
import com.github.recraftedcivilizations.darkcitizens.tasks.ITask
import com.github.recraftedcivilizations.darkcitizens.tasks.Task
import com.github.recraftedcivilizations.darkcitizens.actions.IAction
import com.github.recraftedcivilizations.jobs.randomString
import com.nhaarman.mockitokotlin2.*
import net.milkbowl.vault.economy.Economy
import org.bukkit.Material
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

internal class TaskTest {

    private var economy = mock<Economy>{}
    private var action1 = mock<IAction>{}
    private var action2 = mock<IAction>{}

    private val uuid1 = UUID.randomUUID()
    private val uuid2 = UUID.randomUUID()
    private val uuid3 = UUID.randomUUID()

    private val icon = Material.PLAYER_HEAD

    private val playerMock1 =
        mock<Player> {
            on { uniqueId } doReturn uuid1
            on { hasPermission("drp.job.join.Foo") } doReturn true
        }
    private val playerMock2 =
        mock<Player> {
            on { uniqueId } doReturn uuid2;
            on { hasPermission("drp.job.join.Foo") } doReturn false
        }

    private val playerMock3 =
        mock<Player> {
            on { uniqueId } doReturn uuid3
            on { hasPermission("drp.job.join.Foo") } doReturn true
        }

    private val jobName = randomString()
    private val groupName = randomString()

    private val jobMock = mock<IJob>{
        on { group } doReturn groupName
    }

    private val groupMock = mock<Group>{}

    private val dPlayerMock1 = mock<DPlayer>{
        on { job } doReturn jobName
        on { uuid } doReturn uuid1
    }
    private val dPlayerMock2 = mock<DPlayer>()
    private val dPlayerMock3 = mock<DPlayer>()

    private var dataParser = mock<IParseData>{}
    private var dPlayerManager = DPlayerManager(dataParser)
    private var jobManager: JobManager = mock{}
    private var groupManager: GroupManager = mock<GroupManager>{}
    private var bukkitWrapper = mock<BukkitWrapper>()

    init {
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

    }


    fun randomTaskArgs(): Map<String, Any> {
        return mapOf(
            Pair("name", randomString()),
            Pair("income", Random.nextInt(0..100)),
            Pair("xp", Random.nextInt(0..100)),
            Pair("description", randomString()),
        )
    }

    fun assertTask(task: ITask, taskArgs: Map<String, Any>){
        assertEquals(taskArgs["name"], task.name)
        assertEquals(taskArgs["income"], task.income)
        assertEquals(taskArgs["xp"], task.xp)
        assertEquals(taskArgs["description"], task.description)
    }

    private fun mockFunc(){
        whenever(dataParser.getDPlayer(any())).doAnswer {
            when (it.getArgument<UUID>(0)) {
                uuid1 -> dPlayerMock1
                uuid2 -> dPlayerMock2
                uuid3 -> dPlayerMock3
                else -> null
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

        whenever(jobManager.getJob(any())) doReturn jobMock
        whenever(groupManager.getGroup(groupName)) doReturn groupMock
    }

    @BeforeEach
    fun refreshMocks(){
        dataParser = mock {}
        dPlayerManager = DPlayerManager(dataParser)
        jobManager = mock{}
        groupManager = mock{}
        bukkitWrapper = mock{}


        mockFunc()

    }

    @Test
    fun shouldConstruct(){
        val taskArgs = randomTaskArgs()
        val task = Task(taskArgs["name"] as String, taskArgs["income"] as Int, taskArgs["xp"] as Int, emptyList(), taskArgs["description"] as String, icon, dPlayerManager, economy, jobManager, groupManager)
        assertTask(task, taskArgs)
    }

    @Test
    fun isCompletedForPlayer(){
        val taskArgs = randomTaskArgs()
        val task = Task(taskArgs["name"] as String, taskArgs["income"] as Int, taskArgs["xp"] as Int, listOf(action1, action2), taskArgs["description"] as String, icon, dPlayerManager, economy, jobManager, groupManager)

        assertEquals(true, task.isCompletedForPlayer(playerMock1))
        assertEquals(false, task.isCompletedForPlayer(playerMock2))
        assertEquals(false, task.isCompletedForPlayer(playerMock3))
    }

    @Test
    fun isCompleteWithEmptyActions(){
        val taskArgs = randomTaskArgs()
        val task = Task(taskArgs["name"] as String, taskArgs["income"] as Int, taskArgs["xp"] as Int, emptyList(), taskArgs["description"] as String, icon, dPlayerManager, economy, jobManager, groupManager)

        assertEquals(false, task.isCompletedForPlayer(playerMock1))
    }

    @Test
    fun pay(){

        val taskArgs = randomTaskArgs()
        val task = Task(taskArgs["name"] as String, taskArgs["income"] as Int, taskArgs["xp"] as Int, emptyList(), taskArgs["description"] as String, icon, dPlayerManager, economy, jobManager, groupManager, bukkitWrapper)

        task.pay(playerMock1)

        verify(dPlayerMock1).addXP(groupMock, taskArgs["xp"] as Int)
        verify(economy).depositPlayer(playerMock1, (taskArgs["income"] as Int).toDouble())
    }

    @Test
    fun completeForPlayer(){

        val taskArgs = randomTaskArgs()
        val task = Task(taskArgs["name"] as String, taskArgs["income"] as Int, taskArgs["xp"] as Int, emptyList(), taskArgs["description"] as String, icon, dPlayerManager, economy, jobManager, groupManager, bukkitWrapper)

        task.completeForPlayer(dPlayerMock1)

        verify(bukkitWrapper).notify("You completed the task ${taskArgs["name"] as String}", BarColor.GREEN, BarStyle.SOLID, 5, setOf(playerMock1))
        verify(dPlayerMock1).addXP(groupMock, taskArgs["xp"] as Int)
        verify(economy).depositPlayer(playerMock1, (taskArgs["income"] as Int).toDouble())

    }


}