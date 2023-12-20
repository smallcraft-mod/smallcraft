package io.github.svew.smallcraft.core

import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

interface IScBlockTagsRegistration
{
    val tags: Array<TagKey<Block>>
}
