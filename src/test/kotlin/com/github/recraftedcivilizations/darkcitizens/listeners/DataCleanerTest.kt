package com.github.recraftedcivilizations.darkcitizens.listeners

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
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class DataCleanerTest {
    private val jobName = randomString()
    private val jobMock = mock<IJob> {
        on { name } doReturn jobName
    }
    private val playerMock = mock<Player>{}
    private val dPlayerMock = mock<DPlayer>{
        on { job } doReturn jobName
    }
    private val dPlayerManager = mock<DPlayerManager>{
        on { getDPlayer(playerMock) } doReturn dPlayerMock
    }
    private val jobManager = mock<JobManager>{
        on { getJob(jobName) } doReturn jobMock
    }
    private val event = mock<PlayerQuitEvent>{
        on {player} doReturn playerMock
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
}