package com.github.recraftedcivilizations.tasks

import com.github.recraftedcivilizations.darkcitizens.actions.ActionManager
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.groups.GroupManager
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.laws.LawManager
import com.github.recraftedcivilizations.darkcitizens.parser.dataparser.IParseData
import com.github.recraftedcivilizations.darkcitizens.tasks.ITask
import com.github.recraftedcivilizations.darkcitizens.tasks.Task
import com.github.recraftedcivilizations.darkcitizens.tasks.TaskManager
import com.github.recraftedcivilizations.darkcitizens.actions.Actions
import com.github.recraftedcivilizations.darkcitizens.actions.IAction
import com.github.recraftedcivilizations.jobs.randomString
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import net.milkbowl.vault.economy.Economy
import org.bukkit.Material
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random

internal class TaskManagerTest {

    val lawManager = mock<LawManager>{}
    val econ = mock<Economy>{}
    val dataParser = mock<IParseData>{}
    val dPlayerManager = DPlayerManager(dataParser)
    val action = mock<IAction>{}
    val jobManager = JobManager(dPlayerManager, lawManager)
    val groupManager = GroupManager()
    val icon = Material.PLAYER_HEAD

    val taskField = TaskManager::class.java.getDeclaredField("tasks")

    init {
        taskField.isAccessible =true
    }

    @Test
    fun shouldConstruct(){
        val taskManager = TaskManager(econ, dPlayerManager, groupManager)
    }

    @Test
    fun createTask() {
        val taskManager = TaskManager(econ, dPlayerManager, groupManager)
        val task1 = Task(randomString(), Random.nextInt(), Random.nextInt(), listOf(Actions.DEBUG), randomString(), icon, dPlayerManager, econ, jobManager, groupManager)

        taskManager.setJobManager(jobManager)
        jobManager.setTaskManager(taskManager)

        taskManager.createTask(task1.name, task1.income, task1.xp, listOf("DEBUG"), task1.description, icon)

        val tasks = taskField.get(taskManager) as Set<*>

        assertEquals(1, tasks.size)
        val task2 = tasks.iterator().next() as ITask

        assertEquals(task1.name, task2.name)
        assertEquals(task1.income, task2.income)
        assertEquals(task1.xp, task2.xp)
        assertEquals(task1.actions[0], task2.actions[0])
        assertEquals(task1.actions.size, task2.actions.size)
        assertEquals(task1.description, task2.description)
        assertEquals(task1.icon, task2.icon)

    }

    @Test
    fun createCustomActionTask(){
        val action = mock<IAction>{
            on { name } doReturn "Foo"
        }
        ActionManager.registerAction(action)

        val taskManager = TaskManager(econ, dPlayerManager, groupManager)
        taskManager.setJobManager(jobManager)

        val taskArgs = randomTaskArgs()
        val task = Task(taskArgs["name"] as String, taskArgs["income"] as Int, taskArgs["xp"] as Int, listOf(action), taskArgs["description"] as String, icon, dPlayerManager, econ, jobManager, groupManager)


        taskManager.createTask(task.name, task.income, task.xp, listOf("Foo"), task.description, icon)

        assertTask(taskManager.getTask(taskArgs["name"] as String)!!, taskArgs)
        assert(task.actions == taskManager.getTask(taskArgs["name"] as String)!!.actions)
    }

    @Test
    fun tryCreateTaskWithNonExistingAction(){
        val action = mock<IAction>{
            on { name } doReturn "Foo"
        }
        ActionManager.registerAction(action)

        val taskManager = TaskManager(econ, dPlayerManager, groupManager)
        taskManager.setJobManager(jobManager)

        val taskArgs = randomTaskArgs()

        taskManager.createTask(taskArgs["name"] as String, taskArgs["income"] as Int, taskArgs["xp"] as Int, listOf("bar"), taskArgs["description"] as String, icon)

        assertEquals(null, taskManager.getTask(taskArgs["name"] as String))
    }

    @Test
    fun getTask() {
        val taskManager = TaskManager(econ, dPlayerManager, groupManager)

        taskManager.setJobManager(jobManager)
        jobManager.setTaskManager(taskManager)

        val task1 = Task(randomString(), Random.nextInt(), Random.nextInt(), listOf(Actions.DEBUG), randomString(), icon, dPlayerManager, econ, jobManager, groupManager)
        taskManager.createTask(task1.name, task1.income, task1.xp, listOf("DEBUG"), task1.description, icon)
        val task2 = taskManager.getTask(task1.name)


        assertEquals(task1.name, task2?.name)
        assertEquals(task1.income, task2?.income)
        assertEquals(task1.xp, task2?.xp)
        assertEquals(task1.actions[0], task2?.actions?.get(0))
        assertEquals(task1.actions.size, task2?.actions?.size)
        assertEquals(task1.description, task2?.description)
        assertEquals(task1.icon, task2?.icon)

        assertEquals(null, taskManager.getTask("FOOBAR"))
    }
}