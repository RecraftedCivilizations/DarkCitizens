package com.github.recraftedcivilizations.darkcitizens.laws

import com.github.recraftedcivilizations.jobs.randomString
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.lang.IllegalArgumentException
import kotlin.random.Random

internal class LawManagerTest {

     fun createLawArgs(): Map<Any, Any>{

        return mapOf(
            Pair("name", randomString()),
            Pair("description", randomString())
        )
    }

    fun assertLaw(law: ILaw, lawArgs: Map<Any, Any>) {

    }

    @Test
    fun setTaxAmount() {
        val amount = Random.nextInt(100)

        val manager = LawManager()

        manager.setTaxAmount(amount)

        assertEquals(amount, manager.getTaxAmount())
    }

    @Test
    fun setTaxAmountException(){
        val manager = LawManager()

        var amount  = Random.nextInt()
        while (amount in 1..99){
            amount = Random.nextInt()
        }

        assertThrows(IllegalArgumentException::class.java){
            manager.setTaxAmount(amount)
        }

    }

    @Test
    fun createLaw() {
        val args = createLawArgs()
        val lawManager = LawManager()

        lawManager.createLaw(args["name"] as String, args["description"] as String)

        assertLaw(lawManager.getLaw(args["name"] as String)!!, args)

    }

    @Test
    fun removeLaw() {
        val args = createLawArgs()
        val lawManager = LawManager()

        lawManager.createLaw(args["name"] as String, args["description"] as String)

        lawManager.removeLaw(args["name"] as String)

        assertEquals(null, lawManager.getLaw(args["name"] as String))
    }

    @Test
    fun getLaws() {
        val args = createLawArgs()
        val lawManager = LawManager()

        lawManager.createLaw(args["name"] as String, args["description"] as String)

        val laws = lawManager.getLaws()

        assertEquals(2, laws.size)
    }
}