package com.github.recraftedcivilizations.jobs

import com.github.recraftedcivilizations.dPlayer.DPlayer
import com.github.recraftedcivilizations.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.tasks.ITask
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*

/**
 * @author DarkVanityOfLight
 */

/**
 * A wrapper for the getPlayer function from Bukkit
 * this is mainly for testing purposes
 */
open class BukkitWrapper{

    /**
     * Get a Bukkit player from a [DPlayer]
     * @param player The player to get the Bukkit player for
     */
    open fun getPlayer(player: DPlayer): Player? {
        return Bukkit.getPlayer(player.uuid)
    }

    /**
     * Get a Bukkit player from a UUID
     * @param uuid The UUID of the player to get
     */
    open fun getPlayer(uuid: UUID): Player? {
        return Bukkit.getPlayer(uuid)
    }
}

/**
 * The default implementation for a job,
 * @see IJob
 * @constructor Construct using a [name], a [group] the job belongs,
 * a [playerLimit] a [tasks] set consisting of [ITask], a [canDemote] set consisting
 * of other job names they can demote, a [baseIncome], a [baseXPGain], a [minLvl] to join the job, if
 * an election is required [electionRequired], if permissions are required to join the job([permissionRequired]) and a
 * [DPlayerManager]. The [bukkitWrapper] is only for testing purposes and should not be passed
 */
class Job(
    override val name: String,
    override val group: String,
    override val playerLimit: Int,
    override val tasks: Set<ITask>,
    override val canDemote: Set<String>,
    override val baseIncome: Int,
    override val baseXPGain: Int,
    override val minLvl: Int,
    override val electionRequired: Boolean,
    override val permissionRequired: Boolean,
    private val dPlayerManager: DPlayerManager,
    private val bukkitWrapper: BukkitWrapper = BukkitWrapper()
) : IJob {
    override val currentMembers: MutableSet<DPlayer> = emptySet<DPlayer>().toMutableSet()

    override fun removePlayer(player: DPlayer) {
        for (member in currentMembers){
            if (member.uuid == player.uuid){
                currentMembers.remove(member)
            }
        }
    }

    override fun removePlayer(player: Player) {
        removePlayer(dPlayerManager.getDPlayer(player)!!)
    }

    override fun addPlayer(player: DPlayer) {
        if (canJoin(player)) {
            currentMembers.add(player)
        }
    }

    override fun addPlayer(player: Player) {
        addPlayer(dPlayerManager.getDPlayer(player)!!)
    }

    override fun canJoin(player: DPlayer): Boolean {
        return canJoin(bukkitWrapper.getPlayer(player)!!)
    }

    override fun canJoin(player: Player): Boolean {
        val dPlayer = dPlayerManager.getDPlayer(player)

        if (dPlayer?.groupLvls?.get(group) == null){
            dPlayer?.groupLvls?.set(group, 0)
            dPlayer?.groupXps?.set(group, 0)
        }
        if (dPlayer!!.groupLvls[group]!! >= minLvl){
            if (!permissionRequired || permissionRequired && player.hasPermission("drp.job.join.$name"))
            {
                return if (currentMembers.size < playerLimit){
                    if (!isMember(player)){
                        true
                    }else{
                        player.sendMessage("${ChatColor.RED}You are already in this job")
                        false
                    }
                } else{
                    player.sendMessage("${ChatColor.RED}There are too many players in this job")
                    false
                }
            }else{
                player.sendMessage("${ChatColor.RED}You don't have the required permissions to join this job!")
                return false
            }
        }else{
            player.sendMessage("${ChatColor.RED}You don't have the required level to join this job!")
            return false
        }

    }

    override fun isMember(uuid: UUID): Boolean {
        for (member in currentMembers){
            if (member.uuid == uuid){
                return true
            }
        }
        return false
    }

    override fun isMember(player: DPlayer): Boolean {
        return isMember(player.uuid)
    }

    override fun isMember(player: Player): Boolean {
        return isMember(player.uniqueId)
    }
}
