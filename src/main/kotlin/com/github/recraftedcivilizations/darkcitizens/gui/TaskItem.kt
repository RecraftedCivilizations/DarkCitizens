package com.github.recraftedcivilizations.darkcitizens.gui

import com.github.darkvanityoflight.recraftedcore.gui.Clickable
import com.github.darkvanityoflight.recraftedcore.gui.DisplayItem
import com.github.darkvanityoflight.recraftedcore.gui.InventoryGUI
import com.github.darkvanityoflight.recraftedcore.gui.elements.CloseButtonFactory
import com.github.darkvanityoflight.recraftedcore.utils.itemutils.setLore
import com.github.darkvanityoflight.recraftedcore.utils.itemutils.setName
import com.github.recraftedcivilizations.darkcitizens.tasks.ITask
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

private class ActionItem(override val itemStack: ItemStack): DisplayItem{
    override fun clone(): DisplayItem {
        return ActionItem(itemStack)
    }

}

class TaskItem(override val itemStack: ItemStack, val task: ITask) : Clickable(itemStack) {
    private val actionGUI: InventoryGUI

    init {

        var invSize = task.actions.size + 1
        if(invSize % 9 != 0){
            invSize += (9 - (invSize % 9))
        }

        actionGUI = InventoryGUI(invSize,"Actions")

        for (action in task.actions){
            val actionItemStack = ItemStack(Material.STONE)
            actionItemStack.setName(action.name)
            actionItemStack.setLore(action.description)
            val item = ActionItem(actionItemStack)
            actionGUI.addItem(item)
        }

        actionGUI.setSlot(CloseButtonFactory.getCloseButton(), invSize-1)
    }

    override fun clone(): DisplayItem {
        return TaskItem(itemStack, task)
    }

    override fun onClick(player: Player) {
        actionGUI.show(player)
    }
}