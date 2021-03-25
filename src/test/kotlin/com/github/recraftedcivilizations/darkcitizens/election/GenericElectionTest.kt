package com.github.recraftedcivilizations.darkcitizens.election

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import com.github.recraftedcivilizations.darkcitizens.parser.dataparser.IParseData
import com.github.recraftedcivilizations.jobs.randomString
import com.nhaarman.mockitokotlin2.*
import net.milkbowl.vault.economy.Economy
import org.bukkit.ChatColor
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.util.*
import kotlin.random.Random

private class ElectionStub(
    job: IJob,
    voteFee: Int,
    candidateFee: Int,
    dPlayerManager: DPlayerManager,
    economy: Economy,
    electionManager: ElectionManager,
    bukkitWrapper: BukkitWrapper,
    candidates: MutableSet<DPlayer> = emptySet<DPlayer>().toMutableSet(),
    votes: MutableMap<UUID, Int> = emptyMap<UUID, Int>().toMutableMap(),
    hasVoted: MutableSet<UUID> = emptySet<UUID>().toMutableSet(),
) : GenericElection(candidates, votes, hasVoted, job, voteFee, candidateFee, dPlayerManager, economy, electionManager, bukkitWrapper){}

internal class GenericElectionTest {
    private var dataParser = mock<IParseData>{}
    private var dPlayerManager = DPlayerManager(dataParser)
    private var bukkitWrapper = mock<BukkitWrapper>()
    private var electionManager = mock<ElectionManager>()

    private var jobName = ""

    private var jobMock = mock<IJob>{}
    private var economy = mock<Economy>{}

    private val uuid1 = UUID.randomUUID()
    private val uuid2 = UUID.randomUUID()
    private val uuid3 = UUID.randomUUID()

    private val playerMock1 =
        mock<Player> {
            on { uniqueId } doReturn uuid1
            on { hasPermission("drp.job.join.Foo") } doReturn true
        }
    private val playerMock2 =
        mock<Player> {
            on { uniqueId } doReturn uuid2;
            on { hasPermission("drp.job.join.Foo") } doReturn false
        }

    private val playerMock3 =
        mock<Player> {
            on { uniqueId } doReturn uuid3
            on { hasPermission("drp.job.join.Foo") } doReturn true
        }

    private val dPlayerMock1 = mock<DPlayer>{
        on { uuid } doReturn uuid1
    }
    private val dPlayerMock2 = mock<DPlayer>{
        on { uuid } doReturn uuid2
    }
    private val dPlayerMock3 = mock<DPlayer>{
        on { uuid } doReturn uuid3
    }

    private fun mockFunc(){

        whenever(dataParser.getDPlayer(any())).doAnswer {
            when (it.getArgument<UUID>(0)) {
                uuid1 -> dPlayerMock1
                uuid2 -> dPlayerMock2
                uuid3 -> dPlayerMock3
                else -> null
            }
        }

        whenever(bukkitWrapper.getPlayer(any<UUID>())).doAnswer {
            when (it.getArgument<UUID>(0)) {
                uuid1 -> playerMock1
                uuid2 -> playerMock2
                uuid3 -> playerMock3
                else -> null
            }
        }

        whenever(bukkitWrapper.getPlayer(any<DPlayer>())).doAnswer {
            val arg = it.getArgument<DPlayer>(0)
            when (arg.uuid) {
                uuid1 -> playerMock1
                uuid2 -> playerMock2
                uuid3 -> playerMock3
                else -> {
                    return@doAnswer null
                }
            }
        }

        whenever(jobMock.name) doReturn jobName
    }

    fun randomElectionArgs(): Map<Any, Any>{
        return mapOf(
            Pair("electTime", Random.nextInt(100)),
            Pair("voteFee", Random.nextInt(100)),
            Pair("candidateFee", Random.nextInt(100)),
        )
    }

    private fun createElection(args: Map<Any, Any>): ElectionStub{
        return ElectionStub(jobMock, args["voteFee"] as Int, args["candidateFee"] as Int, dPlayerManager, economy, electionManager, bukkitWrapper)
    }

    @BeforeEach
    fun refreshMocks(){
        dataParser = mock {}
        dPlayerManager = DPlayerManager(dataParser)
        bukkitWrapper = mock{}
        jobMock = mock{}
        economy = mock{}
        electionManager = mock<ElectionManager>()
        jobName = randomString()


        mockFunc()
    }

// <---------------------The actual testing starts here--------------------->

    @Test
    fun addVote() {
        val voteFor = UUID.randomUUID()
        val args = randomElectionArgs()
        val election = createElection(args)
        election.addVote(voteFor)

        assertEquals(1, election.votes[voteFor])

        election.addVote(voteFor)
        assertEquals(2, election.votes[voteFor])
    }

    @Test
    fun canVote() {
        val args = randomElectionArgs()

        whenever(economy.has(playerMock1, (args["voteFee"] as Int).toDouble())) doReturn true
        whenever(economy.has(playerMock2, (args["voteFee"] as Int).toDouble())) doReturn false

        val election = createElection(args)
        assertEquals(true, election.canVote(dPlayerMock1))
        election.hasVoted.add(uuid1)

        assertEquals(false, election.canVote(dPlayerMock1))
        verify(playerMock1).sendMessage("${ChatColor.RED}You already voted!!")

        assertEquals(false, election.canVote(dPlayerMock2))
        verify(playerMock2).sendMessage("${ChatColor.RED}You don't have enough money to pay the fee of ${args["voteFee"]}!!")

    }

    @Test
    fun vote() {
        val args = randomElectionArgs()
        val ranUUID = UUID.randomUUID()

        whenever(economy.has(playerMock1, (args["voteFee"] as Int).toDouble())) doReturn true
        whenever(economy.has(playerMock2, (args["voteFee"] as Int).toDouble())) doReturn false

        val election = createElection(args)

        election.vote(ranUUID, dPlayerMock1)
        verify(playerMock1).sendMessage("${ChatColor.RED}The candidate you want to vote for does not exist!!")

        election.candidates.add(dPlayerMock3)

        election.vote(uuid3, dPlayerMock1)
        assertEquals(1, election.votes[uuid3])
        verify(economy).withdrawPlayer(playerMock1, (args["voteFee"] as Int).toDouble())

        election.vote(uuid3, dPlayerMock2)
        assertEquals(1, election.votes[uuid3])


    }

    @Test
    fun evaluateVotes() {
        val args = randomElectionArgs()

        val election = createElection(args)
        election.votes[dPlayerMock1.uuid] = 2
        election.votes[dPlayerMock2.uuid] = 1
        election.votes[dPlayerMock3.uuid] = 10

        assertEquals(dPlayerMock3, election.evaluateVotes())
    }

    @Test
    fun addCandidate() {
        val args = randomElectionArgs()
        val election = createElection(args)

        election.addCandidate(dPlayerMock1)
        assertEquals(1, election.candidates.size)
        assertEquals(dPlayerMock1, election.candidates.first())
    }

    @Test
    fun canCandidate() {
        val args = randomElectionArgs()

        whenever(jobMock.canJoin(dPlayerMock1)) doReturn true
        whenever(economy.has(playerMock1, (args["candidateFee"] as Int).toDouble())) doReturn true

        whenever(jobMock.canJoin(dPlayerMock2)) doReturn false

        whenever(jobMock.canJoin(dPlayerMock3)) doReturn true
        whenever(economy.has(playerMock3, (args["candidateFee"] as Int).toDouble())) doReturn false

        val election = createElection(args)


        assertEquals(true, election.canCandidate(dPlayerMock1))
        assertEquals(false, election.canCandidate(dPlayerMock2))
        assertEquals(false, election.canCandidate(dPlayerMock3))
        verify(playerMock3, times(1)).sendMessage("${ChatColor.RED}You don't have enough money to run for this job")

    }

    @Test
    fun runFor() {
        val args = randomElectionArgs()

        whenever(jobMock.canJoin(dPlayerMock1)) doReturn true
        whenever(economy.has(playerMock1, (args["candidateFee"] as Int).toDouble())) doReturn true

        whenever(jobMock.canJoin(dPlayerMock2)) doReturn false

        val election = createElection(args)

        election.runFor(dPlayerMock1)

        assertEquals(1, election.candidates.size)
        assertEquals(dPlayerMock1, election.candidates.first())
        verify(economy).withdrawPlayer(playerMock1, ((args["candidateFee"] as Int).toDouble()))
        verify(playerMock1).sendMessage("${ChatColor.GREEN}You are now a candidate for the job ${jobMock.name}")

        election.runFor(dPlayerMock2)
        assertEquals(1, election.candidates.size)
        verifyNoMoreInteractions(playerMock2)

    }

    @Test
    fun run() {
        val playerSet = emptySet<Player>()
        val name = randomString()
        whenever(playerMock1.name) doReturn name
        whenever(bukkitWrapper.getOnlinePlayers()) doReturn playerSet

        val args = randomElectionArgs()
        val election = createElection(args)

        election.votes[dPlayerMock1.uuid] = 2
        election.votes[dPlayerMock2.uuid] = 1
        election.votes[dPlayerMock3.uuid] = 10

        election.run()

        verify(playerMock3).sendMessage("Congratulations you won the election")
        verify(jobMock).join(dPlayerMock3)
        verify(bukkitWrapper).notify("${playerMock3.name} won the election and is now a ${jobMock.name}", BarColor.YELLOW, BarStyle.SEGMENTED_10, 5, playerSet)
        verify(electionManager).electionEnded(election)

    }
}