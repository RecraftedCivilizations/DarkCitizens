package com.github.recraftedcivilizations.darkcitizens.commands

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.jobs.special.major.IMajor
import com.github.recraftedcivilizations.darkcitizens.laws.LawManager
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetTaxes(private val dPlayerManager: DPlayerManager, private val jobManager: JobManager): CommandExecutor {
    override fun onCommand(sender: CommandSender, p1: Command, p2: String, args: Array<out String>): Boolean {
        if(sender !is Player){ sender.sendMessage("Fuck off console man!!"); return false }

        val dPlayer = dPlayerManager.getDPlayer(sender)!!
        val playerJob = jobManager.getJob(dPlayer.job)!!

        if (playerJob !is IMajor){ sender.sendMessage("${ChatColor.RED}You need to be a major to set laws"); return false}

        val amount: Int
        try{
            amount = args[0].toInt()
        }catch (e: NumberFormatException){
            sender.sendMessage("${ChatColor.RED}That isn't a number!")
            return false
        }

        return if(amount > 99 || amount < 0){
            sender.sendMessage("${ChatColor.RED}You can't set the tax amount to $amount%")
            false
        }else{
            playerJob.setTaxLaw(amount)
            true
        }
    }
}