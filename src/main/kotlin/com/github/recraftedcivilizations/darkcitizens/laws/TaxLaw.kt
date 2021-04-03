package com.github.recraftedcivilizations.darkcitizens.laws

data class TaxLaw(var taxAmount: Int) : ILaw{ override val description: String = "We all love Taxes" }