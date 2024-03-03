package io.github.svew.smallcraft.block

import io.github.svew.smallcraft.core.ScBlockLootProvider
import io.github.svew.smallcraft.core.registrations.ScBlockRegistration
import io.github.svew.smallcraft.registries.ModItems.DIRT_ITEM
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor

object DirtBlockRegistration : ScBlockRegistration<Block>()
{
    override val id = "dirt"
    override val name = "Dirt"
    override val new = { Block(this.properties) }
    override val properties: BlockBehaviour.Properties = BlockBehaviour.Properties.of()
        .mapColor(MapColor.DIRT)
        .strength(0.5F)
        .sound(SoundType.GRAVEL)
    override fun addLootTable(reg: ScBlockLootProvider) = reg.createSingleItemTable(DIRT_ITEM, 4)
}

