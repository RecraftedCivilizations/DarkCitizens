package com.github.recraftedcivilizations.darkcitizens

import com.github.darkvanityoflight.recraftedcore.ARecraftedPlugin
import com.github.darkvanityoflight.recraftedcore.gui.GUIListener
import com.github.recraftedcivilizations.darkcitizens.api.PAPI.GroupsPlaceholder
import com.github.recraftedcivilizations.darkcitizens.api.PAPI.JobsPlaceholder
import com.github.recraftedcivilizations.darkcitizens.api.PAPI.LawsPlaceholder
import com.github.recraftedcivilizations.darkcitizens.commands.*
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import com.github.recraftedcivilizations.darkcitizens.election.ElectionManager
import com.github.recraftedcivilizations.darkcitizens.groups.GroupManager
import com.github.recraftedcivilizations.darkcitizens.jobs.JobManager
import com.github.recraftedcivilizations.darkcitizens.laws.LawManager
import com.github.recraftedcivilizations.darkcitizens.listeners.DataCleaner
import com.github.recraftedcivilizations.darkcitizens.listeners.ElectionTrigger
import com.github.recraftedcivilizations.darkcitizens.listeners.FriendlyFire
import com.github.recraftedcivilizations.darkcitizens.listeners.InfoDisplay
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
class DarkCitizens : ARecraftedPlugin() {
    private lateinit var dataParser: IParseData
    private lateinit var configParser: ConfigParser
    private lateinit var taskManager: TaskManager
    private lateinit var jobManager: JobManager
    private lateinit var groupManager: GroupManager
    private lateinit var dPlayerManager: DPlayerManager
    private lateinit var electionManager: ElectionManager
    private lateinit var lawManager: LawManager
    private lateinit var dataCleaner: DataCleaner

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
        configParser = ConfigParser(config, dataFolder.absolutePath, taskManager, jobManager, groupManager, dPlayerManager)
        configParser.read()

        this.getCommand("jobs")?.setExecutor(ShowJobs(jobManager, dPlayerManager, electionManager))
        this.getCommand("tasks")?.setExecutor(ShowTasks(jobManager, dPlayerManager))
        this.getCommand("elections")?.setExecutor(ShowElections(electionManager))
        this.getCommand("createLaw")?.setExecutor(CreateLaw(dPlayerManager, jobManager))
        this.getCommand("removeLaw")?.setExecutor(RemoveLaw(dPlayerManager, jobManager))
        this.getCommand("laws")?.setExecutor(ShowLaws(lawManager))
        this.getCommand("setTaxes")?.setExecutor(SetTaxes(dPlayerManager, jobManager))
        this.getCommand("demote")?.setExecutor(Demote(dPlayerManager, jobManager))
        dataCleaner = DataCleaner(dPlayerManager, jobManager)
        server.pluginManager.registerEvents(dataCleaner, this)
        server.pluginManager.registerEvents(ElectionTrigger(dPlayerManager, jobManager, electionManager), this)
        server.pluginManager.registerEvents(FriendlyFire(dPlayerManager, jobManager, groupManager), this)
        server.pluginManager.registerEvents(InfoDisplay(groupManager), this)

        BaseIncomeRunner(jobManager, dPlayerManager, econ!!, groupManager, lawManager).runTaskTimer(this, configParser.baseIncomeTime.toLong() * 60L * 20L, configParser.baseIncomeTime.toLong() * 60L * 20L)

        var hasBank = false

        for (bank in econ!!.banks){

            if (bank == "CITYBANK"){
                hasBank = true
                break
            }
        }
        if (!hasBank){
            econ!!.createBank("CITYBANK", "")
        }



        initApi()
    }

    override fun onDisable() {
        dataCleaner.onShutdown()
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
        GroupsPlaceholder(groupManager, dPlayerManager, jobManager, description.authors.joinToString(", "), description.version).register()
        JobsPlaceholder(jobManager, dPlayerManager, description.authors.joinToString(", "), description.version).register()
        DarkCitizens.taskManager = taskManager
        DarkCitizens.jobManager = jobManager
        DarkCitizens.groupManager = groupManager
        DarkCitizens.dPlayerManager = dPlayerManager
        DarkCitizens.electionManager = electionManager
        DarkCitizens.lawManager = lawManager
    }

    companion object{
        var econ: Economy? = null
        lateinit var taskManager: TaskManager
        lateinit var jobManager: JobManager
        lateinit var groupManager: GroupManager
        lateinit var dPlayerManager: DPlayerManager
        lateinit var electionManager: ElectionManager
        lateinit var lawManager: LawManager
    }
}