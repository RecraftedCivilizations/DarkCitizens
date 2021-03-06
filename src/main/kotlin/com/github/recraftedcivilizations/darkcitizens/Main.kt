package com.github.recraftedcivilizations.darkcitizens

import com.github.darkvanityoflight.recraftedcore.ARecraftedPlugin
import com.github.darkvanityoflight.recraftedcore.gui.GUIListener
import com.github.recraftedcivilizations.darkcitizens.commands.ShowJobs
import com.github.recraftedcivilizations.darkcitizens.commands.ShowTasks
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.groups.GroupManager
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.parser.ConfigParser
import com.github.recraftedcivilizations.darkcitizens.parser.dataparser.IParseData
import com.github.recraftedcivilizations.darkcitizens.parser.dataparser.YMLDataSource
import com.github.recraftedcivilizations.darkcitizens.tasks.TaskManager
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

    override fun onEnable(){

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

        Bukkit.getServer().pluginManager.registerEvents(GUIListener(), this)
        this.getCommand("jobs")?.setExecutor(ShowJobs(jobManager, dPlayerManager))
        this.getCommand("tasks")?.setExecutor(ShowTasks(jobManager, dPlayerManager))

    }

    private fun initManagers(){
        dPlayerManager = DPlayerManager(dataParser)
        taskManager = TaskManager(com.github.recraftedcivilizations.darkcitizens.Main.Companion.econ!!, dPlayerManager)
        jobManager = JobManager(dPlayerManager)
        groupManager = GroupManager()

        taskManager.setJobManager(jobManager)
        jobManager.setTaskManager(taskManager)
    }

    companion object{
        var econ: Economy? = null
    }
}