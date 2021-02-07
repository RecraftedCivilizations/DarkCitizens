package com.github.recraftedcivilizations.parser.dataparser

import com.github.recraftedcivilizations.BukkitWrapper
import com.github.recraftedcivilizations.dPlayer.DPlayer
import com.github.recraftedcivilizations.dPlayer.DPlayerData1
import com.github.recraftedcivilizations.dPlayer.DPlayerData2
import com.github.recraftedcivilizations.dPlayer.DPlayerFactory
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.*

class YMLDataSource(var filePath: String, val bukkitWrapper: BukkitWrapper = BukkitWrapper()) : IParseData {
    private val dataFile : YamlConfiguration = YamlConfiguration()

    companion object{
        const val fileName = "data.yml"
        const val dPlayerDataPath = "DPlayerData"
        const val jobName = "job"
        const val wantedName = "wanted"
        const val criminalName = "isCriminal"
        const val groupLvlName = "groupLvls"
        const val groupXpName = "groupXps"
    }

    init {

        filePath = if (filePath.endsWith("/")){
            "$filePath$fileName"
        }else{
            "$filePath/$fileName"
        }

        val file = File(filePath)

        if (file.exists()){
            dataFile.load(file)
            bukkitWrapper.info("Found old data file, loading data")
        }
    }

    override fun getDPlayer(playerUUID: UUID): DPlayer {
        val configSection = dataFile.getConfigurationSection("$dPlayerDataPath.$playerUUID")

        val dPlayer =  if (configSection == null){
            DPlayerFactory.createDPlayer(bukkitWrapper.getPlayer(playerUUID)!!)
        }else{
            configSectionToDPlayer(configSection, playerUUID)
        }

        return dPlayer ?: DPlayerFactory.createDPlayer(bukkitWrapper.getPlayer(playerUUID)!!)
    }

    override fun setDPlayer(dPlayer: DPlayer) {
        setDPlayer(dPlayer.serializeData())
    }

    override fun setDPlayer(dData: DPlayerData1) {
        setDPlayer(dData.toDPlayerData2(), dData.uuid)
    }

    override fun setDPlayer(dData: DPlayerData2, playerUUID: UUID) {
        val path = "$dPlayerDataPath.$playerUUID"

        dataFile.set("$path.$jobName", dData.job)
        dataFile.set("$path.$wantedName", dData.wanted)
        dataFile.set("$path.$criminalName", dData.isCriminal)

        saveMap("$path.$groupLvlName", dData.groupLvls)
        saveMap("$path.$groupXpName", dData.groupXps)
    }

    private fun configSectionToDPlayer(configurationSection: ConfigurationSection, playerUUID: UUID): DPlayer?{

        val job = configurationSection.getString(jobName)
        val wanted = configurationSection.getBoolean(wantedName)
        val isCriminal = configurationSection.getBoolean(criminalName)

        if ( configurationSection.getConfigurationSection(groupLvlName) == null || configurationSection.getConfigurationSection(groupXpName) == null){
            return null
        }

        val groupLvls = configSectionToMap<String, Int>(configurationSection.getConfigurationSection(groupLvlName)!!)
        val groupXps = configSectionToMap<String, Int>(configurationSection.getConfigurationSection(groupXpName)!!)

        return DPlayerFactory.createDPlayer(playerUUID, job, wanted, isCriminal, groupLvls, groupXps)
    }

    private fun <K, V> configSectionToMap(configurationSection: ConfigurationSection): Map<K, V> {
        val output = emptyMap<K, V>().toMutableMap()
        for (key in configurationSection.getKeys(false)){
            output[key as K] = configurationSection.get(key) as V
        }

        return output
    }

    private fun saveMap(path: String, map: Map<String, Any>){
        for (key in map.keys){
            dataFile.set(key, map[key])
        }
    }
}