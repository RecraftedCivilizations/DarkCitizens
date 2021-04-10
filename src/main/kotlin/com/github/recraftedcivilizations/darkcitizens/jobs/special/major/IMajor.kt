package com.github.recraftedcivilizations.darkcitizens.jobs.special.major

import com.github.recraftedcivilizations.darkcitizens.jobs.IJob
import com.github.recraftedcivilizations.darkcitizens.laws.LawManager

interface IMajor : IJob {
    val lawManager: LawManager

    /**
     * Create a new Law
     * @param name The name of the law
     * @param description The description of the law
     */
    fun createLaw(name: String, description: String)

    /**
     * Remove an existing law
     * @param name The name of the law
     */
    fun removeLaw(name: String)

    /**
     * Set the current tax law
     * @param amount The tax percentage has to be <100 and >0
     */
    fun setTaxLaw(amount: Int)
}