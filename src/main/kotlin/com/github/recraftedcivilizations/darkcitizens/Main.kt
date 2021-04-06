package com.github.recraftedcivilizations.darkcitizens

import com.github.darkvanityoflight.recraftedcore.ARecraftedPlugin
import com.github.darkvanityoflight.recraftedcore.gui.GUIListener
import com.github.recraftedcivilizations.darkcitizens.api.PAPI.LawsPlaceholder
import com.github.recraftedcivilizations.darkcitizens.commands.ShowElections
import com.github.recraftedcivilizations.darkcitizens.commands.ShowJobs
import com.github.recraftedcivilizations.darkcitizens.commands.ShowTasks
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.election.ElectionManager
import com.github.recraftedcivilizations.darkcitizens.groups.GroupManager
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.laws.LawManager
import com.github.recraftedcivilizations.darkcitizens.listeners.DataCleaner
import com.github.recraftedcivilizations.darkcitizens.listeners.ElectionTrigger
import com.github.recraftedcivilizations.darkcitizens.listeners.FriendlyFire
import com.github.recraftedcivilizations.darkcitizens.parser.ConfigParser
import com.github.recraftedcivilizations.darkcitizens.parser.dataparser.IParseData
import com.github.recraftedcivilizations.darkcitizens.parser.dataparser.YMLDataSource
import com.github.recraftedcivilizations.darkcitizens.runnables.BaseIncomeRunner
import com.github.recraftedcivilizations.darkcitizens.tasks.TaskManager
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.plugin.RegisteredServiceProvider

/**
 * @author DarkVanityOfLight
 */

/**
 * The Main plugin
 */
class Main : ARecraftedPlugin() {
    lateinit var dataParser: IParseData
    lateinit var configParser: ConfigParser
    lateinit var taskManager: TaskManager
    lateinit var jobManager: JobManager
    lateinit var groupManager: GroupManager
    lateinit var dPlayerManager: DPlayerManager
    lateinit var electionManager: ElectionManager
    lateinit var lawManager: LawManager

    override fun onEnable(){

        saveDefaultConfig()

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
        this.getCommand("jobs")?.setExecutor(ShowJobs(jobManager, dPlayerManager, electionManager))
        this.getCommand("tasks")?.setExecutor(ShowTasks(jobManager, dPlayerManager))
        this.getCommand("elections")?.setExecutor(ShowElections(electionManager))
        server.pluginManager.registerEvents(DataCleaner(dPlayerManager, jobManager), this)
        server.pluginManager.registerEvents(ElectionTrigger(dPlayerManager, jobManager, electionManager), this)
        server.pluginManager.registerEvents(FriendlyFire(dPlayerManager, jobManager, groupManager), this)

        BaseIncomeRunner(jobManager, dPlayerManager, econ!!, groupManager).runTaskTimer(this, configParser.baseIncomeTime.toLong() * 60L * 20L, configParser.baseIncomeTime.toLong() * 60L * 20L)

        initApi()
    }

    /**
     * Initialize all managers
     */
    private fun initManagers(){
        groupManager = GroupManager()
        lawManager = LawManager()
        dPlayerManager = DPlayerManager(dataParser)
        taskManager = TaskManager(econ!!, dPlayerManager, groupManager)
        jobManager = JobManager(dPlayerManager, lawManager)
        electionManager = ElectionManager(dPlayerManager, econ!!, this)

        taskManager.setJobManager(jobManager)
        jobManager.setTaskManager(taskManager)
    }

    private fun initApi(){
        LawsPlaceholder(lawManager, description.authors.joinToString(", "), description.version).register()
    }

    companion object{
        var econ: Economy? = null
    }
}