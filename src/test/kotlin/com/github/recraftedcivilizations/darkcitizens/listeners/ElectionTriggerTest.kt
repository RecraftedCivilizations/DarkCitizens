package com.github.recraftedcivilizations.darkcitizens.listeners

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.election.ElectionManager
import com.github.recraftedcivilizations.darkcitizens.events.JobLeaveEvent
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.jobs.elected.GenericElectedJob
import com.github.recraftedcivilizations.jobs.randomString
import com.nhaarman.mockitokotlin2.*
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

internal class ElectionTriggerTest {
    private var jobName = randomString()
    private var jobMock = mock<IJob> {
        on { name } doReturn jobName
    }
    private var playerMock = mock<Player>{}
    private var dPlayerMock = mock<DPlayer>{
        on { job } doReturn jobName
    }
    private var dPlayerManager = mock<DPlayerManager>{
        on { getDPlayer(playerMock) } doReturn dPlayerMock
    }
    private var jobManager = mock<JobManager>{
        on { getJob(jobName) } doReturn jobMock
    }
    private var event = mock<PlayerQuitEvent>{
        on {player} doReturn playerMock
    }

    private var bukkitWrapper = mock<BukkitWrapper>{
        on { getOnlinePlayers() } doReturn setOf(playerMock)
    }

    private var electionManager = mock<ElectionManager>()

    @AfterEach
    fun refresh(){
        jobName = randomString()
        jobMock = mock<IJob> {
            on { name } doReturn jobName
        }
        playerMock = mock<Player>{}
        dPlayerMock = mock<DPlayer>{
            on { job } doReturn jobName
        }
        dPlayerManager = mock<DPlayerManager>{
            on { getDPlayer(playerMock) } doReturn dPlayerMock
        }
        jobManager = mock<JobManager>{
            on { getJob(jobName) } doReturn jobMock
        }
        event = mock<PlayerQuitEvent>{
            on {player} doReturn playerMock
        }

        bukkitWrapper = mock<BukkitWrapper>{}
        electionManager = mock<ElectionManager>()
    }

    @Test
    fun onDeathNoJob() {
        dPlayerMock = mock<DPlayer>{
            on { job } doReturn null
        }

        whenever(dPlayerManager.getDPlayer(playerMock)) doReturn dPlayerMock

        val e = PlayerDeathEvent(playerMock, emptyList(), 0, null)

        val eListener = ElectionTrigger(dPlayerManager, jobManager, electionManager)

        eListener.onDeath(e)

        verify(dPlayerManager).getDPlayer(playerMock)
        verify(jobManager).getJob(null)
        verify(dPlayerMock).job
        verifyNoMoreInteractions(jobMock, jobManager, dPlayerMock, dPlayerManager, electionManager)

    }

    @Test
    fun onDeathWithJob() {
        whenever(dPlayerMock.job) doReturn jobName
        whenever(jobMock.leaveOnDeath) doReturn true

        val e = PlayerDeathEvent(playerMock, emptyList(), 0, null)

        val eListener = ElectionTrigger(dPlayerManager, jobManager, electionManager)

        eListener.onDeath(e)

        verify(dPlayerManager).getDPlayer(playerMock)
        verify(jobManager).getJob(jobName)
        verify(jobMock).leaveOnDeath
        verify(jobMock).leave(dPlayerMock)
        verify(dPlayerMock).job
        verifyNoMoreInteractions(jobMock, jobManager, dPlayerMock, dPlayerManager, electionManager)

    }

    @Test
    fun onJobLeave() {

        val jobMock = mock<GenericElectedJob>{}

        val e = JobLeaveEvent(dPlayerMock, jobMock)

        val eListener = ElectionTrigger(dPlayerManager, jobManager, electionManager)

        eListener.onJobLeave(e)
        verify(electionManager).createElection(jobMock)
        verifyNoMoreInteractions(jobMock, jobManager, dPlayerMock, dPlayerManager, electionManager)

    }
}