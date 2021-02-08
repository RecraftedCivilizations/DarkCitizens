package com.github.recraftedcivilizations.parser.dataparser

import com.github.recraftedcivilizations.BukkitWrapper
import com.github.recraftedcivilizations.dPlayer.DPlayer
import com.github.recraftedcivilizations.dPlayer.DPlayerData2
import com.github.recraftedcivilizations.jobs.randomString
import com.nhaarman.mockitokotlin2.*
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import java.io.File
import java.util.*
import kotlin.random.Random

const val path = "."

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class YMLDataSourceTest {

    private val playerUUID = UUID.randomUUID()

    private val playerMock = mock<Player>{ on { uniqueId } doReturn playerUUID }

    private val bukkitWrapper = mock<BukkitWrapper>{ on { getPlayer(playerUUID) } doReturn playerMock }


    @BeforeAll
    fun setUp(){
        doNothing().whenever(bukkitWrapper).info(any())
    }

    @AfterEach
    fun cleanUp(){
        val file = File(path + "/" +  YMLDataSource.fileName)
        file.delete()
    }

    @Test
    fun shouldConstructWithIncompleteName(){
        val dataSource = YMLDataSource(path, bukkitWrapper)
        assertEquals("$path/${YMLDataSource.fileName}", dataSource.filePath)
        assertEquals(true, File(dataSource.filePath).exists())
    }

    @Test
    fun shouldConstructWithCompleteName(){
        val dataSource = YMLDataSource("$path/", bukkitWrapper)
        assertEquals("$path/${YMLDataSource.fileName}", dataSource.filePath)
        assertEquals(true, File(dataSource.filePath).exists())
    }

    @Test
    fun shouldConstructWithOldFile(){
        val file = File("$path/${YMLDataSource.fileName}")
        file.createNewFile()

        YMLDataSource(path, bukkitWrapper)
        verify(bukkitWrapper).info("Found old data file, loading data")
    }

    @Test
    fun setDPlayer() {
        // DPlayer data and UUID
        val dataSource = YMLDataSource(path, bukkitWrapper)
        val dPlayerData = DPlayerData2(randomString(), Random.nextBoolean(), Random.nextBoolean(), mapOf(Pair(randomString(), Random.nextInt()), Pair(randomString(), Random.nextInt())), mapOf(Pair(randomString(), Random.nextInt())))
        val uuid = UUID.randomUUID()

        dataSource.setDPlayer(dPlayerData, uuid)

        val dataFile = YamlConfiguration()
        dataFile.load(dataSource.filePath)
        assertEquals(true, dataFile.isSet(YMLDataSource.dPlayerDataPath))
        assertEquals(true, dataFile.isSet(YMLDataSource.dPlayerDataPath + "." + uuid.toString()))
        assertEquals(dPlayerData.job, dataFile.getString(YMLDataSource.dPlayerDataPath + "." + uuid +  "." + YMLDataSource.jobName))
        assertEquals(dPlayerData.wanted, dataFile.getBoolean(YMLDataSource.dPlayerDataPath + "." + uuid + "." + YMLDataSource.wantedName))
        assertEquals(dPlayerData.isCriminal, dataFile.getBoolean(YMLDataSource.dPlayerDataPath + "." + uuid + "." + YMLDataSource.criminalName))
        for (key in dPlayerData.groupLvls.keys){
            assertEquals(dPlayerData.groupLvls[key], dataFile.getInt(YMLDataSource.dPlayerDataPath + "." + uuid + "." + YMLDataSource.groupLvlName + "." + key))
        }

        for (key in dPlayerData.groupXps.keys){
            assertEquals(dPlayerData.groupXps[key], dataFile.getInt(YMLDataSource.dPlayerDataPath + "." + uuid + "." + YMLDataSource.groupXpName + "." + key))
        }
    }

    @Test
    fun testSetDPlayer() {
        // With DPlayer
        val dataSource = YMLDataSource(path, bukkitWrapper)
        val dPlayerData = DPlayerData2(randomString(), Random.nextBoolean(), Random.nextBoolean(), mapOf(Pair(randomString(), Random.nextInt()), Pair(randomString(), Random.nextInt())), mapOf(Pair(randomString(), Random.nextInt())))
        val uuid = UUID.randomUUID()
        val dPlayer = DPlayer(dPlayerData.toDPlayerData1(uuid))

        dataSource.setDPlayer(dPlayer)

        val dataFile = YamlConfiguration()
        dataFile.load(dataSource.filePath)
        assertEquals(true, dataFile.isSet(YMLDataSource.dPlayerDataPath))
        assertEquals(true, dataFile.isSet(YMLDataSource.dPlayerDataPath + "." + uuid.toString()))
        assertEquals(dPlayerData.job, dataFile.getString(YMLDataSource.dPlayerDataPath + "." + uuid +  "." + YMLDataSource.jobName))
        assertEquals(dPlayerData.wanted, dataFile.getBoolean(YMLDataSource.dPlayerDataPath + "." + uuid + "." + YMLDataSource.wantedName))
        assertEquals(dPlayerData.isCriminal, dataFile.getBoolean(YMLDataSource.dPlayerDataPath + "." + uuid + "." + YMLDataSource.criminalName))
        for (key in dPlayerData.groupLvls.keys){
            assertEquals(dPlayerData.groupLvls[key], dataFile.getInt(YMLDataSource.dPlayerDataPath + "." + uuid + "." + YMLDataSource.groupLvlName + "." + key))
        }

        for (key in dPlayerData.groupXps.keys){
            assertEquals(dPlayerData.groupXps[key], dataFile.getInt(YMLDataSource.dPlayerDataPath + "." + uuid + "." + YMLDataSource.groupXpName + "." + key))
        }
    }

    @Test
    fun testSetDPlayer1() {
        // With DPLayerData1

        val dataSource = YMLDataSource(path, bukkitWrapper)
        val dPlayerData = DPlayerData2(randomString(), Random.nextBoolean(), Random.nextBoolean(), mapOf(Pair(randomString(), Random.nextInt()), Pair(randomString(), Random.nextInt())), mapOf(Pair(randomString(), Random.nextInt())))
        val uuid = UUID.randomUUID()

        dataSource.setDPlayer(dPlayerData.toDPlayerData1(uuid))

        val dataFile = YamlConfiguration()
        dataFile.load(dataSource.filePath)
        assertEquals(true, dataFile.isSet(YMLDataSource.dPlayerDataPath))
        assertEquals(true, dataFile.isSet(YMLDataSource.dPlayerDataPath + "." + uuid.toString()))
        assertEquals(dPlayerData.job, dataFile.getString(YMLDataSource.dPlayerDataPath + "." + uuid +  "." + YMLDataSource.jobName))
        assertEquals(dPlayerData.wanted, dataFile.getBoolean(YMLDataSource.dPlayerDataPath + "." + uuid + "." + YMLDataSource.wantedName))
        assertEquals(dPlayerData.isCriminal, dataFile.getBoolean(YMLDataSource.dPlayerDataPath + "." + uuid + "." + YMLDataSource.criminalName))
        for (key in dPlayerData.groupLvls.keys){
            assertEquals(dPlayerData.groupLvls[key], dataFile.getInt(YMLDataSource.dPlayerDataPath + "." + uuid + "." + YMLDataSource.groupLvlName + "." + key))
        }

        for (key in dPlayerData.groupXps.keys){
            assertEquals(dPlayerData.groupXps[key], dataFile.getInt(YMLDataSource.dPlayerDataPath + "." + uuid + "." + YMLDataSource.groupXpName + "." + key))
        }
    }

    @Test
    fun getDPlayer() {
        val dataSource = YMLDataSource(path, bukkitWrapper)
        val dPlayerData = DPlayerData2(randomString(), Random.nextBoolean(), Random.nextBoolean(), mapOf(Pair(randomString(), Random.nextInt()), Pair(randomString(), Random.nextInt())), mapOf(Pair(randomString(), Random.nextInt())))
        val uuid = UUID.randomUUID()

        dataSource.setDPlayer(dPlayerData, uuid)

        val dPlayer = dataSource.getDPlayer(uuid)
        assertEquals(dPlayerData.toDPlayerData1(uuid), dPlayer.serializeData())

    }

    @Test
    fun invalidPlayer(){
        val dataSource = YMLDataSource(path, bukkitWrapper)

        assertEquals(DPlayer(playerMock).serializeData(), dataSource.getDPlayer(playerUUID).serializeData())
    }

    @Test
    fun faultyConfig(){
        val dataSource = YMLDataSource(path, bukkitWrapper)
        val config = YamlConfiguration()
        config.load(dataSource.filePath)

        config.createSection("${YMLDataSource.dPlayerDataPath}.$playerUUID")

        assertEquals(DPlayer(playerMock).serializeData(), dataSource.getDPlayer(playerUUID).serializeData())
    }
}