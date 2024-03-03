package io.github.svew.smallcraft.core

import io.github.svew.smallcraft.core.registrations.IScRegistration
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.level.ItemLike

interface ScCreativeModeTabsRegistration<T>
    : IScRegistration<T>
    where T : ItemLike
{
    /**
     * Item creative mode tabs
     */
    val creativeModeTabs: Set<ResourceKey<CreativeModeTab>>
}