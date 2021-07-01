package com.github.recraftedcivilizations.darkcitizens.runnables

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.groups.Group
import com.github.recraftedcivilizations.darkcitizens.groups.GroupManager
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.laws.LawManager
import com.github.recraftedcivilizations.darkcitizens.parser.dataparser.IParseData
import com.github.recraftedcivilizations.jobs.randomString
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import net.milkbowl.vault.economy.Economy
import org.bukkit.entity.Player
import org.junit.jupiter.api.Test

import java.util.*
import kotlin.random.Random

internal class BaseIncomeRunnerTest {

    private var economy = mock<Economy>{}
    private val lawManager = mock<LawManager>{}

    private val uuid1 = UUID.randomUUID()

    private val playerMock1 =
        mock<Player> {
            on { uniqueId } doReturn uuid1
            on { hasPermission("drp.job.join.Foo") } doReturn true
        }

    private val jobName = randomString()
    private val groupName = randomString()
    private val baseIncome = Random.nextInt()
    private val baseXp = Random.nextInt()

    private val jobMock = mock<IJob>{
        on { group } doReturn groupName
        on { baseIncome } doReturn baseIncome
        on { baseXPGain } doReturn baseXp
    }

    private val groupMock = mock<Group>{}

    private val dPlayerMock1 = mock<DPlayer>{
        on { job } doReturn jobName
        on { uuid } doReturn uuid1
    }

    private var dataParser = mock<IParseData>{}
    private var dPlayerManager = mock<DPlayerManager>()
    private var jobManager: JobManager = mock{}
    private var groupManager: GroupManager = mock<GroupManager>{}
    private var bukkitWrapper = mock<BukkitWrapper>()

    init {
        whenever(jobManager.getJobs()) doReturn setOf(jobMock)
        whenever(jobMock.currentMembers) doReturn setOf(dPlayerMock1)
        whenever(groupManager.getGroup(groupName)) doReturn groupMock
        whenever(bukkitWrapper.getPlayer(uuid1)) doReturn playerMock1
    }

    @Test
    fun run() {
        val runner = BaseIncomeRunner(jobManager, dPlayerManager, economy, groupManager, lawManager, bukkitWrapper)
        runner.run()

        verify(dPlayerMock1).addXP(groupMock, baseXp)
        verify(economy).depositPlayer(playerMock1, baseIncome.toDouble())
        verify(dPlayerManager).setDPlayer(dPlayerMock1)

    }
}