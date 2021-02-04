package com.github.recraftedcivilizations

import com.github.recraftedcivilizations.dPlayer.DPlayer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import java.util.logging.Level

/**
 * @author DarkVanityOfLight
 */


/**
 * A wrapper for the getPlayer function from Bukkit
 * this is mainly for testing purposes
 */
open class BukkitWrapper{

    private val logger = Bukkit.getLogger()

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

    open fun info(message: String?) {
        logger.log(Level.INFO, message)
    }

    open fun info(message: String?, vararg vars: Any?) {
        logger.log(Level.INFO, message, vars)
    }

    open fun severe(message: String?) {
        logger.log(Level.SEVERE, message)
    }

    open fun severe(message: String?, error: Throwable?) {
        logger.log(Level.SEVERE, message, error)
    }

    open fun warning(message: String?) {
        logger.log(Level.WARNING, message)
    }

    open fun warning(message: String?, error: Throwable?) {
        logger.log(Level.WARNING, message, error)
    }

    open fun warning(message: String?, vararg vars: Any?) {
        logger.log(Level.WARNING, message, vars)
    }
}