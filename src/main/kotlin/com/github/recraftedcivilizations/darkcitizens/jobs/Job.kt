package com.github.recraftedcivilizations.darkcitizens.jobs

import com.github.recraftedcivilizations.darkcitizens.BukkitWrapper
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.tasks.ITask
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*

/**
 * @author DarkVanityOfLight
 */



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
    private val jobManager: JobManager,
    private var bukkitWrapper: com.github.recraftedcivilizations.darkcitizens.BukkitWrapper = com.github.recraftedcivilizations.darkcitizens.BukkitWrapper()
) : IJob {
    override val currentMembers: MutableSet<DPlayer> = emptySet<DPlayer>().toMutableSet()

    override fun setBukkitWrapper(bukkitWrapper: BukkitWrapper){
        this.bukkitWrapper = bukkitWrapper
    }
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

    override fun join(dPlayer: DPlayer) {
        if(this.canJoin(dPlayer)){
            // Leave the old job, ugly ik
            dPlayer.job?.let { jobManager.getJob(it) }?.leave(dPlayer)
            this.addPlayer(dPlayer)
            dPlayer.job = name
            bukkitWrapper.getPlayer(dPlayer)?.sendMessage("${ChatColor.GREEN}You successfully joined the job $name")
            dPlayerManager.setDPlayer(dPlayer)

        }
    }

    override fun join(player: Player) {
        dPlayerManager.getDPlayer(player.uniqueId)?.let { join(it) }
    }

    override fun leave(dPlayer: DPlayer) {
        removePlayer(dPlayer)
        dPlayer.job = null
        dPlayerManager.setDPlayer(dPlayer)
    }
    override fun leave(player: Player) {
        dPlayerManager.getDPlayer(player.uniqueId)?.let { leave(it) }
    }
}
