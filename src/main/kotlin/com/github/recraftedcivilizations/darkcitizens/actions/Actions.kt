package com.github.recraftedcivilizations.darkcitizens.actions

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
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

        override fun register() {
            ActionManager.registerAction(this)
        }

        override fun resetForPlayer(player: Player) {
            return
        }

        override fun resetForPlayer(player: DPlayer) {
            return
        }

        override fun resetOneForPlayer(player: Player) {
            return
        }

        override fun resetOneForPlayer(player: DPlayer) {
            return
        }
    }

}