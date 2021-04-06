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

class RemoveLaw(private val lawManager: LawManager, private val dPlayerManager: DPlayerManager, private val jobManager: JobManager): CommandExecutor {

    override fun onCommand(sender: CommandSender, p1: Command, p2: String, args: Array<out String>): Boolean {
        if(sender !is Player){ sender.sendMessage("Fuck off console man!!"); return false }

        val dPlayer = dPlayerManager.getDPlayer(sender)!!
        val job = jobManager.getJob(dPlayer.job)

        if(job !is IMajor){ sender.sendMessage("${ChatColor.RED}You need to be a major to do that"); return false }

        if (args.isEmpty()){return false}

        lawManager.removeLaw(args.joinToString(" "))
        return true
    }

}