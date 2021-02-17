package com.github.recraftedcivilizations.tasks

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.parser.dataparser.IParseData
import com.github.recraftedcivilizations.darkcitizens.tasks.ITask
import com.github.recraftedcivilizations.darkcitizens.tasks.Task
import com.github.recraftedcivilizations.darkcitizens.tasks.TaskManager
import com.github.recraftedcivilizations.darkcitizens.tasks.actions.Actions
import com.github.recraftedcivilizations.darkcitizens.tasks.actions.IAction
import com.github.recraftedcivilizations.jobs.randomString
import com.nhaarman.mockitokotlin2.mock
import net.milkbowl.vault.economy.Economy
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random

internal class TaskManagerTest {

    val econ = mock<Economy>{}
    val dataParser = mock<IParseData>{}
    val dPlayerManager = DPlayerManager(dataParser)
    val action = mock<IAction>{}
    val jobManager = JobManager(dPlayerManager)

    val taskField = TaskManager::class.java.getDeclaredField("tasks")

    init {
        taskField.isAccessible =true
    }

    @Test
    fun shouldConstruct(){
        val taskManager = TaskManager(econ, dPlayerManager)
    }

    @Test
    fun createTask() {
        val taskManager = TaskManager(econ, dPlayerManager)
        val task1 = Task(randomString(), Random.nextInt(), Random.nextInt(), listOf(Actions.DEBUG), randomString(), dPlayerManager, econ, jobManager)

        taskManager.setJobManager(jobManager)
        jobManager.setTaskManager(taskManager)

        taskManager.createTask(task1.name, task1.income, task1.xp, listOf("DEBUG", "FOO"), task1.description)

        val tasks = taskField.get(taskManager) as Set<*>

        assertEquals(1, tasks.size)
        val task2 = tasks.iterator().next() as ITask

        assertEquals(task1.name, task2.name)
        assertEquals(task1.income, task2.income)
        assertEquals(task1.xp, task2.xp)
        assertEquals(task1.actions[0], task2.actions[0])
        assertEquals(task1.actions.size, task2.actions.size)
        assertEquals(task1.description, task2.description)

    }

    @Test
    fun getTask() {
        val taskManager = TaskManager(econ, dPlayerManager)

        taskManager.setJobManager(jobManager)
        jobManager.setTaskManager(taskManager)

        val task1 = Task(randomString(), Random.nextInt(), Random.nextInt(), listOf(Actions.DEBUG), randomString(), dPlayerManager, econ, jobManager)
        taskManager.createTask(task1.name, task1.income, task1.xp, listOf("DEBUG"), task1.description)
        val task2 = taskManager.getTask(task1.name)


        assertEquals(task1.name, task2?.name)
        assertEquals(task1.income, task2?.income)
        assertEquals(task1.xp, task2?.xp)
        assertEquals(task1.actions[0], task2?.actions?.get(0))
        assertEquals(task1.actions.size, task2?.actions?.size)
        assertEquals(task1.description, task2?.description)

        assertEquals(null, taskManager.getTask("FOOBAR"))
    }
}