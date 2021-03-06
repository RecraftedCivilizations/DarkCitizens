package com.github.recraftedcivilizations.darkcitizens.parser.dataparser


import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayer
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerData1
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerData2
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerFactory
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.*

/**
 * @author DarkVanityOfLight
 */

/**
 * Save and read data from a YML file
 * @see IParseData
 * @constructor Construct using a base [filePath] and an optional bukkitWrapper for testing
 */
class YMLDataSource(var filePath: String, private val bukkitWrapper: com.github.recraftedcivilizations.darkcitizens.BukkitWrapper = com.github.recraftedcivilizations.darkcitizens.BukkitWrapper()) : IParseData {
    private val dataFile : YamlConfiguration = YamlConfiguration()

    /**
     * Store names and paths for the config file
     */
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
        }else{
            file.createNewFile()
        }
    }

    /**
     * Get a DPlayer from the DataSource
     * @param playerUUID The UUID of the player you want to get
     */
    override fun getDPlayer(playerUUID: UUID): DPlayer {

        load()

        val configSection = dataFile.getConfigurationSection("$dPlayerDataPath.$playerUUID")

        val dPlayer =  if (configSection == null){
            DPlayerFactory.createDPlayer(bukkitWrapper.getPlayer(playerUUID)!!)
        }else{
            configSectionToDPlayer(configSection, playerUUID)
        }

        return dPlayer ?: DPlayerFactory.createDPlayer(bukkitWrapper.getPlayer(playerUUID)!!)
    }

    /**
     * Set a DPlayer in the DataSource
     * @param dPlayer The player to save to the DataSource
     */
    override fun setDPlayer(dPlayer: DPlayer) {
        setDPlayer(dPlayer.serializeData())
    }

    /**
     * Set a DPlayer from dPlayerData of the form [DPlayerData1]
     * @param dData The dPlayerData of the player
     */
    override fun setDPlayer(dData: DPlayerData1) {
        setDPlayer(dData.toDPlayerData2(), dData.uuid)
    }

    /**
     * Set a DPlayer from dPlayerData of the for [DPlayerData2] and the player UUID
     * @param dData The dPlayerData of the player
     * @param playerUUID The UUID of the player
     */
    override fun setDPlayer(dData: DPlayerData2, playerUUID: UUID) {

        load()

        val path = "$dPlayerDataPath.$playerUUID"

        dataFile.set("$path.$jobName", dData.job)
        dataFile.set("$path.$wantedName", dData.wanted)
        dataFile.set("$path.$criminalName", dData.isCriminal)

        saveMap("$path.$groupLvlName", dData.groupLvls)
        saveMap("$path.$groupXpName", dData.groupXps)

        save()
    }

    /**
     * Convert a configuration section and an UUID to a [DPlayer], returns null if
     * the section is incomplete
     * @param configurationSection The config section to parse
     * @param playerUUID The UUID for the [DPlayer]
     */
    private fun configSectionToDPlayer(configurationSection: ConfigurationSection, playerUUID: UUID): DPlayer?{

        val job = configurationSection.getString(jobName)
        val wanted = configurationSection.getBoolean(wantedName)
        val isCriminal = configurationSection.getBoolean(criminalName)

        val groupLvls: Map<String, Int> = if ( configurationSection.getConfigurationSection(groupLvlName) != null){
            configSectionToMap<String, Int>(configurationSection.getConfigurationSection(groupLvlName)!!)
        }else{
            emptyMap<String, Int>()
        }

        val groupXps: Map<String, Int> = if (configurationSection.getConfigurationSection(groupXpName) != null){
            configSectionToMap<String, Int>(configurationSection.getConfigurationSection(groupXpName)!!)
        }else{
            emptyMap<String, Int>()
        }


        return DPlayerFactory.createDPlayer(playerUUID, job, wanted, isCriminal, groupLvls, groupXps)
    }

    /**
     * Convert a configuration section to a map of the type [K], [V]
     * @param K The type of the keys
     * @param V The type of the Values
     * @param configurationSection The config section to read from
     */
    private fun <K, V> configSectionToMap(configurationSection: ConfigurationSection): Map<K, V> {
        val output = emptyMap<K, V>().toMutableMap()
        for (key in configurationSection.getKeys(false)){
            output[key as K] = configurationSection.get(key) as V
        }

        return output
    }

    /**
     * Save a map to the given Path
     * @param path The path to save the map under
     * @param map The map to save
     */
    private fun saveMap(path: String, map: Map<String, Any>){
        for (key in map.keys){
            dataFile.set("$path.$key", map[key])
        }
    }

    private fun load(){
        dataFile.load(filePath)
    }

    private fun save(){
        dataFile.save(filePath)
    }
}