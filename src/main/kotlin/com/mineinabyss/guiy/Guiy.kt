package com.mineinabyss.guiy

import com.mineinabyss.guiy.inventory.GuiyEventListener
import com.mineinabyss.guiy.inventory.GuiyInventoryHolder
import com.mineinabyss.guiy.inventory.GuiyScopeManager
import kotlinx.coroutines.cancel
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import kotlin.properties.Delegates

private var pl by Delegates.notNull<JavaPlugin>();
val guiyPlugin get() = pl


object Guiy {
    fun enable(plugin: JavaPlugin) {
        plugin.server.pluginManager.registerEvents(GuiyEventListener(), plugin)
        pl = plugin
    }
    fun disable(plugin: JavaPlugin) {
        GuiyScopeManager.scopes.forEach { it.cancel() }
        Bukkit.getOnlinePlayers()
            .mapNotNull { it.openInventory.topInventory.holder as? GuiyInventoryHolder }
            .forEach { it.close() }
    }
}
