package com.mineinabyss.guiy.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mineinabyss.guiy.components.canvases.LocalInventory
import com.mineinabyss.guiy.layout.Layout
import com.mineinabyss.guiy.layout.MeasureResult
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.sizeIn
import com.mineinabyss.idofront.items.editItemMeta
import com.mineinabyss.idofront.textcomponents.miniMsg
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * An item to display in an inventory layout.
 *
 * @param itemStack The [ItemStack] to display.
 */
@Composable
fun Item(itemStack: ItemStack?, modifier: Modifier = Modifier) {
    val canvas = LocalInventory.current
    Layout(
        measurePolicy = { _, constraints ->
            MeasureResult(constraints.minWidth, constraints.minHeight) {}
        },
        renderer = { node ->
            val inv = canvas
            for (x in 0 until node.width)
                for (y in 0 until node.height)
                    set(inv, x, y, itemStack)
        },
        modifier = Modifier.sizeIn(minWidth = 1, minHeight = 1).then(modifier)
    )
}

/**
 * An item to display in an inventory layout.
 *
 * @param material The [Material] of the item.
 * @param title The item's display name (formatted by MiniMesssage).
 * @param amount The amount of the item.
 * @param lore The item's lore (formatted by MiniMessage).
 */
@Composable
fun Item(
    material: Material,
    title: String? = null,
    amount: Int = 1,
    lore: List<String> = listOf(),
    modifier: Modifier = Modifier,
) {
    val titleMM = remember(title) { title?.miniMsg() }
    val loreMM = remember(lore) { lore.map { it.miniMsg() } }

    Item(ItemStack(material, amount).editItemMeta {
        itemName(titleMM)
        lore(loreMM)
    }, modifier)
}
