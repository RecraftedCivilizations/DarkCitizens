package com.github.recraftedcivilizations.darkcitizens.commands

import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.jobs.special.mayor.IMayor
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CreateLaw(private val dPlayerManager: DPlayerManager, private val jobManager: JobManager): CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player){ sender.sendMessage("Fuck off console man!!"); return false }

        val dPlayer = dPlayerManager.getDPlayer(sender)!!
        val playerJob = jobManager.getJob(dPlayer.job)!!

        if (playerJob !is IMayor){ sender.sendMessage("${ChatColor.RED}You need to be a mayor to set laws"); return false}
        if (args.size < 2){ sender.sendMessage("${ChatColor.RED}You need to supply a name and a description of the law"); return false }

        val name = args[0]
        val description = args.drop(1).joinToString(" ")

        playerJob.createLaw(name, description)
        return true
    }

}