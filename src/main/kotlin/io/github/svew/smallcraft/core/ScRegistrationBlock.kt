package io.github.svew.smallcraft.core

import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour

abstract class ScBlockRegistration<T : ScBlock>
    : ScRegistration<T>()
{
    /**
     * Block behaviours
     */
    abstract val properties: BlockBehaviour.Properties
    /**
     * Tags to be applied to the block
     */
    open val tags: Array<TagKey<Block>> = arrayOf()

    /**
     * Registers the drop of the block to the loot table
     * Don't override if the block doesn't drop anything
     */
    open fun addLootTable(registry: ScLootTableRegistry)
    {
        registry.dropSelf(obj)
    }
}