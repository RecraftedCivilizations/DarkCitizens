package com.github.recraftedcivilizations.darkcitizens.listeners

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.jobs.randomString
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerQuitEvent
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class DataCleanerTest {
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
    }

    @Test
    fun onQuit() {
        val dataCleaner = DataCleaner(dPlayerManager, jobManager)

        dataCleaner.onQuit(event)

        verify(dPlayerManager).getDPlayer(playerMock)
        verify(dPlayerMock).isCriminal = false
        verify(dPlayerMock).wanted = false
        verify(dPlayerManager).setDPlayer(dPlayerMock)
        verify(jobMock).leave(dPlayerMock)

    }

    @Test
    fun onShutdown() {
        val dataCleaner = DataCleaner(dPlayerManager, jobManager, bukkitWrapper)

        dataCleaner.onShutdown()
        verify(bukkitWrapper).getOnlinePlayers()
        verify(dPlayerManager).getDPlayer(playerMock)
        verify(dPlayerManager).getDPlayer(playerMock)

        verify(dPlayerMock).isCriminal = false
        verify(dPlayerMock).wanted = false
        verify(dPlayerManager).setDPlayer(dPlayerMock)
        verify(jobMock).leave(dPlayerMock)


    }
}