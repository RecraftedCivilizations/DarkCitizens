package com.github.recraftedcivilizations.tasks

import com.github.recraftedcivilizations.BukkitWrapper
import com.github.recraftedcivilizations.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.jobs.JobManager
import com.github.recraftedcivilizations.jobs.randomString
import com.github.recraftedcivilizations.parser.dataparser.IParseData
import com.github.recraftedcivilizations.tasks.actions.IAction
import com.nhaarman.mockitokotlin2.mock
import net.milkbowl.vault.economy.Economy
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import kotlin.random.Random

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class TaskFactoryTest {

    val action = mock<IAction>()
    val economy = mock<Economy> {  }
    val bukkitWrapper = mock<BukkitWrapper> {  }
    val dataParser = mock<IParseData>{  }
    val dPlayerManager = DPlayerManager(dataParser)
    val taskManager = TaskManager(economy, dPlayerManager)
    val jobManager = JobManager(dPlayerManager)

    @BeforeAll
    fun setUp(){
        taskManager.setJobManager(jobManager)
        jobManager.setTaskManager(taskManager)
    }

    @Test
    fun createTask() {
        val task = Task(randomString(), Random.nextInt(), Random.nextInt(), listOf(action), randomString(), dPlayerManager, economy, jobManager, bukkitWrapper)
        val task2 = TaskFactory.createTask(task.name, task.income, task.xp, listOf(action), task.description, dPlayerManager, economy, jobManager)
        assertEquals(task.name, task2.name)
        assertEquals(task.income, task2.income)
        assertEquals(task.xp, task2.xp)
        assertEquals(task.actions[0], task2.actions[0])
        assertEquals(task.actions.size, task2.actions.size)
        assertEquals(task.description, task2.description)

    }
}