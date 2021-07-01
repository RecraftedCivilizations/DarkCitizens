package com.github.recraftedcivilizations.tasks

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.groups.GroupManager
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.laws.LawManager
import com.github.recraftedcivilizations.darkcitizens.parser.dataparser.IParseData
import com.github.recraftedcivilizations.darkcitizens.tasks.Task
import com.github.recraftedcivilizations.darkcitizens.tasks.TaskFactory
import com.github.recraftedcivilizations.darkcitizens.tasks.TaskManager
import com.github.recraftedcivilizations.darkcitizens.actions.IAction
import com.github.recraftedcivilizations.jobs.randomString
import com.nhaarman.mockitokotlin2.mock
import net.milkbowl.vault.economy.Economy
import org.bukkit.Material
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import kotlin.random.Random

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class TaskFactoryTest {
    val lawManager = mock<LawManager>{}
    val action = mock<IAction>()
    val economy = mock<Economy> {  }
    val bukkitWrapper = mock<BukkitWrapper> {  }
    val dataParser = mock<IParseData>{  }
    val dPlayerManager = DPlayerManager(dataParser)
    val groupManager = GroupManager()
    val taskManager = TaskManager(economy, dPlayerManager, groupManager)
    val jobManager = JobManager(dPlayerManager, lawManager)
    val icon = Material.PLAYER_HEAD

    @BeforeAll
    fun setUp(){
        taskManager.setJobManager(jobManager)
        jobManager.setTaskManager(taskManager)
    }

    @Test
    fun createTask() {
        val task = Task(randomString(), Random.nextInt(), Random.nextInt(), listOf(action), randomString(), icon, dPlayerManager, economy, jobManager, groupManager, bukkitWrapper)
        val task2 = TaskFactory.createTask(task.name, task.income, task.xp, listOf(action), task.description, icon, dPlayerManager, economy, jobManager, groupManager)
        assertEquals(task.name, task2.name)
        assertEquals(task.income, task2.income)
        assertEquals(task.xp, task2.xp)
        assertEquals(task.actions[0], task2.actions[0])
        assertEquals(task.actions.size, task2.actions.size)
        assertEquals(task.description, task2.description)
        assertEquals(task.icon, task2.icon)

    }
}