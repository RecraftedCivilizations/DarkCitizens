package com.github.recraftedcivilizations.tasks.actions

import com.github.recraftedcivilizations.dPlayer.DPlayer
import org.bukkit.entity.Player

/**
 * @author DarkVanityOfLight
 */

/**
 * All actions have to be registered here
 */
enum class Actions : IAction {

    DEBUG {
        override val description: String
            get() = "Foo"

        override fun isCompletedForPlayer(player: DPlayer): Boolean {
            return true
        }

        override fun isCompletedForPlayer(player: Player): Boolean {
            return true
        }
    }

}