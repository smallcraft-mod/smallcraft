package io.github.svew.smallcraft.core.registrations

import io.github.svew.smallcraft.core.ScBlock
import io.github.svew.smallcraft.core.ScCreativeModeTabsRegistration
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraftforge.client.model.generators.ItemModelProvider

abstract class ScBlockItemRegistration<T : BlockItem>
    : ScRegistration<T>()
    , ScCreativeModeTabsRegistration<T>
    , ScItemModelRegistration
{
    /**
     * Item behaviours
     */
    abstract val properties: Item.Properties
    /**
     * Returns the block this item represents
     */
    abstract val block: ScBlock

    // Since it's a block item, we probably just want the block model
    override fun addModel(provider: ItemModelProvider) = null
}