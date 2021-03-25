package com.github.recraftedcivilizations.darkcitizens.election

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import net.milkbowl.vault.economy.Economy
import org.bukkit.ChatColor
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

/**
 * @author DarkVanityOfLight
 */

fun <T> MutableMap<T, Int>.inc(key: T, more: Int = 1) = merge(key, more, Int::plus)

abstract class GenericElection(
    override val electTime: Int,
    override val candidates: MutableSet<DPlayer>,
    override val votes: MutableMap<UUID, Int>,
    override val hasVoted: MutableSet<UUID>,
    override val job: IJob,
    override val voteFee: Int,
    override val candidateFee: Int,
    val dPlayerManager: DPlayerManager,
    val economy: Economy,
    private val bukkitWrapper: BukkitWrapper

) : IElect, BukkitRunnable(), Listener {

    override fun evaluateVotes(): DPlayer {
        val sorted = votes.toList().sortedByDescending { (_, value) -> value }.toMap()
        val winnerUUID = sorted.entries.first().key

        // TODO("Decide what to do if they have the same number of votes")
        return dPlayerManager.getDPlayer(winnerUUID)!!
    }

    override fun addVote(uuid: UUID) { 
        votes.inc(uuid)
    }

    override fun vote(uuid: UUID, dPlayer: DPlayer) {
        val player = bukkitWrapper.getPlayer(dPlayer.uuid)!!

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
            }
        }else{
            player.sendMessage("${ChatColor.RED}The candidate you want to vote for does not exist!!")
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

        return if(job.canJoin(dPlayer)){
            if (economy.has(player, candidateFee.toDouble())){
                true
            }else{
                player.sendMessage("${ChatColor.RED}You don't have enough money to run for this job")
                false
            }
        }else{
            false
        }
    }

    override fun run() {
        val winner = evaluateVotes()
        val winnerPlayer = bukkitWrapper.getPlayer(winner.uuid)
        winnerPlayer?.sendMessage("Congratulations you won the election")
        job.join(winner)
        bukkitWrapper.notify("${winnerPlayer!!.name} won the election and is now a ${job.name}", BarColor.YELLOW, BarStyle.SEGMENTED_10, 5, bukkitWrapper.getOnlinePlayers())

    }

    @EventHandler
    open fun onLeave(e: PlayerQuitEvent){
        val dPlayer = dPlayerManager.getDPlayer(e.player)
        candidates.remove(dPlayer)

    }
}