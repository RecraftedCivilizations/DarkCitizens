package com.github.recraftedcivilizations.darkcitizens.listeners

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.groups.Group
import com.github.recraftedcivilizations.darkcitizens.groups.GroupManager
import com.github.recraftedcivilizations.darkcitizens.jobs.Job
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.bukkit.entity.Arrow
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.util.*

internal class FriendlyFireTest {

    private val uuid1 = UUID.randomUUID()
    private val uuid2 = UUID.randomUUID()

    private var job1 = mock<Job>{
        on { group } doReturn "Foo"
    }

    private var job2 = mock<Job>{
        on { group } doReturn "Bar"
    }

    private var group1 = mock<Group> {
        on { friendlyFire } doReturn false
    }

    private var group2 = mock<Group> {
        on { friendlyFire } doReturn true
    }

    private var dPlayerMock1 = mock<DPlayer>{}
    private var dPlayerMock2 = mock<DPlayer> {}

    private var dPlayerManager = mock<DPlayerManager>{
        on { getDPlayer(uuid1) } doReturn dPlayerMock1
        on { getDPlayer(uuid2) } doReturn dPlayerMock2
    }
    private var jobManager = mock<JobManager>{
        on { getJob("job1") } doReturn job1
        on { getJob("job2") } doReturn job2
    }
    private var groupManager = mock<GroupManager>{
        on { getGroup("Foo") } doReturn group1
        on { getGroup("Bar") } doReturn group2
    }
    private var playerMock1 = mock<Player>{
        on { uniqueId } doReturn uuid1
    }
    private var playerMock2 = mock<Player>{
        on { uniqueId } doReturn uuid2
    }


    @Test
    fun onFriendlyFire() {

        whenever(dPlayerMock1.job) doReturn "job1"
        whenever(dPlayerMock2.job) doReturn "job1"

        val eListener = FriendlyFire(dPlayerManager, jobManager, groupManager)

        val e = EntityDamageByEntityEvent(playerMock1, playerMock1, EntityDamageEvent.DamageCause.ENTITY_ATTACK, 1.0)

        eListener.onDamage(e)

        assertEquals(true, e.isCancelled)
    }

    @Test
    fun onFriendlyFireThroughProjectile(){
        whenever(dPlayerMock1.job) doReturn "job1"
        whenever(dPlayerMock2.job) doReturn "job1"

        val eListener = FriendlyFire(dPlayerManager, jobManager, groupManager)


        val attacker = mock<Arrow> {
            on { shooter } doReturn playerMock1
        }
        val e = EntityDamageByEntityEvent(playerMock1, playerMock1, EntityDamageEvent.DamageCause.ENTITY_ATTACK, 1.0)

        eListener.onDamage(e)

        assertEquals(true, e.isCancelled)
    }

    @Test
    fun noFriendlyFire(){

        whenever(dPlayerMock1.job) doReturn "job1"
        whenever(dPlayerMock2.job) doReturn ""

        val eListener = FriendlyFire(dPlayerManager, jobManager, groupManager)


        val attacker = mock<Arrow> {
            on { shooter } doReturn playerMock1
        }
        val e = EntityDamageByEntityEvent(attacker, playerMock1, EntityDamageEvent.DamageCause.ENTITY_ATTACK, 1.0)

        eListener.onDamage(e)

        assertEquals(false, e.isCancelled)

    }

    @Test
    fun damageFromOther(){
        whenever(dPlayerMock1.job) doReturn "job1"
        whenever(dPlayerMock2.job) doReturn ""

        val eListener = FriendlyFire(dPlayerManager, jobManager, groupManager)


        val attacker = mock<Entity> {}
        val e = EntityDamageByEntityEvent(attacker, playerMock1, EntityDamageEvent.DamageCause.ENTITY_ATTACK, 1.0)

        eListener.onDamage(e)

        assertEquals(false, e.isCancelled)
    }
}