package io.github.svew.smallcraft.util

import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

interface ITaggedBlock {
    val TAGS: Array<TagKey<Block>>
}

interface ITaggedItem {
    val TAGS: Array<TagKey<Item>>
}