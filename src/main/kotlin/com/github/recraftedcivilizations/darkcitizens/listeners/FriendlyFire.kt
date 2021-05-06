package com.github.recraftedcivilizations.darkcitizens.listeners

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.groups.GroupManager
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class FriendlyFire(private val dPlayerManager: DPlayerManager, private val jobManager: JobManager, private val groupManager: GroupManager) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onDamage(e: EntityDamageByEntityEvent){
        val attacked = e.entity
        val attacker = e.damager

        if(attacked is Player){
            val attackedDPlayer = dPlayerManager.getDPlayer(attacked.uniqueId)
            val attackerDPlayer: DPlayer? = when (attacker) {
                is Player -> {
                    // Get the groups
                    dPlayerManager.getDPlayer(attacker.uniqueId)
                }
                is Projectile -> {
                    // Check that the projectile came from a player
                    if (attacker.shooter !is Player) return
                    dPlayerManager.getDPlayer(attacker.shooter as Player)

                }
                else -> {
                    // If we don't have an direct attack or a projectile return
                    return
                }
            }


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