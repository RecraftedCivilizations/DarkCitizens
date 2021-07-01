package com.github.recraftedcivilizations.darkcitizens.election

import com.github.darkvanityoflight.recraftedcore.ARecraftedPlugin
import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList.unregisterAll
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

/**
 * @author DarkVanityOfLight
 */

fun <T> MutableMap<T, Int>.inc(key: T, more: Int = 1) = merge(key, more, Int::plus)


abstract class GenericElection(
    override val candidates: MutableSet<DPlayer>,
    override val votes: MutableMap<UUID, Int>,
    override val hasVoted: MutableSet<UUID>,
    override val job: IJob,
    override val voteFee: Int,
    override val candidateFee: Int,
    private val candidateTime: Int,
    private val voteTime: Int,
    val dPlayerManager: DPlayerManager,
    val economy: Economy,
    private val electionManager: ElectionManager,
    private val plugin: ARecraftedPlugin,
    private val bukkitWrapper: BukkitWrapper = BukkitWrapper()

) : IElect, BukkitRunnable(), Listener {
    override var state: ElectionStates = ElectionStates.CANDIDATE
    private val runTime = 0

    override fun evaluateVotes(): DPlayer? {
        if (candidates.isEmpty()) return null
        return if(votes.isNotEmpty()){
            val sorted = votes.toList().sortedByDescending { (_, value) -> value }.toMap()
            val winnerUUID = sorted.entries.first().key
            dPlayerManager.getDPlayer(winnerUUID)!!
        }else{
            candidates.first()
        }

        // TODO("Decide what to do if they have the same number of votes")
    }

    override fun addVote(uuid: UUID) { 
        votes.inc(uuid)
    }

    override fun vote(uuid: UUID, dPlayer: DPlayer) {
        val player = bukkitWrapper.getPlayer(dPlayer.uuid)!!

        if (state == ElectionStates.VOTE){

            var isInCandidates: Boolean = false
            for(candidate in candidates){
                if (uuid == candidate.uuid){
                    isInCandidates = true
                    break
                }
            }

            if (isInCandidates){
                if(canVote(dPlayer)){
                    addVote(uuid)
                    economy.withdrawPlayer(player, voteFee.toDouble())
                    player.sendMessage("You successfully voted")
                }
            }else{
                player.sendMessage("${ChatColor.RED}The candidate you want to vote for does not exist!!")
            }
        }else{
            player.sendMessage("${ChatColor.RED} The vote phase hasn't started yet!!")
        }
    }

    override fun canVote(dPlayer: DPlayer): Boolean {
        val player = bukkitWrapper.getPlayer(dPlayer.uuid)!!
        return if( economy.has(player, voteFee.toDouble())){
            if (!hasVoted.contains(dPlayer.uuid)){
                true
            }else{
                player.sendMessage("${ChatColor.RED}You already voted!!")
                false
            }
        }else{
            player.sendMessage("${ChatColor.RED}You don't have enough money to pay the fee of $voteFee!!")
            false
        }
    }

    override fun addCandidate(dPlayer: DPlayer) {
        candidates.add(dPlayer)
    }

    override fun runFor(dPlayer: DPlayer) {
        val player = bukkitWrapper.getPlayer(dPlayer.uuid)!!
        if(canCandidate(dPlayer)){
            addCandidate(dPlayer)
            economy.withdrawPlayer(player, candidateFee.toDouble())
            player.sendMessage("${ChatColor.GREEN}You are now a candidate for the job ${job.name}")
        }
    }

    override fun canCandidate(dPlayer: DPlayer): Boolean {
        val player = bukkitWrapper.getPlayer(dPlayer.uuid)!!
        bukkitWrapper.info("[DEBUG] candidates are" )
        return if(!isCandidate(dPlayer)){
             if(job.canJoin(dPlayer)){
                if (economy.has(player, candidateFee.toDouble())){
                    true
                }else{
                    player.sendMessage("${ChatColor.RED}You don't have enough money to run for this job")
                    false
                }
            }else{
                false
            }
        }else{
            player.sendMessage("${ChatColor.RED}You already are a candidate")
            false
        }
    }

    override fun run() {

        if (state == ElectionStates.CANDIDATE){
            this.state = ElectionStates.VOTE
            Bukkit.getScheduler().runTaskLater(plugin, this as Runnable, voteTime * 20L * 60)
            bukkitWrapper.notify("You can now vote for a candidate in the election for ${job.name}", BarColor.RED, BarStyle.SEGMENTED_20, 5, bukkitWrapper.getOnlinePlayers())

        } else if (state == ElectionStates.VOTE){// Only evaluate after the Vote phase
            // Get the winner and put him into his job
            val winner = evaluateVotes()
            if (winner != null){
                val winnerPlayer = bukkitWrapper.getPlayer(winner.uuid)
                winnerPlayer?.sendMessage("Congratulations you won the election")
                job.join(winner)
                bukkitWrapper.notify("${winnerPlayer!!.name} won the election and is now a ${job.name}", BarColor.YELLOW, BarStyle.SEGMENTED_10, 5, bukkitWrapper.getOnlinePlayers())
            }
            this.state = ElectionStates.ENDED
            electionManager.electionEnded(this)
        }
    }

    override fun start() {
        this.runTaskLater(plugin, candidateTime * 20L * 60)
    }

    fun isCandidate(dPlayer: DPlayer): Boolean{
        for (candidate in candidates){
            if (candidate.uuid == dPlayer.uuid){
                return true
            }
        }
        return false
    }

    fun isCandidate(player: Player): Boolean {
        for (candidate in candidates){
            if(candidate.uuid == player.uniqueId){
                return true
            }
        }
        return false
    }

    @EventHandler
    open fun onLeave(e: PlayerQuitEvent){
        val dPlayer = dPlayerManager.getDPlayer(e.player)
        candidates.remove(dPlayer)
        if (this.state == ElectionStates.ENDED){
            unregisterAll(this)
        }
    }
}