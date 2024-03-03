package io.github.svew.smallcraft.core.registrations

import io.github.svew.smallcraft.core.ScCreativeModeTabsRegistration
import net.minecraft.world.item.Item

abstract class ScItemRegistration<T : Item>
    : ScRegistration<T>()
    , ScCreativeModeTabsRegistration<T>
    , ScItemModelRegistration
    , ScNameRegistration<T>
{
    /**
     * Item behaviours
     */
    abstract val properties: Item.Properties
}