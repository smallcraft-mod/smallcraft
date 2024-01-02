package io.github.svew.smallcraft.core

import net.minecraft.world.item.Item

abstract class ScItemRegistration<T : ScItem>
    : ScRegistration<T>()
    , ScCreativeModeTabsRegistration<T>
{
    /**
     * Item behaviours
     */
    abstract val properties: Item.Properties
}