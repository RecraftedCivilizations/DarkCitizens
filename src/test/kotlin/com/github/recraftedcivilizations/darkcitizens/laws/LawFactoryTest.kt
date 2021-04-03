package com.github.recraftedcivilizations.darkcitizens.laws

import com.github.recraftedcivilizations.jobs.randomString
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random

internal class LawFactoryTest {

    @Test
    fun createCustomLaw() {
        val description = randomString()
        val name = randomString()

        val law = LawFactory.createLaw(LawType.CUSTOMLAW, description, name)

        assertEquals(description, law.description)
        assertEquals(name, law.name)
    }

    @Test
    fun createTaxLaw(){
        val amount = Random.nextInt()
        val law = LawFactory.createLaw(LawType.TAXLAW, amount = amount)

        assertEquals("We all love Taxes", law.description)
        assertEquals("Taxes", law.name)
    }

    @Test
    fun nonValidLaw(){
        assertThrows(NullPointerException::class.java){
            LawFactory.createLaw(LawType.CUSTOMLAW, null, null, null)
        }
    }


}