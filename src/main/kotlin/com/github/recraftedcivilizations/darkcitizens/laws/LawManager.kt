package com.github.recraftedcivilizations.darkcitizens.laws

class LawManager {
    private val laws: MutableSet<ILaw> = emptySet<ILaw>().toMutableSet()
    private val taxLaw: TaxLaw = LawFactory.createLaw(LawType.TAXLAW, amount = 0) as TaxLaw

    init {
        laws.add(taxLaw)
    }

    /**
     * Set the amount players get taxed
     * @param amount The amount to tax them in %
     */
    fun setTaxAmount(amount: Int){
        if (amount > 100 || amount < 0){
            throw IllegalArgumentException("The tax amount has to be between 0 and 100%")
        }else{
            taxLaw.taxAmount = amount
        }
    }

    /**
     * get the current amount with witch players get taxed
     * @return The amount players get taxed with
     */
    fun getTaxAmount(): Int{
        return taxLaw.taxAmount
    }

    /**
     * Get a specific law
     * @param name The name of the law, cases do not matter
     * @return The law or null if no law with this name was found
     */
    fun getLaw(name: String): ILaw?{
        for (law in laws){
            if (law.name.equals(name, true)){
                return  law
            }
        }

        return null
    }

    /**
     * Create a new law
     * @param name The name of the law
     * @param description A description of the law
     */
    fun createLaw(name: String, description: String){
        val law = LawFactory.createLaw(LawType.CUSTOMLAW, description, name)
        laws.add(law)
    }

    /**
     * Remove an existing law
     * @param name The name of the law to remove
     */
    fun removeLaw(name: String){
        for (law in laws){
            if (law.name == name){
                laws.remove(law)
                break
            }
        }
    }

    /**
     * Get all current laws
     * @return An immutable set of all laws
     */
    fun getLaws(): Set<ILaw> {
        return laws
    }

}