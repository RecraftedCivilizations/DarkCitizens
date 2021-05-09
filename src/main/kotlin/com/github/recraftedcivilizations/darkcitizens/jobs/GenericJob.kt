package com.github.recraftedcivilizations.darkcitizens.jobs

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.events.JobLeaveEvent
import com.github.recraftedcivilizations.darkcitizens.tasks.ITask
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

/**
 * @author DarkVanityOfLight
 */



/**
 * A Generic Implementation of a job,
 * @see IJob
 * @constructor Construct using a [name], a [group] the job belongs,
 * a [playerLimit] a [tasks] set consisting of [ITask], a [canDemote] set consisting
 * of other job names they can demote, a [baseIncome], a [baseXPGain], a [minLvl] to join the job,
 * if permissions are required to join the job([permissionRequired]) and a
 * [DPlayerManager]. The [bukkitWrapper] is only for testing purposes and should not be passed
 */
abstract class GenericJob(
    override val name: String,
    override val group: String,
    override val playerLimit: Int,
    override val tasks: Set<ITask>,
    override val canDemote: Set<String>,
    override val baseIncome: Int,
    override val baseXPGain: Int,
    override val minLvl: Int,
    override val permissionRequired: Boolean,
    override val icon: Material,
    override val leaveOnDeath: Boolean,
    override val prefix: String,
    private val dPlayerManager: DPlayerManager,
    private val jobManager: JobManager,
    private var bukkitWrapper: BukkitWrapper = BukkitWrapper()
) : IJob {
    override val currentMembers: MutableSet<DPlayer> = emptySet<DPlayer>().toMutableSet()

    /**
     * Set the bukkit wrapper, debugging purposes only
     * @param bukkitWrapper The bukkit wrapper to set
     */
    override fun setBukkitWrapper(bukkitWrapper: BukkitWrapper){
        this.bukkitWrapper = bukkitWrapper
    }

    /**
     * Remove a dPlayer from this job
     * @param player the [DPlayer] to remove
     */
    override fun removePlayer(player: DPlayer) {
        for (member in currentMembers){
            if (member.uuid == player.uuid){
                currentMembers.remove(member)
                break
            }
        }
    }

    /**
     * Remove a player from this job, this will call
     * [removePlayer] with the dPlayer as argument
     * @param player The player to remove
     */
    override fun removePlayer(player: Player) {
        removePlayer(dPlayerManager.getDPlayer(player)!!)
    }

    /**
     * Add a dPlayer to the job
     * @param player The [DPlayer] to add
     */
    override fun addPlayer(player: DPlayer) {
        currentMembers.add(player)
    }

    /**
     * Add a Player to the job, this will call
     * [addPlayer] with DPlayer as arg
     * @param player The player to add
     */
    override fun addPlayer(player: Player) {
        addPlayer(dPlayerManager.getDPlayer(player)!!)
    }

    /**
     * Check if a DPlayer can join this job,
     * this calls [canJoin] with the regular player as arg
     * @param player The DPlayer to check
     * @return true if the player can join false if not
     */
    override fun canJoin(player: DPlayer): Boolean {
        return canJoin(bukkitWrapper.getPlayer(player)!!)
    }

    /**
     * Check if a player can join this job
     * @param player The player to check
     * @return true if the player can join false if not
     */
    override fun canJoin(player: Player): Boolean {
        val dPlayer = dPlayerManager.getDPlayer(player)

        dPlayer!!

        if (dPlayer.groupLvls[group] == null){
            dPlayer.groupLvls[group] = 0
            dPlayer.groupXps[group] = 0
        }
        if (dPlayer.groupLvls[group]!! >= minLvl){
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

    /**
     * Check if the player is already a member of this job
     * @param uuid The uuid of the player to check for
     * @return true if the player is a member false if not
     */
    override fun isMember(uuid: UUID): Boolean {
        for (member in currentMembers){
            if (member.uuid == uuid){
                return true
            }
        }
        return false
    }

    /**
     * Check if the dPlayer is already member of this job,
     * this will call [isMember] with the players uuid
     * @param player the DPlayer to check for
     */
    override fun isMember(player: DPlayer): Boolean {
        return isMember(player.uuid)
    }

    /**
     * Check if the player is already member of this job,
     * this will call [isMember] with the players uuid
     * @param player The player to check for
     */
    override fun isMember(player: Player): Boolean {
        return isMember(player.uniqueId)
    }

    /**
     * Join this job, this will check for if the player can actually join the job
     * @param player The player to join
     */
    override fun join(player: Player) {
        dPlayerManager.getDPlayer(player.uniqueId)?.let { join(it) }
    }

    override fun join(dPlayer: DPlayer) {
        // Leave the old job, ugly ik
        if (canJoin(dPlayer)){
            dPlayer.job?.let { jobManager.getJob(it) }?.leave(dPlayer)
            this.addPlayer(dPlayer)
            dPlayer.job = name
            bukkitWrapper.getPlayer(dPlayer)?.sendMessage("${ChatColor.GREEN}You successfully joined the job $name")
            dPlayerManager.setDPlayer(dPlayer)
        }
    }

    /**
     * Leave this job
     * @param dPlayer The player to leave this job
     */
    override fun leave(dPlayer: DPlayer) {
        if(isMember(dPlayer.uuid)){
            removePlayer(dPlayer)
            dPlayer.job = null
            dPlayerManager.setDPlayer(dPlayer)
            val leaveEvent = JobLeaveEvent(dPlayer, this)
            bukkitWrapper.getPluginManager().callEvent(leaveEvent)
        }
    }

    /**
     * Leave this job, this will call [leave] with dPlayer as argument
     * @param player The player to leave this job
     */
    override fun leave(player: Player) {
        dPlayerManager.getDPlayer(player.uniqueId)?.let { leave(it) }
    }
}
