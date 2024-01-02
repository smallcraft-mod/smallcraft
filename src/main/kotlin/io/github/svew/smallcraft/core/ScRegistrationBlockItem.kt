package io.github.svew.smallcraft.core

import net.minecraft.world.item.Item

abstract class ScBlockItemRegistration<T : ScBlockItem>
    : ScRegistration<T>()
    , ScCreativeModeTabsRegistration<T>
{
    /**
     * Item behaviours
     */
    abstract val properties: Item.Properties
    /**
     * Returns the block this item represents
     */
    abstract val block: ScBlock
}