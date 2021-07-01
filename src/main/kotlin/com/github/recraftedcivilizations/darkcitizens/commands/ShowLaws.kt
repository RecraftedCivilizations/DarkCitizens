package com.github.recraftedcivilizations.darkcitizens.commands

import com.github.recraftedcivilizations.darkcitizens.laws.LawManager
import com.github.recraftedcivilizations.darkcitizens.laws.TaxLaw
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta

class ShowLaws(private val lawManager: LawManager): CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Fuck off console man!!"); return false
        }

        val laws = lawManager.getLaws().toTypedArray()
        val lawBook = ItemStack(Material.WRITTEN_BOOK)
        val lawBookMeta = lawBook.itemMeta as BookMeta
        lawBookMeta.title = "Laws"
        lawBookMeta.author = "The Government"

        val pages = List<String>(laws.size) { i ->
            if (laws[i] is TaxLaw){
                "${laws[i].name}\n${laws[i].description}\nThe current tax is at ${(laws[i] as TaxLaw).taxAmount}%"
            }else{
                "${laws[i].name}\n${laws[i].description}"
            }
        }

        lawBookMeta.pages = pages

        lawBook.itemMeta = lawBookMeta
        sender.openBook(lawBook)
        return true

    }


}