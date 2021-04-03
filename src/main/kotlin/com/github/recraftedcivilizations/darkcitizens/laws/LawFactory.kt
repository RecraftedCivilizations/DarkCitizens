package com.github.recraftedcivilizations.darkcitizens.laws

/**
 * @author DarkVanityOfLight
 */

/**
 * Describes all laws that exist
 */
enum class LawType{

    TAXLAW,
    CUSTOMLAW,
}

/**
 * Create laws
 */
object LawFactory {

    /**
     * Create a new law based on the type you requested
     * @see LawType
     * @param type The law type you want
     * @param description Only needs to be supplied if you want a [CustomLaw]
     * @param name Only needs to be supplied if you want a [CustomLaw]
     * @param amount The percentage of taxes that will be paid, only needs to be supplied if you want a [TaxLaw]
     */
    fun createLaw(type: LawType, description: String? = null, name: String? = null, amount: Int? = null): ILaw {
        return when (type) {
            LawType.TAXLAW -> {
                TaxLaw(amount!!)
            }
            LawType.CUSTOMLAW -> {
                CustomLaw(description!!, name!!)
            }
            else -> {
                throw IllegalArgumentException("The supplied args do not match your law type")
            }
        }
    }
}