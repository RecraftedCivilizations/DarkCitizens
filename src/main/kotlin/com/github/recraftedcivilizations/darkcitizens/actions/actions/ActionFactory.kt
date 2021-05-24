package com.github.recraftedcivilizations.darkcitizens.actions.actions

import com.github.recraftedcivilizations.darkcitizens.actions.IAction
import com.github.recraftedcivilizations.darkcitizens.dPlayer.DPlayerManager
import org.bukkit.Material
import org.bukkit.block.Block

enum class ActionType{
    ChopWood,
    CraftItem,
    Fish,
    MineBlock
}

object ActionFactory {

    /**
     * Creates a new action and register it
     * @param type The action type
     */
    fun createNewAction(type: ActionType, name: String, description: String, number: Int?, itemType: Material?, block: Block?, dPlayerManager: DPlayerManager?){

        val action: IAction = when (type){
            ActionType.ChopWood -> ChopWood(name, description, number!!, dPlayerManager!!)
            ActionType.CraftItem -> CraftItem(name, description, number!!, itemType!!, dPlayerManager!!)
            ActionType.Fish -> Fish(name, description, number!!)
            ActionType.MineBlock -> MineBlock(name, description, block!!, number!!, dPlayerManager!!)
        }

        action.register()
    }
}