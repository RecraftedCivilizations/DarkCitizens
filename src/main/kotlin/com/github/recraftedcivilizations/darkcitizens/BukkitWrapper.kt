package com.github.recraftedcivilizations.darkcitizens

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
/**
 * @author DarkVanityOfLight
 */


/**
 * A wrapper for the getPlayer function from Bukkit
 * this is mainly for testing purposes
 */
open class BukkitWrapper: com.github.darkvanityoflight.recraftedcore.api.BukkitWrapper() {

    /**
     * Get a Bukkit player from a [DPlayer]
     * @param player The player to get the Bukkit player for
     */
    open fun getPlayer(player: DPlayer): Player? {
        return Bukkit.getPlayer(player.uuid)
    }

    /**
     * Get a Bukkit player from his name
     * @param name The name of the player
     * @return The player or null if no one is found
     */
    open fun getPlayer(name: String): Player?{

        return Bukkit.getPlayer(name)
    }
}