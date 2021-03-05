package com.github.recraftedcivilizations.darkcitizens.gui

import com.github.darkvanityoflight.recraftedcore.gui.Clickable
import com.github.recraftedcivilizations.darkcitizens.tasks.ITask
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class TaskItem(override val itemStack: ItemStack, val task: ITask) : Clickable(itemStack) {
    override fun onClick(player: Player) {
        TODO("Not yet implemented")
    }
}