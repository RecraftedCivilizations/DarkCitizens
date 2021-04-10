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

/**
 * @author DarkVanityOfLight
 */

/**
 * Represents an Action as GUI Item
 * @param itemStack The ItemStack to represent as
 */
private class ActionItem(override val itemStack: ItemStack): DisplayItem{
    override fun clone(): DisplayItem {
        return ActionItem(itemStack)
    }

}

/**
 * Represents an [ITask] as GUI Item
 * @param itemStack The ItemStack to represent as
 * @param task The task to represent
 */
class TaskItem(override val itemStack: ItemStack, val task: ITask) : Clickable(itemStack) {
    private val actionGUI: InventoryGUI

    // Create the action gui
    init {

        var invSize = task.actions.size + 1
        if(invSize % 9 != 0){
            invSize += (9 - (invSize % 9))
        }

        actionGUI = InventoryGUI(invSize,"Actions")

        // Create a new action item for each task
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

    /**
     * Show the action gui for that task
     * @param player The player to show the action gui too
     */
    override fun onClick(player: Player) {
        actionGUI.show(player)
    }
}