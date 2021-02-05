package com.github.recraftedcivilizations

import com.github.recraftedcivilizations.dPlayer.DPlayer
import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
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
        Bukkit.getLogger().log(Level.INFO, message)
    }

    open fun info(message: String?, vararg vars: Any?) {
        Bukkit.getLogger().log(Level.INFO, message, vars)
    }

    open fun severe(message: String?) {
        Bukkit.getLogger().log(Level.SEVERE, message)
    }

    open fun severe(message: String?, error: Throwable?) {
        Bukkit.getLogger().log(Level.SEVERE, message, error)
    }

    open fun warning(message: String?) {
        Bukkit.getLogger().log(Level.WARNING, message)
    }

    open fun warning(message: String?, error: Throwable?) {
        Bukkit.getLogger().log(Level.WARNING, message, error)
    }

    open fun warning(message: String?, vararg vars: Any?) {
        Bukkit.getLogger().log(Level.WARNING, message, vars)
    }

    open fun notify(msg: String, color: BarColor, style: BarStyle, displayTime: Int, players: Collection<Player>){
        com.github.darkvanityoflight.recraftedcore.utils.notifyutils.notify(msg, color, style, displayTime, players)
    }
}