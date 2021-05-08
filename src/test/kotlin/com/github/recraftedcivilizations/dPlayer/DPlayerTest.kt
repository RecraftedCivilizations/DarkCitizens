package com.github.recraftedcivilizations.dPlayer

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerData1
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerData2
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.groups.Group
import com.github.recraftedcivilizations.darkcitizens.jobs.Job
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.laws.LawManager
import com.github.recraftedcivilizations.darkcitizens.parser.dataparser.IParseData
import com.github.recraftedcivilizations.darkcitizens.tasks.ITask
import com.github.recraftedcivilizations.darkcitizens.tasks.Task
import com.github.recraftedcivilizations.darkcitizens.tasks.TaskManager
import com.github.recraftedcivilizations.jobs.randomString
import com.nhaarman.mockitokotlin2.*
import net.milkbowl.vault.economy.Economy
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.plugin.PluginManager
import org.junit.Ignore
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.random.Random

internal class DPlayerTest {
    val lawManager = mock<LawManager>{}

    @Test
    fun serializeData() {
        val uuid = UUID(23423483952389, 3909432134)
        val dPlayerData = DPlayerData1(
            uuid,
            null,
            true,
            true,
            mapOf(Pair("Foo", 3), Pair("", 10)),
            mapOf(Pair("Foo", 3), Pair("", 10))
        )
        val dPlayer = DPlayer(dPlayerData)

        assertEquals(dPlayerData, dPlayer.serializeData())
    }

    @Disabled("Depreceated")
    @Test
    fun joinJob() {
        assertEquals(0, 1)
        val icon = mock<Material>{}
        // Player Stuff
        val uuid = UUID.randomUUID()
        val playerMock = mock<Player>{
            on {uniqueId} doReturn uuid
        }

        whenever(playerMock.sendMessage(any<String>())).doAnswer {
            println(it.getArgument(0) as String)
        }

        val dPlayerData = DPlayerData1(
                uuid,
                null,
                true,
                true,
                emptyMap(),
                emptyMap()
        )
        val dPlayer = DPlayer(dPlayerData)

        val dataParser = mock<IParseData>{
            on {getDPlayer(any())} doReturn dPlayer
        }
        val dPlayerManager = DPlayerManager(dataParser)

        // Bukkit wrapper stuff
        val bukkitWrapper = mock<BukkitWrapper>{
            on { getPlayer(dPlayer) } doReturn playerMock
        }

        // Job stuff
        val jobManager = JobManager(dPlayerManager, lawManager)
        val job = Job(randomString(), randomString(), Random.nextInt(10), emptySet(), emptySet(), Random.nextInt(), Random.nextInt(), 0, false, icon, Random.nextBoolean(), dPlayerManager, jobManager, bukkitWrapper)

        dPlayer.setJobManager(jobManager)
        dPlayer.setBukkitWrapper(bukkitWrapper)
        dPlayer.joinJob(job)
        assertEquals(job.name, dPlayer.job)
    }

    @Test
    fun addXP() {
        val pluginManager = mock<PluginManager>{}
        val bukkitWrapper = mock<BukkitWrapper>{
            on { getPluginManager() } doReturn pluginManager
        }
        val uuid = UUID(23423483952389, 3909432134)
        val dPlayerData = DPlayerData1(
            uuid,
            null,
            true,
            true,
            mapOf(Pair("Foo", 3), Pair("", 10)),
            mapOf(Pair("Foo", 3), Pair("", 10))
        )
        val dPlayer = DPlayer(dPlayerData)
        dPlayer.setBukkitWrapper(bukkitWrapper)

        val group1 = Group("Foo", 10, listOf(100), false, false, "")

        dPlayer.addXP(group1, 10)

        assertEquals(13, dPlayer.groupXps["Foo"])

        val group2 = Group("Bar", 10, listOf(100), false, false, "")
        dPlayer.addXP(group2, 101)

        assertEquals(1, dPlayer.groupLvls["Bar"])
        verify(pluginManager, times(3)).callEvent(any())

    }

    @Test
    fun addXpToNonExistingGroup() {

        val pluginManager = mock<PluginManager>{}
        val bukkitWrapper = mock<BukkitWrapper>{
            on { getPluginManager() } doReturn pluginManager
        }

        val uuid = UUID(23423483952389, 3909432134)
        val dPlayerData = DPlayerData1(
            uuid,
            null,
            true,
            true,
            mapOf(Pair("Foo", 3), Pair("", 10)),
            mapOf(Pair("Foo", 3), Pair("", 10))
        )

        val group = Group("FooBar", 5, emptyList(), false, false, "")
        val dPlayer = DPlayer(dPlayerData)
        dPlayer.setBukkitWrapper(bukkitWrapper)

        dPlayer.addXP(group, 10)

        assertEquals(10, dPlayer.groupXps["FooBar"])
        verify(pluginManager, times(1)).callEvent(any())
    }

    @Test
    fun shouldConstruct() {
        val uuid = UUID.randomUUID()

        // With DPlayerData1
        var dPlayerData: Any = DPlayerData1(
            uuid,
            null,
            true,
            true,
            mapOf(Pair("Foo", 3), Pair("", 10)),
            mapOf(Pair("Foo", 3), Pair("", 10))
        )
        var dPlayer = DPlayer(dPlayerData as DPlayerData1)
        assertEquals(dPlayerData.uuid, dPlayer.uuid)
        assertEquals(dPlayerData.job, dPlayer.job)
        assertEquals(dPlayerData.wanted, dPlayer.wanted)
        assertEquals(dPlayerData.isCriminal, dPlayer.isCriminal)
        assertEquals(dPlayerData.groupLvls, dPlayer.groupLvls)
        assertEquals(dPlayer.groupXps, dPlayer.groupXps)

        // With DPlayerData2
        val player: Player = mock { on { uniqueId } doReturn uuid }
        dPlayerData =
            DPlayerData2(null, true, true, mapOf(Pair("Foo", 3), Pair("", 10)), mapOf(Pair("Foo", 3), Pair("", 10)))
        dPlayer = DPlayer(dPlayerData, player)
        assertEquals(uuid, dPlayer.uuid)
        assertEquals(dPlayerData.job, dPlayer.job)
        assertEquals(dPlayerData.wanted, dPlayer.wanted)
        assertEquals(dPlayerData.isCriminal, dPlayer.isCriminal)
        assertEquals(dPlayerData.groupLvls, dPlayer.groupLvls)
        assertEquals(dPlayer.groupXps, dPlayer.groupXps)

        // With player
        dPlayer = DPlayer(player)
        assertEquals(uuid, dPlayer.uuid)
        assertEquals(false, dPlayer.wanted)
        assertEquals(false, dPlayer.isCriminal)
        assertEquals(emptyMap<String, Int>().toMutableMap(), dPlayer.groupLvls)
        assertEquals(emptyMap<String, Int>().toMutableMap(), dPlayer.groupXps)
    }
}