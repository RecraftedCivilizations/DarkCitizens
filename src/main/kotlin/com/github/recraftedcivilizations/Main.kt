package com.github.recraftedcivilizations

import com.github.darkvanityoflight.recraftedcore.ARecraftedPlugin
import com.github.recraftedcivilizations.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.groups.GroupManager
import com.github.recraftedcivilizations.jobs.JobManager
import com.github.recraftedcivilizations.parser.ConfigParser
import com.github.recraftedcivilizations.parser.dataparser.IParseData
import com.github.recraftedcivilizations.parser.dataparser.YMLDataSource
import com.github.recraftedcivilizations.tasks.TaskManager
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.plugin.RegisteredServiceProvider

class Main : ARecraftedPlugin() {
    lateinit var dataParser: IParseData
    lateinit var configParser: ConfigParser
    lateinit var taskManager: TaskManager
    lateinit var jobManager: JobManager
    lateinit var groupManager: GroupManager
    lateinit var dPlayerManager: DPlayerManager

    override fun onLoad(){

        // Get the econ
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")){
            val rsp : RegisteredServiceProvider<Economy>? =  server.servicesManager.getRegistration(Economy::class.java)
            if (rsp != null) {
                econ = rsp.provider
            }
        }

        dataParser = YMLDataSource(dataFolder.absolutePath)

        initManagers()

        // Read the config
        configParser = ConfigParser(config, dataFolder.absolutePath, taskManager, jobManager, groupManager)
        configParser.read()

    }

    private fun initManagers(){
        dPlayerManager = DPlayerManager(dataParser)
        taskManager = TaskManager(econ!!, dPlayerManager)
        jobManager = JobManager(dPlayerManager)

        taskManager.setJobManager(jobManager)
        jobManager.setTaskManager(taskManager)
    }

    companion object{
        var econ: Economy? = null
    }
}