package com.github.recraftedcivilizations.darkcitizens.api.PAPI

import com.github.recraftedcivilizations.darkcitizens.laws.LawManager
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

class LawsPlaceholder(private val lawManager: LawManager, private vararg val authors: String, private val version: String) : PlaceholderExpansion() {


    override fun persist(): Boolean {
        return true
    }

    override fun canRegister(): Boolean {
        return true
    }

    override fun getAuthor(): String {
        return authors.joinToString(", ")
    }

    override fun getIdentifier(): String {
        return "darkcitizens"
    }

    override fun getVersion(): String {
        return version
    }

    override fun onPlaceholderRequest(player: Player?, identifier: String): String? {
        val laws = lawManager.getLaws()

        if (identifier == "laws"){
            var outputString = ""
            for (law in laws){
                outputString += law.name
                outputString += "\n"
                outputString += law.description
                outputString += "\n"
            }

            return outputString
        }else{
            return when {
                identifier.startsWith("lawName") -> {
                    val cutIdent = identifier.removePrefix("lawName")
                    val lawNum = cutIdent.toInt()

                    laws.toTypedArray()[lawNum].name

                }
                identifier.startsWith("lawDescription") -> {
                    val cutIdent = identifier.removePrefix("lawDescription")
                    val lawNum = cutIdent.toInt()

                    laws.toTypedArray()[lawNum].description
                }
                else -> {
                    lawManager.getLaw(identifier)?.description
                }
            }
        }
    }
}