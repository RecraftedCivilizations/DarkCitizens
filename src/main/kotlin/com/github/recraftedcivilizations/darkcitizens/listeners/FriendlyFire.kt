package com.github.recraftedcivilizations.darkcitizens.listeners

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.groups.GroupManager
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class FriendlyFire(private val dPlayerManager: DPlayerManager, private val jobManager: JobManager, private val groupManager: GroupManager) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onDamage(e: EntityDamageByEntityEvent){
        val attacked = e.entity
        val attacker = e.damager

        if (attacked is Player && attacker is Player){
            // Get the groups
            val attackedDPlayer = dPlayerManager.getDPlayer(attacked.uniqueId)
            val attackerDPlayer = dPlayerManager.getDPlayer(attacker.uniqueId)

            val attackedJob = jobManager.getJob(attackedDPlayer?.job)
            val attackerJob = jobManager.getJob(attackerDPlayer?.job)

            val attackedGroup = groupManager.getGroup(attackedJob?.group)
            val attackerGroup = groupManager.getGroup(attackerJob?.group)

            // If they are in the same group and the group doesn't have friendly fire, cancel the damage
            if (attackerGroup != null && attackedGroup != null){
                if (attackedGroup == attackerGroup && !attackedGroup.friendlyFire){
                    e.isCancelled = true
                }
            }
        }

    }

}