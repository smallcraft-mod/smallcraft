package io.github.svew.smallcraft.core

import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab
import thedarkcolour.kotlinforforge.forge.ObjectHolderDelegate

interface IScRegistration<T>
{
    val obj: T
    val id: String
}

abstract class ScRegistration<T> : IScRegistration<T>
{
    override val obj: T get() = holder?.get() ?: throw IllegalStateException("Object hasn't been registered yet")

    abstract val new: () -> T

    private var holder: ObjectHolderDelegate<T>? = null

    fun setObj(holder: ObjectHolderDelegate<T>)
    {
        if (this.holder != null) throw IllegalStateException("Object has already been registered")
        this.holder = holder
    }
}

interface ScCreativeModeTabsRegistration<T>
    : IScRegistration<T>
{
    /**
     * Item creative mode tabs
     */
    val creativeModeTabs: Set<ResourceKey<CreativeModeTab>>
}